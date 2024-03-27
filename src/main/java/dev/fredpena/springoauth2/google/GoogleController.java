package dev.fredpena.springoauth2.google;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/google")
@RequiredArgsConstructor
public class GoogleController {

    private final GoogleCalendarService googleCalendarService;

    @GetMapping("/callback")
    public ResponseEntity<Void> getHandleGoogleCallback(
            @RequestParam("code") String authorizationCode,
            @RequestParam("scope") String scope) {


        try {
            googleCalendarService.createAndStoreCredential(authorizationCode, scope);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/google");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}
