package com.mazaiting.config;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WelcomeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(WelcomeController.class);
	private GlobalProperties global;
	private AppProperties app;
	
	@Autowired
	public void setApp(AppProperties app) {
		this.app = app;
	}
	
	@Autowired
	public void setGlobalProperties(GlobalProperties global) {
		this.global = global;
	}
	
	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		String globalProperties = global.toString();
		String appProperties = app.toString();
		
		LOGGER.debug("Welcome {}, {}", app, global);
		
		model.put("message", appProperties + globalProperties);
		return "welcome";
	}
	
}
