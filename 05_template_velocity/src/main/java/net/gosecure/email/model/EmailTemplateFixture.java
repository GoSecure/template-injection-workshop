package net.gosecure.email.model;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmailTemplateFixture {


    public static Map<Integer,EmailTemplate> loadTemplates() {
        //Provisioning email template to the session attributes
        Map<Integer,EmailTemplate> templates = new HashMap<>();
        templates.put(1, new EmailTemplate("New Subscriber",convertStreamToString("/email/new_offer.html")));
        templates.put(2, new EmailTemplate("New Offer",     convertStreamToString("/email/new_sub.html")));

        return templates;
    }

    private static String convertStreamToString(String templateFile) {
        try {
            InputStream is = EmailTemplateFixture.class.getResourceAsStream(templateFile);
            return new String(FileCopyUtils.copyToByteArray(is), StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
