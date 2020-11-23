package net.gosecure.email.model;

import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PageTemplateFixture {


    public static Map<Integer, PageTemplate> loadTemplates() {
        //Provisioning email template to the session attributes
        Map<Integer, PageTemplate> templates = new HashMap<>();
        templates.put(1, new PageTemplate("Welcome Page",     convertStreamToString("/tpl/welcome_page.html")));
        templates.put(2, new PageTemplate("Lorem Ipsum",convertStreamToString("/tpl/lorem_ipsum.html")));
        templates.put(3, new PageTemplate("Error Not Found",     convertStreamToString("/tpl/page_404.html")));

        return templates;
    }

    private static String convertStreamToString(String templateFile) {
        try {
            InputStream is = PageTemplateFixture.class.getResourceAsStream(templateFile);
            return new String(FileCopyUtils.copyToByteArray(is), StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
