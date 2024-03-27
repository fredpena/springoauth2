package dev.fredpena.springoauth2.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import dev.fredpena.springoauth2.google.GoogleAccount;

@PageTitle("Auth")
@Route(value = "auth")
@RouteAlias(value = "")
public class AuthView extends VerticalLayout {


    public AuthView(GoogleAccount googleAccount) {

        var link = "%s?access_type=%s&client_id=%s&redirect_uri=%s&response_type=%s&scope=%s".formatted(googleAccount.authUri(), googleAccount.accessType(), googleAccount.clientId(), googleAccount.redirectUri(), googleAccount.responseType(), googleAccount.scope());

        Button auth = new Button("Auth");
        auth.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        auth.addClickListener(click -> {
            UI.getCurrent().getPage().open(link, "_self");
        });

        add(auth);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
