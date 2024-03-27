package dev.fredpena.springoauth2.views;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.fredpena.springoauth2.google.GoogleCalendarService;

import java.util.List;

@PageTitle("Google")
@Route(value = "google")
public class GoogleView extends VerticalLayout implements BeforeEnterObserver {

    private final GoogleCalendarService googleCalendarService;

    public GoogleView(GoogleCalendarService googleCalendarService) {
        this.googleCalendarService = googleCalendarService;

        ComboBox<Language> languageComboBox = new ComboBox<>("Language");
        languageComboBox.setItemLabelGenerator(Language::toString);
        languageComboBox.setItems(List.of(
                new Language("English", "en"),
                new Language("Español", "es"),
                new Language("Français", "fr"),
                new Language("Suomi", "fi"),
                new Language("Português", "pt")));
        languageComboBox.setValue(new Language("English", "en"));

        ComboBox<Country> countryComboBox = new ComboBox<>("Country");
        countryComboBox.setItemLabelGenerator(Country::toString);
        countryComboBox.setItems(List.of(
                new Country("United States", "usa"),
                new Country("United Kingdom", "uk"),
                new Country("España", "spain"),
                new Country("República Dominicana", "do"),
                new Country("France", "french"),
                new Country("Canada", "canadian"),
                new Country("Suomi", "finnish"),
                new Country("Sverige", "swedish"),
                new Country("Portugal", "portuguese"),
                new Country("Brasil", "brazilian")));
        countryComboBox.setValue(new Country("República Dominicana", "do"));

        IntegerField maxResults = new IntegerField("Max events");
        maxResults.setMin(1);
        maxResults.setStep(1);
        maxResults.setValue(10);
        maxResults.setStepButtonsVisible(true);
        Button view = new Button("View");

        VerticalLayout events = new VerticalLayout();
        events.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        view.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        view.addClickListener(click -> {
            events.removeAll();

            for (Event event : googleCalendarService.nextEvents(maxResults.getValue(), languageComboBox.getValue().code(), countryComboBox.getValue().code())) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                events.add(new Span("%s (%s)".formatted(event.getSummary(), start)));
            }
        });


        add(new HorizontalLayout(languageComboBox, countryComboBox), maxResults, view, events);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.START);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!googleCalendarService.hasCredential()) {
            beforeEnterEvent.forwardTo("auth");
        }
    }

    private record Language(String name, String code) {
        @Override
        public String toString() {
            return name + " (" + code + ")";
        }
    }

    private record Country(String name, String code) {
        @Override
        public String toString() {
            return name + " (" + code + ")";
        }
    }
}
