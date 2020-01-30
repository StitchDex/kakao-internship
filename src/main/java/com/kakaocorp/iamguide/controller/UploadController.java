package com.kakaocorp.iamguide.controller;


import com.kakaocorp.iamguide.service.UploadService;

import net.daum.tenth2.util.Tenth2Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.daum.tenth2.Tenth2InputStream;
import net.daum.tenth2.util.Tenth2Util;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLDecoder;

@Controller
public class UploadController {
    private Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Value("${tenth.host}")
    private String tenthHost;

    @Autowired
    UploadService uploadService;
    //Need transactions by using response number 
    @RequestMapping(value = "admin/imageupload", method = RequestMethod.POST)
    public void
    ImageUpload(HttpServletResponse response,HttpServletRequest request, @RequestParam("upload") MultipartFile upload) throws IOException, JSONException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        String  fileUrl = uploadService.setImage(upload,request.getRemoteAddr());
        PrintWriter printWriter = response.getWriter();
        JSONObject json = new JSONObject();
        json.put("uploaded", 1);
        json.put("fileName" ,upload.getOriginalFilename());
        json.put("url",fileUrl);
        printWriter.println(json);
        printWriter.flush();
        logger.info("{}","imageupload");
    }
    //pathvariable, cache
    @RequestMapping(value="/iam_user_guide/guide/2020_01/{filename}",method = RequestMethod.GET)
    public @ResponseBody byte[]
    ImageDownload(HttpServletResponse response,HttpServletRequest request,@PathVariable("filename") String filename) throws IOException, JSONException {
        Tenth2Util util = new Tenth2Util();

        logger.info("{}","imagedownload");

        String uploadPath = URLDecoder.decode(request.getRequestURI(),"UTF-8");
        byte[] data = util.get(uploadPath);
        return data;
    }

    @RequestMapping(value = "admin/imageurl", method = RequestMethod.POST)
    public @ResponseBody void insertImageUrl(@RequestBody Object urls) {
        uploadService.updateImageUrl(urls);
    }
}
