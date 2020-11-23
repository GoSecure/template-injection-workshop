package net.gosecure.email.controller;

import net.gosecure.email.model.EmailTemplate;
import net.gosecure.email.model.EmailTemplateFixture;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.util.Map;

@SessionAttributes("emailTemplates")
@Controller
public class EmailController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

	@RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
	public ModelAndView welcomePage() {

		ModelAndView model = new ModelAndView();
		model.setViewName("index");
		return model;

	}

	@RequestMapping("/redirect_any")
	public ModelAndView redirect_any(@RequestParam("view") String redirView) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("secret","test1234");
		modelAndView.setViewName(redirView);
		return modelAndView;
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView adminPage(@ModelAttribute("emailTemplates") Map<Integer,EmailTemplate> emailTemplates) {

		ModelAndView model = new ModelAndView();

		model.addObject("emailTemplates",emailTemplates);
		model.setViewName("admin");

		return model;
	}

	@RequestMapping(value = "/admin/edit_template/{idTemplate}", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView adminEditEmailTemplate(
			@PathVariable int idTemplate,
			@ModelAttribute EmailTemplate newTemplate,
			@ModelAttribute("emailTemplates") Map<Integer,EmailTemplate> emailTemplates,
			HttpServletRequest request,
			@RequestParam(required=false , value = "action") String action) {

		ModelAndView model = new ModelAndView();

		if("GET".equals(request.getMethod())) {

			EmailTemplate emailTemplate = emailTemplates.get(idTemplate);
			model.addObject("templateForm",emailTemplate);

		}
		else if("POST".equals(request.getMethod()) && action.equals("test")) {

			model.addObject("preview", previewEmailTemplate(newTemplate.getTemplateCode()));
			model.addObject("templateForm", newTemplate);
		}
		else if("POST".equals(request.getMethod()) && action.equals("save")) {
			LOGGER.info("Saving " + newTemplate.getSubject() + "...");
			emailTemplates.put(idTemplate, newTemplate);

			return new ModelAndView("redirect:" + "/admin");
		}

		model.setViewName("edit_template");
		return model;
	}


	@ModelAttribute("emailTemplates")
	public Object getTemplates() {
		return EmailTemplateFixture.loadTemplates();
	}

	private String previewEmailTemplate(String templateStr) {
		VelocityContext context = new VelocityContext();

		//Test data
		context.put("customerName", "John M. Smith");
		context.put("link", "http://44con.com");

		StringWriter swOut = new StringWriter();
		Velocity.evaluate( context, swOut, "test", templateStr);
		return swOut.getBuffer().toString();
	}
}