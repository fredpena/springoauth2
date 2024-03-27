package dev.fredpena.springoauth2.google;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author me@fredpena.dev
 * @created 01/12/2023  - 00:27
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleCalendarService {
    private static final String USER_ID = "user";
    private static final String APPLICATION_NAME = "TEST";

    private final GoogleAuthorization authorization;
    private final NetHttpTransport httpTransport;
    private final AuthorizationCodeFlow flow;

    public void createAndStoreCredential(String code, String scope) throws IOException {
        createAndStoreCredential(USER_ID, code, scope);
    }

    public void createAndStoreCredential(String user, String code, String scope) throws IOException {
        log.info("Authorization Code: {}", code);
        log.info("Scope: {}", scope);
        //user In a multi-tenant environment change this value to the tenant value
        authorization.authorize(user, code);
    }

    public boolean hasCredential() {
        return hasCredential(USER_ID);
    }

    public boolean hasCredential(String userId) {
        try {
            return authorization.credential(userId) != null;
        } catch (IOException e) {
            return false;
        }
    }


    public Calendar getCalendar() {
        try {
            Credential credential = authorization.credential(USER_ID);

            return new Calendar.Builder(httpTransport,
                    GsonFactory.getDefaultInstance(), credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Event> getAllHoliday(DateTime timeMin, DateTime timeMax) {
        try {
            Events events = getCalendar().events()
                    .list("es.do#holiday@group.v.calendar.google.com")
                    //.setMaxResults(10)
                    .setTimeMin(timeMin)
                    .setTimeMax(timeMax)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            return events.getItems();

        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public List<Event> nextEvents(Integer maxResults, String language, String country) {
        try {
            // List the next n events from the primary calendar.
            DateTime now = new DateTime(System.currentTimeMillis());

            Events events = getCalendar().events()
                    .list("%s.%s#holiday@group.v.calendar.google.com".formatted(language, country))
                    .setMaxResults(maxResults)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            return events.getItems();

        } catch (IOException e) {
            return new ArrayList<>();
        }
    }


    public void tryGetEvents() {
        try {
            Calendar calendar = getCalendar();

            // List the next 10 events from the primary calendar.
            DateTime now = new DateTime(System.currentTimeMillis());

            Events events = calendar.events()
                    .list("es.do#holiday@group.v.calendar.google.com")
                    .setMaxResults(1)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            List<Event> items = events.getItems();
            if (items.isEmpty()) {
                System.out.println("No upcoming events found.");
            } else {
                System.out.println("Upcoming events");
                for (Event event : items) {
                    DateTime start = event.getStart().getDateTime();
                    if (start == null) {
                        start = event.getStart().getDate();
                    }
                    System.out.printf("%s (%s)\n", event.getSummary(), start);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
