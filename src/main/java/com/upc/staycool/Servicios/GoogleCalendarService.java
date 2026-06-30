package com.upc.staycool.Servicios;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "StayCool UPC";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    
    // IMPORTANTE: Debes reemplazar estos valores con los de tu Google Cloud Console
    public static final String CLIENT_ID = "1037439397962-9fpk1c26scm7vb3j07plcil9la8k5c1h.apps.googleusercontent.com";
    public static final String CLIENT_SECRET = "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";
    public static final String REDIRECT_URI = "http://localhost:8081/api/v1/calendar/callback";

    private GoogleAuthorizationCodeFlow getFlow() throws GeneralSecurityException, IOException {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPES)
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();
    }

    public String getAuthorizationUrl(Long eventId, Long userId) throws GeneralSecurityException, IOException {
        GoogleAuthorizationCodeFlow flow = getFlow();
        return flow.newAuthorizationUrl()
                .setRedirectUri(REDIRECT_URI)
                .setState(eventId + "_" + userId)
                .build();
    }

    public Event crearEventoConToken(String code, String resumen, String descripcion, String fechaInicio, String fechaFin) throws IOException, GeneralSecurityException {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleAuthorizationCodeFlow flow = getFlow();
        
        // Intercambiar el código por el token de acceso
        TokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
        Credential credential = flow.createAndStoreCredential(response, "user");

        Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        Event event = new Event()
                .setSummary(resumen)
                .setDescription(descripcion != null ? descripcion : "Cita programada desde la plataforma StayCool.");

        EventDateTime start = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(fechaInicio))
                .setTimeZone("America/Lima");
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(fechaFin))
                .setTimeZone("America/Lima");
        event.setEnd(end);

        event = service.events().insert("primary", event).execute();
        System.out.printf("Evento despachado a Google Calendar exitosamente: %s\n", event.getHtmlLink());

        return event;
    }
}