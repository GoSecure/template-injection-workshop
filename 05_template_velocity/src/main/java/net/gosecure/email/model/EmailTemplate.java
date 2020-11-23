package net.gosecure.email.model;

public class EmailTemplate {

    private String subject;
    private String templateCode;

    public EmailTemplate() {}

    public EmailTemplate(String subject, String templateCode) {
        this.subject = subject;
        this.templateCode = templateCode;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }
}
