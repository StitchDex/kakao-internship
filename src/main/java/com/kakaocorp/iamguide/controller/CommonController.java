package com.kakaocorp.iamguide.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CommonController {

    private Logger logger = LoggerFactory.getLogger(CommonController.class);

    @GetMapping("/")
    public String indexPage(Authentication auth) {
        if (auth != null && auth.isAuthenticated())
            return "redirect:/guide";
        else
            return "/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/admin")
    public String adminPage(Authentication auth) {
        logger.info("adminPage {}", auth.getName());
        return "/admin";
    }

    @GetMapping("/guide")
    public ModelAndView guideMainPage(Authentication auth,ModelAndView model) {
        model.setViewName("guide-document");
        model.addObject("selected", 23);
        logger.info("guideMainPage {}", auth.getName());
        return model;
    }
}
