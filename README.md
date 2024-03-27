# Integration of OAuth2 and Spring Boot with Vaadin Flow

This project is a demo intended to test the integration concept of `OAuth2`, `Spring Boot`, and `Vaadin Flow`. It
focuses on implementing an OAuth2 flow, along with the Spring Boot framework for server logic and Vaadin Flow for the
visual part of the application.

## Prerequisites

Before running this project, make sure to have the following properties configured in the `application.properties` file:

```properties
account.google.client-id:${ACCOUNT_GOOGLE_CLIENT_ID:...}
account.google.client-secret:${ACCOUNT_GOOGLE_SECRET:...}
account.google.redirect-uri:${ACCOUNT_GOOGLE_REDIRECT_URI:https://.../api/google/callback}
```

Replace the `...` values with your Google account configuration. You can follow
the [Google Calendar API Quickstart](https://developers.google.com/calendar/api/quickstart/js) guide
to obtain these values.

## Google Console Configuration

To correctly configure `OAuth2` credentials in the `Google Console`, follow these steps:

1. Create a new project: Create a new project in the Google Console for your application.
2. Enable the API: Enable the corresponding API, in this case, the Google Calendar API.
3. Configure OAuth consent: Configure the `OAuth consent screen` with your application details.
4. Authorize credentials for a web application: Create OAuth2 credentials of type `OAuth 2.0 Client IDs` for a `web
   application` and ensure to provide the correct redirect URI.

To generate the correct redirect URI, you need an https protocol. To achieve this, we can use `ngrok`.

## ngrok Configuration

To ensure the correct functioning of the `Webhook` in the `OAuth 2.0 Client IDs` section specifically in `Authorized
redirect URIs`, it is mandatory to provide a URL with the HTTPS protocol. This is essential to maintain the security of
communications between the server and the client application.

Steps to configure ngrok:

1. Download ngrok from ngrok.com
2. Unzip the downloaded file and start ngrok in your terminal by running the following command (replace PORT with the
   port
   your local server is running on):

```shell script
ngrok http 54154
````

Ngrok will generate a public URL that you can use as the `Authorized redirect URI` in the `Google Console`.

With these configurations correctly set up in the `Google Console` and the corresponding values entered in the
`application.properties` file, the application should function correctly after running the command

```shell script
# Mac & Linux
./mvnw
````

```shell script
# Windows
mvnw
```

**Note:**

- This demo focuses on Google Calendar to display holidays in the locality configured in the calendar. The visual part
  of
  Vaadin Flow displays these holidays to provide useful functionality to the user.

- Remember that this application is designed as a demonstration and can be extended to include more features and
  functionalities as needed.
