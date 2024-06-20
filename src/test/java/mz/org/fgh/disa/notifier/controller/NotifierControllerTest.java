package mz.org.fgh.disa.notifier.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import mz.org.fgh.disa.notifier.service.EmailService;

@SpringBootTest
public class NotifierControllerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotifierController notifierController;

    @Test
    public void sendNotificationShouldHandleWrongToAddress() throws MessagingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = objectMapper.createObjectNode();
        ArrayNode emails = objectMapper.createArrayNode();
        emails.add("emailShouldBeSeparatedByColon@example.com;secondEmail@example.com");
        root.set("to", emails);
        doThrow(AddressException.class).when(emailService).sendEmail(any(), any(), any(),
                any(), any(), any(), any(), any());
        ResponseEntity<String> sendNotification = notifierController.sendNotification(root);
        assertThat(sendNotification.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
