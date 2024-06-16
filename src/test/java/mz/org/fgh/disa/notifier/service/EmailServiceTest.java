package mz.org.fgh.disa.notifier.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.mail.internet.AddressException;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test()
    public void sendEmailThrowsExceptionForInvalidAddress() {
        assertThrows(AddressException.class, () -> {
            String[] to = new String[] { "emailShouldBeSeparatedByColon@example.com;secondEmail@example.com" };
            emailService.sendEmail(to,
                    "", "", null, null, "", "", null);
        });
    }
}
