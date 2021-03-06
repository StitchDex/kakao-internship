package com.kakaocorp.iamguide.service;


import com.kakaocorp.iamguide.IamUtils;
import com.kakaocorp.iamguide.controller.CommonController;
import com.kakaocorp.iamguide.dao.UploadMapper;
import com.kakaocorp.iamguide.model.Image;
import lombok.RequiredArgsConstructor;
import net.daum.tenth2.Tenth2File;
import net.daum.tenth2.Tenth2OutputStream;
import net.daum.tenth2.util.Tenth2Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;


@Service
@RequiredArgsConstructor
public class UploadService {

    @Value("${tenth.serviece.name}")
    private String serviceId;

    private Logger logger = LoggerFactory.getLogger(UploadService.class);

    private final UploadMapper uploadMapper;

    public JSONObject createImage(MultipartFile upload, Authentication auth) throws IOException, JSONException {
        final String PREFIX = "/get_image/";
        JSONObject json = new JSONObject();
        Tenth2OutputStream os = null;

        long imageSize = upload.getSize();
        String imageName = upload.getOriginalFilename();
        String imagePath = IamUtils.getTenthPath(serviceId, imageName, auth);
        String uuid = IamUtils.wrapImagePath();
        try {
            //tenth upload
            os = new Tenth2OutputStream(imagePath, upload.getBytes().length);
            os.write(upload.getBytes());
            os.flush();
        } catch (FileAlreadyExistsException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            if (os != null) try {
                os.close();
            } catch (IOException e) {
            }
        }
        json.put("uploaded", 1);
        json.put("fileName", imageName);
        json.put("url", PREFIX + uuid);
        uploadMapper.createImage(imagePath, uuid); // Image table Only
        return json;
    }

    @Cacheable(cacheNames = "imageCache", key = "#uuid")
    public byte[] retrieveImage(String uuid) throws IOException {
        byte[] imageData = null;
        String uploadPath = uploadMapper.retrieveImage(uuid);
        //String uploadPath = URLDecoder.decode(imagePath, "UTF-8");

        imageData = Tenth2Util.get(uploadPath);

        return imageData;
    }

    @CacheEvict(cacheNames = "imageCache", key = "#uuid", condition = "#uuid!=null")
    public boolean deleteImage(String path, String uuid) throws IOException {
        Tenth2File file = new Tenth2File(path);
        if (file.exists() && file.isFile())
            file.delete();
        return !file.exists();
    }

    public void updateImaging(String id, List insertUrl, List deleteUrl) throws IOException {

        if (!insertUrl.isEmpty()) {
            for (int i = 0; i < insertUrl.size(); i++) {
                String temp = insertUrl.get(i).toString().substring(11);
                logger.info("insert_img: {}", temp);
                insertUrl.set(i, temp);
            }
            uploadMapper.createImaging(insertUrl, id); //새로추가된 이미지와 문서 연결 : 이미징 테이블에 추가
        }

        if (!deleteUrl.isEmpty()) {
            for (int i = 0; i < deleteUrl.size(); i++) {
                String temp = deleteUrl.get(i).toString().substring(11);
                logger.info("delete_img: {}", temp);
                deleteUrl.set(i, temp);
            }
            uploadMapper.deleteImaging(deleteUrl, id); //이미징 테이블에서 연결관계 해제
        }

        List<Image> trashList = uploadMapper.findTrash();
        int pn = 0;
        while (pn != trashList.size()) {
            if (!deleteImage(trashList.get(pn).getPath(), trashList.get(pn).getUuid())) { //DELETE FAIL
                trashList.remove(pn);
            } else { //DELETE SUCCESS
                pn++;
            }
        }

        if (!trashList.isEmpty()) {
            uploadMapper.deleteTrash(trashList); //이미징 테이블과 연결관계가 없는 이미지 데이터 모두 삭제
        }
    }
}
