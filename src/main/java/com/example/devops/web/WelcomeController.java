package com.example.devops.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WelcomeController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Environment environment;

    WelcomeController(Environment environment) {
        this.environment = environment;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public String welcome(Model model, HttpServletRequest request) {
        logger.info("Processing index request");
        model.addAttribute("course", "DevOps");

        String environmentNames = String.join(", ", environment.getActiveProfiles());
        environmentNames = environmentNames.length() == 0 ? "local" : environmentNames;
        model.addAttribute("environment", environmentNames);
        return "index";
    }
}