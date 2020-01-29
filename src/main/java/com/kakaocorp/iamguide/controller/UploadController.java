package com.kakaocorp.iamguide.controller;


import com.kakaocorp.iamguide.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;


@Controller
public class UploadController {



    @Autowired
    UploadService uploadService;

    @RequestMapping(value = "admin/imageupload", method = RequestMethod.POST)
    public void
    ImageUpload(HttpServletResponse response,HttpServletRequest request, @RequestParam("file") MultipartFile upload) throws IOException, JSONException {
        /*String tenthpath = uploadService.setImage(upload);

        String fileName = upload.getOriginalFilename();
        String fileUrl = request.getContextPath() + tenthpath + fileName;*/


        // 한글깨짐을 방지하기위해 문자셋 설정, 파라미터로 전달되는 response 객체의 한글 설정
        String fileName = upload.getOriginalFilename();
        byte[] bytes = upload.getBytes();
        String uploadPath = "/Users/kakao/Desktop/upload/";
        OutputStream out = new FileOutputStream(new File(uploadPath + fileName));
        out.write(bytes);

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        String fileUrl = request.getContextPath() +"/Users/kakao/Desktop/upload/"+ fileName;
        PrintWriter printWriter = response.getWriter();
        JSONObject json = new JSONObject();
        json.put("uploaded", 1);
        json.put("fileName", fileName);
        json.put("url", fileUrl);
        printWriter.println(json);
        printWriter.flush();
    }
}
