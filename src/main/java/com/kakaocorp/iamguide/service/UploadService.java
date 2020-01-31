package com.kakaocorp.iamguide.service;


import com.kakaocorp.iamguide.IamUtils;
import com.kakaocorp.iamguide.dao.UploadMapper;
import com.kakaocorp.iamguide.model.Image;
import net.daum.tenth2.Tenth2File;
import net.daum.tenth2.Tenth2InputStream;
import net.daum.tenth2.Tenth2OutputStream;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;
import sun.reflect.annotation.ExceptionProxy;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.FileAlreadyExistsException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;


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
    UploadMapper uploadMapper;

    public static void setImaging(String id, String content, List<String> img_url) {
        String doc_key = id;
    }


    public String setImage(MultipartFile upload, String userIp) throws IOException {

        Tenth2OutputStream os = null;
        String uploadPath = IamUtils.getTenthPath(serviceId, upload.getOriginalFilename());
        try {
            //tenth upload
            os = new Tenth2OutputStream(uploadPath, upload.getBytes().length);
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

        uploadMapper.setImage(uploadPath); // Image table Only
        return uploadPath;


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
            //uploadMapper.insertImageUrl(insert); //이미지 테이블에 새로운 이미지 추가
            uploadMapper.insertImaging(insert); //새로추가된 이미지와 문서 연결 : 이미징 테이블에 추가
        }

        if(!delete.isEmpty()){
            uploadMapper.deleteImaging(delete, docId); //이미징 테이블에서 연결관계 해제
        }

        List<Image> trashList = uploadMapper.findTrash();
        int pn = 0;
        while(pn != trashList.size())
        {
            if (!delete(trashList.get(pn).getPath())) {//DELETE FAIL
                trashList.remove(pn);
            }
            else { //DELETE SUCCESS
                pn++;
            }
        }
        uploadMapper.deleteTrash(trashList); //이미징 테이블과 연결관계가 없는 이미지 데이터 모두 삭제
    }

    private boolean delete(String path) throws IOException {
        Tenth2File file = new Tenth2File(path);
        if(file.exists() && file.isFile())
            file.delete();
        return !file.exists();
    }
}
