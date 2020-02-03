package com.kakaocorp.iamguide.controller;


import com.kakaocorp.iamguide.service.UploadService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;

@Controller
public class UploadController {

    private Logger logger = LoggerFactory.getLogger(CommonController.class);
    private static final String IMAGE_DEFAULT_PATH = "/iam_user_guide/guide/**";

    @Autowired
    private UploadService uploadService;

    @RequestMapping(value = "admin/imageupload", method = RequestMethod.POST)
    public void ImageUpload(Authentication auth, HttpServletResponse response, @RequestParam("upload") MultipartFile upload) throws IOException, JSONException {
        JSONObject json = uploadService.createImage(upload, auth);

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(json);
        printWriter.flush();
        logger.info("{},{}", "imageUpload", upload.getOriginalFilename());

    }

    @RequestMapping(value = IMAGE_DEFAULT_PATH, method = RequestMethod.GET)
    public @ResponseBody
    byte[] ImageDownload(HttpServletRequest request) throws IOException {
        byte[] imageData = null;

        imageData = uploadService.retrieveImage(request.getRequestURI());

        logger.info("{},{}", "imageUpload", request.getRequestURI());
        return imageData;
    }
}
