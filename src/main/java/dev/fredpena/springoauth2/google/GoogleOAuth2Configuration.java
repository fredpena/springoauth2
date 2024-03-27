package dev.fredpena.springoauth2.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * @author me@fredpena.dev
 * @created 30/11/2023  - 14:54
 */

@Configuration
@RequiredArgsConstructor
public class GoogleOAuth2Configuration {

    @Value("${account.google.client-id:unknown}")
    private String clientId;

    @Value("${account.google.client-secret:unknown}")
    private String clientSecret;

    @Value("${account.google.redirect-uri:unknown}")
    private String redirectUri;

    @Value("${account.google.auth-uri:unknown}")
    private String authUri;

    @Value("${account.google.token-uri:unknown}")
    private String tokenUri;

    @Value("${account.google.access-type:unknown}")
    private String accessType;

    @Value("${account.google.response-type:unknown}")
    private String responseType;

    @Value("${account.google.scope:unknown}")
    private String scope;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    @Bean
    public NetHttpTransport configureNetHttpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    @Bean
    public GoogleAccount loadGoogleAccount() {

        return new GoogleAccount(clientId, clientSecret, redirectUri, authUri, tokenUri, accessType, responseType, scope);

    }

    private GoogleClientSecrets.Details loadGoogleClientSecrets() {
        GoogleClientSecrets.Details details = new GoogleClientSecrets.Details();

        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setRedirectUris(List.of(redirectUri));
        details.setAuthUri(authUri);
        details.setTokenUri(tokenUri);

        return details;
    }

    @Bean
    public GoogleAuthorizationCodeFlow configureFlow() throws IOException, GeneralSecurityException {

        GoogleClientSecrets.Details details = loadGoogleClientSecrets();

        GoogleClientSecrets googleClientSecrets = new GoogleClientSecrets();
        googleClientSecrets.setWeb(details);


        // If you prefer to use the file-based approach for OAuth2 client credentials,
        // uncomment the lines below and comment out the ones above.

        // Resource file path for OAuth2 client credentials JSON file
        // String CREDENTIALS_FILE_PATH = "path/to/your/credentials.json";

        // Uncomment the lines below to load OAuth2 client credentials from a JSON file

        // final InputStream in = getClass().getResourceAsStream(CREDENTIALS_FILE_PATH);
        // if (in == null) {
        //     throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        // }
        // GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        return new GoogleAuthorizationCodeFlow.Builder(
                configureNetHttpTransport(), JSON_FACTORY, googleClientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType(accessType)
                .build();

    }


}
