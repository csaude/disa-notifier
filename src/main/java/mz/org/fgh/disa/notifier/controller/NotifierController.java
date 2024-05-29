package mz.org.fgh.disa.notifier.controller;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import lombok.extern.log4j.Log4j2;
import mz.org.fgh.disa.notifier.service.EmailService;

@RestController
@RequestMapping("notification")
@Log4j2
public class NotifierController {

	private EmailService emailService;

	@Autowired
	public NotifierController(EmailService emailService) {
		this.emailService = emailService;
	}

	@PostMapping
	public ResponseEntity<String> sendNotification(@RequestPart("data") JsonNode data,
			@RequestParam(name = "attachment", required = false) MultipartFile file) {

		log.debug("json being received..." + data);

		if (data == null) {
			return ResponseEntity.badRequest().body("data parameter is required");
		}

		try {
			String attachmentName = "";

			ObjectMapper objectMapper = new ObjectMapper();
			String[] to = objectMapper.convertValue(data.get("to"), String[].class);
			String subject = objectMapper.convertValue(data.get("subject"), String.class);
			String body = objectMapper.convertValue(data.get("body"), String.class);
			String module = objectMapper.convertValue(data.get("module"), String.class);
			String startDate = objectMapper.convertValue(data.get("startDate"), String.class);
			String endDate = objectMapper.convertValue(data.get("endDate"), String.class);
			String repoLink = objectMapper.convertValue(data.get("repoLink"), String.class);

			byte[] attachment = null;

			if (file != null && !file.isEmpty()) {
				attachmentName = file.getOriginalFilename();
				try (InputStream inputStream = file.getInputStream()) {
					attachment = IOUtils.toByteArray(inputStream);
				}
			}

			emailService.sendEmail(to, subject, body, attachment, attachmentName, module, startDate, endDate, repoLink); 

			return ResponseEntity.ok("Email sent successfully.");
		} catch (AddressException e) {
			log.info("Bad address: {} {}", data.get("to"), e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (IOException | MessagingException e) {
			log.error(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to send email.");
		}
	}
}
