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
	
	private String htmlTemplate;
	
	@Value("${spring.mail.username}")
    private String fromEmail;
	
	@Autowired
	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = Objects.requireNonNull(javaMailSender, "javaMailSender must not be null");
	}
	
	private TemplateEngine templateEngine;
	
	public void sendEmail(String[] to, String subject, 
			  			  String text,
			  			  byte[] attachment, String attachmentName, String module, String startDate, String endDate, String repoLink) throws MessagingException, IOException {
		
		Objects.requireNonNull(to, "email recipient must not be null");
		Objects.requireNonNull(subject, "email subject must not be null");
		Objects.requireNonNull(text, "email content must not be null");
		Objects.requireNonNull(module, "module calling the service must not be null");
		Objects.requireNonNull(repoLink, "repoLink must not be null");
		
		Context context = new Context();
		templateEngine = TemplateEngineUtils.getTemplateEngine();

		context.setVariable("dateAndTime", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
		context.setVariable("errorDescription", text);
		context.setVariable("fromDate", startDate);
		context.setVariable("toDate", endDate);
		context.setVariable("repoLink", repoLink);

				
		if (module.equalsIgnoreCase("notification")) {
			if (attachment==null) {
				htmlTemplate = templateEngine.process("no_results_report", context);
			} else {
				htmlTemplate = templateEngine.process("index_report", context);
			}
		} else {
			htmlTemplate = templateEngine.process("index_error", context);
		}
		
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(htmlTemplate, true);
		helper.setFrom(fromEmail, "[Disa-SESP Interop.]");
		
		/*if (attachment !=null) {
			ByteArrayResource attachmentResource = new ByteArrayResource(attachment);
			helper.addAttachment(attachmentName, attachmentResource);
		}*/
		
		javaMailSender.send(message);       
	}
}
