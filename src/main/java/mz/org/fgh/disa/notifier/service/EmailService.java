package mz.org.fgh.disa.notifier.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import mz.org.fgh.disa.notifier.util.TemplateEngineUtils;

@Service
public class EmailService {
	
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
    private String fromEmail;
	
	@Autowired
	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = Objects.requireNonNull(javaMailSender, "javaMailSender must not be null");
	}
	
	public void sendEmail(String[] recipients, String subject, String body, String module, 
			String startDate, String endDate, String repoLink, Boolean resultFlag) throws MessagingException, IOException {
  
		TemplateEngine templateEngine;
		String htmlTemplate;
		
		Objects.requireNonNull(recipients, "email recipient must not be null");
		Objects.requireNonNull(subject, "email subject must not be null");
		Objects.requireNonNull(body, "email content must not be null");
		Objects.requireNonNull(module, "module calling the service must not be null");
		Objects.requireNonNull(repoLink, "repoLink must not be null");
		
		Context context = new Context();
		templateEngine = TemplateEngineUtils.getTemplateEngine();

		context.setVariable("dateAndTime", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
		context.setVariable("errorDescription", body);
		context.setVariable("fromDate", startDate);
		context.setVariable("toDate", endDate);
		context.setVariable("repoLink", repoLink);

				
		if (module.equalsIgnoreCase("notification")) {
			if (Boolean.TRUE.equals(resultFlag)) {
				htmlTemplate = templateEngine.process("index_report", context);
			} else {
				htmlTemplate = templateEngine.process("no_results_report", context);
			}
		} else {
			htmlTemplate = templateEngine.process("index_error", context);
		}
		
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(recipients);
		helper.setSubject(subject);
		helper.setText(htmlTemplate, true);
		helper.setFrom(fromEmail, "[Disa-SESP Interop.]");
		
		javaMailSender.send(message);       
	}
}
