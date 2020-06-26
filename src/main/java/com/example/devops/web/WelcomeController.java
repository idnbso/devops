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

        String uri = request.getRequestURI();
        String environmentName = uri.contains("devopsqa") ? "QA" : (uri.contains("devops") ? "PROD" : "DEV");
        model.addAttribute("environment", environmentName);
        return "index";
    }
}