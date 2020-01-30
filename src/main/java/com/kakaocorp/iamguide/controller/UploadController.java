package com.kakaocorp.iamguide.controller;


import com.kakaocorp.iamguide.service.UploadService;
import net.daum.tenth2.Tenth2InputStream;
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
    }

    //Get image (need edit mapping value + transaction)
    @RequestMapping(value="/iam_user_guide/guide/2020_01/{filename}",method = RequestMethod.GET)
    public @ResponseBody byte[]
    ImageDownload(HttpServletResponse response,HttpServletRequest request,@PathVariable("filename") String filename) throws IOException, JSONException {
        Tenth2InputStream is = null;
        String uploadPath = URLDecoder.decode(request.getRequestURI(),"UTF-8");
        byte[] data = null;
        is = new Tenth2InputStream(uploadPath);
        try {
            is = new Tenth2InputStream(uploadPath);
            // 현재 위치에서 남은 데이터 길이를 가져옵니다.
            long remains = is.remains();



            data = new byte[(int)remains];

            // readFully는 data.length만큼 읽습니다.
            // 반면 read는 최대 data.length만큼 읽으려고 시도하지만 모두 읽지 못할 수 있습니다. 주의하세요!
            is.readFully(data);

        } catch(FileNotFoundException e) {
            throw e;
        } catch(IOException e) {
            throw e;
        } finally {
            if(is != null) try { is.close(); } catch (IOException e) {}
        }
        return data;
    }

}
