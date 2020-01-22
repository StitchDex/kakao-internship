package com.example.iam_guide_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

    @RequestMapping(value = "/imageupload", method = RequestMethod.POST)
    public void ImageUpload(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile upload)throws Exception{
        // 한글깨짐을 방지하기위해 문자셋 설정
        response.setCharacterEncoding("utf-8");

        // 마찬가지로 파라미터로 전달되는 response 객체의 한글 설정
        response.setContentType("text/html; charset=utf-8");

        // 업로드한 파일 이름
        String fileName = upload.getOriginalFilename();

        // 파일을 바이트 배열로 변환
        byte[] bytes = upload.getBytes();

        // 이미지를 업로드할 디렉토리(배포 디렉토리로 설정)
        String uploadPath = "file:////Users/";


        OutputStream out = new FileOutputStream(new File(uploadPath + fileName));

        // 서버로 업로드
        // write메소드의 매개값으로 파일의 총 바이트를 매개값으로 준다.
        // 지정된 바이트를 출력 스트립에 쓴다 (출력하기 위해서)
        out.write(bytes);

        // 클라이언트에 결과 표시
        String callback = request.getParameter("CKEditorFuncNum");

        // 서버=>클라이언트로 텍스트 전송(자바스크립트 실행)
        PrintWriter printWriter = response.getWriter();
        String fileUrl = request.getContextPath() + "/images/" + fileName;
        printWriter.println("<script>window.parent.CKEDITOR.tools.callFunction(" + callback + ",'" + fileUrl
                + "','이미지가 업로드되었습니다.')" + "</script>");
        printWriter.flush();
    }

}
