package dev.fredpena.springoauth2.google;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author me@fredpena.dev
 * @created 30/11/2023  - 22:48
 */
@Slf4j
@Component
public class GoogleAuthorization extends AuthorizationCodeInstalledApp {

    private final GoogleAccount googleAccount;

    public GoogleAuthorization(AuthorizationCodeFlow flow, GoogleAccount googleAccount) {
        super(flow, new LocalServerReceiver());
        this.googleAccount = googleAccount;
    }


    public Credential credential(String userId) throws IOException {
        return authorize(userId, null);
    }

    public Credential authorize(String userId, String code) throws IOException {
        Credential credential = getFlow().loadCredential(userId);
        if (credential != null) {
            // Check if token is expired and refresh if necessary
            if (credential.getExpiresInSeconds() != null && credential.getExpiresInSeconds() < 180) {
                log.info("Token refreshed.");

                credential.refreshToken();
            }

            return credential;
        }

        if (code == null) {
            return null;
        }

        TokenResponse response = getFlow().newTokenRequest(code).setRedirectUri(googleAccount.redirectUri()).execute();

        if (response.getRefreshToken() != null) {
            // If a Refresh Token is not provided in the authorization response,
            // it's important to find a place to store it persistently for future use.
            // The Refresh Token is essential for obtaining new access tokens once
            // the current access tokens expire, so its storage
            // and management are critical to ensure the continuous functionality of the system.
        } else {
            // If a Refresh Token already exists for the user, retrieve it
            // and include it in the response using response.setRefreshToken(...).
        }

        // Store credential and return it
        return getFlow().createAndStoreCredential(response, userId);
    }


}
