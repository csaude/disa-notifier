package mz.org.fgh.disa.notifier.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Nullable;

public class EmailDTO {

    private String to;
    private String subject;
    private String body;
    private String module;
    @Nullable
    private MultipartFile attachment;
    
    public EmailDTO() {
    	super();
	}

    public EmailDTO(String to, String subject, String body, String module, MultipartFile attachment) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.module = module;
        this.attachment = attachment;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public MultipartFile getAttachment() {
        return attachment;
    }

    public void setAttachment(MultipartFile attachment) {
        this.attachment = attachment;
    }
}

