package com.example.devops.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WelcomeController {

    @RequestMapping("/")
    public String welcome(Model model, HttpServletRequest request) {
        model.addAttribute("course", "DevOps");

        String requestURI = request.getRequestURI();
        String environmentName = getEnvironmentName(requestURI);
        model.addAttribute("environment", environmentName);
        return "index";
    }

    private String getEnvironmentName(String uri) {
        return uri.contains("devopsqa") ? "QA" : (uri.contains("devops") ? "PROD" : "DEV");
    }
}