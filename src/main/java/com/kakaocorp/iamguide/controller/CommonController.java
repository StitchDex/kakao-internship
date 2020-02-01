package com.kakaocorp.iamguide.controller;

import com.kakaocorp.iamguide.service.CommonService;

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

    @Autowired
    private CommonService commonService;

    @GetMapping("/")
    public String indexPage(Authentication auth) {
        if(auth !=null && auth.isAuthenticated())
            return "redirect:/guide";
        else
            return "/login";
    }
    /**
     * PAGE: login (AUTHENTICATED)
     * @return
     */
    @GetMapping("/login")
    public ModelAndView loginPage() {
        return  new ModelAndView("login");
    }

    @GetMapping("/admin")
    public String adminPage(Authentication auth){
        logger.info("{}",auth.getDetails());
        return "/admin";
    }

    @GetMapping("/guide")
    public String guidePage(@RequestParam(required = false) String doc_key, Model model){
        if(doc_key != null) {
            model.addAttribute("selected",doc_key);
        }
        return "/guide";
    }
}
