package com.kakaocorp.iamguide.service;


import com.kakaocorp.iamguide.IamUtils;
import com.kakaocorp.iamguide.dao.UploadMapper;
import com.kakaocorp.iamguide.model.Image;
import net.daum.tenth2.Tenth2File;
import net.daum.tenth2.Tenth2OutputStream;
import net.daum.tenth2.util.Tenth2Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;


@Service
public class UploadService {
    @Autowired
    private TransactionTemplate txTemplate;

    @Value("${tenth.serviece.name}")
    private String serviceId;

    @Value("${tenth.host}")
    private String host;

    @Value("${tenth.key.read}")
    private String readKey;

    @Value("${tenth.key.write}")
    private String writeKey;

    @Autowired
    private UploadMapper uploadMapper;



    //need Transactions
    public JSONObject createImage(MultipartFile upload) throws IOException, JSONException {

        JSONObject json = new JSONObject();
        Tenth2OutputStream os = null;

        long imageSize = upload.getSize();
        String imageName = upload.getOriginalFilename();
        String imagePath = IamUtils.getTenthPath(serviceId,imageName);

        try {
            //tenth upload
            os = new Tenth2OutputStream(imagePath, upload.getBytes().length);
            os.write(upload.getBytes());
            os.flush();
        }
        catch(FileAlreadyExistsException e) {
            throw e;
        } catch(IOException e) {
            throw e;
        } finally {
            if(os != null) try { os.close(); } catch (IOException e) {}
        }

        json.put("uploaded", 1);
        json.put("fileName", imageName);
        json.put("url", imagePath);
        uploadMapper.createImage(imagePath); // Image table Only
        return json;
    }

    public byte[] retrieveImage(String imagePath) throws IOException {
        byte[] imageData = null;
        String uploadPath = URLDecoder.decode(imagePath,"UTF-8");

        imageData = Tenth2Util.get(uploadPath);

        return imageData;
    }

    private boolean deleteImage(String path) throws IOException {
        Tenth2File file = new Tenth2File(path);
        if(file.exists() && file.isFile())
            file.delete();
        return !file.exists();
    }

    public void createImaging(String id, String content, List<String> img_url) {
        String doc_key = id;
    }

    public void updateImageUrl(Object urls) throws IOException {
        HashMap hashMap = (HashMap) urls;
        String docId = (String) hashMap.get("docId");
        ArrayList<Image> insert = new ArrayList<>();
        ArrayList<Image> delete = new ArrayList<>();

        for(String s : (ArrayList<String>) hashMap.get("insertUrl")){
            insert.add(new Image(s, docId));
        }
        for(String s : (ArrayList<String>) hashMap.get("deleteUrl")){
            delete.add(new Image(s, docId));
        }

        if(!insert.isEmpty()){
            uploadMapper.insertImaging(insert, docId); //새로추가된 이미지와 문서 연결 : 이미징 테이블에 추가
        }

        if(!delete.isEmpty()){
            uploadMapper.deleteImaging(delete, docId); //이미징 테이블에서 연결관계 해제
        }

        List<Image> trashList = uploadMapper.findTrash();
        int pn = 0;
        while(pn != trashList.size())
        {
            if (!deleteImage(trashList.get(pn).getPath())) {//DELETE FAIL
                trashList.remove(pn);
            }
            else { //DELETE SUCCESS
                pn++;
            }
        }

        if(!trashList.isEmpty()){
            uploadMapper.deleteTrash(trashList); //이미징 테이블과 연결관계가 없는 이미지 데이터 모두 삭제
        }
    }
}
