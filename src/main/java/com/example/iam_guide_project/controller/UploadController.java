package com.example.iam_guide_project.controller;



import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;


@Controller
public class UploadController {

    @RequestMapping(value = "admin/imageupload", method = RequestMethod.POST)
    public void ImageUpload(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile upload)throws Exception{
        // 한글깨짐을 방지하기위해 문자셋 설정
        response.setCharacterEncoding("utf-8");

        // 마찬가지로 파라미터로 전달되는 response 객체의 한글 설정
        response.setContentType("text/html; charset=utf-8");

        String fileName = upload.getOriginalFilename();

        byte[] bytes = upload.getBytes();


        String uploadPath = "/Users/kakao/Desktop/upload/";

        OutputStream out = new FileOutputStream(new File(uploadPath + fileName));

        // 서버로 업로드
        // write메소드의 매개값으로 파일의 총 바이트를 매개값으로 준다.
        // 지정된 바이트를 출력 스트립에 쓴다 (출력하기 위해서)
        out.write(bytes);

        PrintWriter printWriter = response.getWriter();
        String fileUrl = request.getContextPath() +"/Users/kakao/Desktop/upload/"+ fileName;
        JSONObject json = new JSONObject();
        json.put("uploaded", 1);
        json.put("fileName", fileName);
        json.put("url", fileUrl);
        printWriter.println(json);
        printWriter.flush();
    }

}
