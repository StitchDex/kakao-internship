package com.example.iam_guide_project.controller;

import com.example.iam_guide_project.service.CommonService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

@Controller
public class CommonController {
    private Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    CommonService commonService;


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
        logger.info("[GET]{}", "/login");
        return  new ModelAndView("login");
    }

    @GetMapping("/admin")
    public String adminPage(){
        logger.info("[Get]{}", "/admin");
        return "/admin";
    }
    @GetMapping("/guide")
    public String guidePage(){
        logger.info("[Get]{}", "/guide");
        return "/guide";
    }





}
