package dev.fredpena.springoauth2.google;

/**
 * @author me@fredpena.dev
 * @created 26/03/2024  - 23:36
 */


public record GoogleAccount(String clientId, String clientSecret, String redirectUri,
                            String authUri, String tokenUri, String accessType,
                            String responseType, String scope) {

}
