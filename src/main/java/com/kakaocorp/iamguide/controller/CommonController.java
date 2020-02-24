package com.kakaocorp.iamguide.controller;

import com.kakaocorp.iamguide.model.GuideDoc;
import com.kakaocorp.iamguide.service.GuideDocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CommonController {

    private Logger logger = LoggerFactory.getLogger(CommonController.class);

    private final GuideDocService guideDocService;

    @Autowired
    public CommonController(GuideDocService guideDocService) {
        this.guideDocService = guideDocService;
    }

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
    public ModelAndView adminPage(Authentication auth, ModelAndView model) {

        String docKey = guideDocService.selectMain("2");
        if (docKey == null) {
            docKey = guideDocService.selectMain("1");
        }
        model.setViewName("admin-document");

        GuideDoc guideDoc = guideDocService.retrieveGuideDoc(docKey);
        model.addObject("selected", docKey);
        model.addObject("guideTitle", guideDoc.getText());
        model.addObject("guideContent", guideDoc.getContent());
        logger.info("adminMainPage {}", auth.getName());
        return model;
    }

    @GetMapping("/guide")
    public ModelAndView guideMainPage(Authentication auth, ModelAndView model) {

        String docKey = guideDocService.selectMain("2");
        if (docKey == null) {
            docKey = guideDocService.selectMain("1");
        }
        model.setViewName("guide-document");

        GuideDoc guideDoc = guideDocService.retrieveGuideDoc(docKey);
        model.addObject("selected", docKey);
        model.addObject("guideTitle", guideDoc.getText());
        model.addObject("guideContent", guideDoc.getContent());
        logger.info("guideMainPage {}", auth.getName());
        return model;
    }


}
