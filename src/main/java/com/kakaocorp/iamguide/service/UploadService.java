package com.kakaocorp.iamguide.service;

import com.kakaocorp.iamguide.IamUtils;
import com.kakaocorp.iamguide.dao.UploadMapper;
<<<<<<< HEAD
=======
import net.daum.tenth2.Tenth2OutputStream;
>>>>>>> a97c529ceadb3c8fc218b1454ee8237ea26102ac
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.util.HashMap;


@Service
public class UploadService {
    @Autowired
    private TransactionTemplate txTemplate;

    @Value("${tenth.serviece.name}")
    private String serviceId;

    @Value("${tenth.twg.host}")
    private String host;

    @Value("${tenth.key.read}")
    private String readKey;

    @Value("${tenth.key.write}")
    private String writeKey;

    @Autowired
    UploadMapper uploadMapper;

    public HashMap<String,String> setImage(MultipartFile upload) throws IOException {
        /*serviceId=$1                  # 서비스ID
                wirte_key=$2            # Write key(서비스ID 등록시 발급받은 write key)
        tenth2_full_pathname=$3         # 업로드할 Tenth2 타겟경로 (파일이 업로드될 전체 경로를 명시합니다.
                                        e.g. "/test/diego/test_post_upload_image_resize_no_original_20160121045425")
        full_pathname_to_upload=$4      # 업로드할 로컬파일 경로*/
        /*Tenth2OutputStream os = null;
        String path = IamUtils.getTenthPath(serviceId, upload.getOriginalFilename());
        //tenth
        os = new Tenth2OutputStream(path, upload.getBytes().length);
        os.write(upload.getBytes());*/
        HashMap<String,String> info = null;
        String fileName = upload.getOriginalFilename();
        byte[] bytes = upload.getBytes();
        String uploadPath = "/Users/kakao/Desktop/upload/";
        OutputStream out = new FileOutputStream(new File(uploadPath + fileName));
        out.write(bytes);
        info = new HashMap<String,String>();
        info.put("uploadPath",uploadPath);
        info.put("fileName",fileName);

        uploadMapper.setImage(uploadPath);
        return info;
    }

    public String getFileUrl(String ip, String path) throws UnsupportedEncodingException, SignatureException {
        long expires = (System.currentTimeMillis() / 1000) + 300;
        String plainText = "GET\n" + expires + "\n" + ip + "\n\n\n" + path ;
        String signature = getSignature(readKey, plainText);
        String url = host + path +
                "?TWGServiceId=" + serviceId +
                "&Signature=" + signature +
                "&AllowedIp=" + ip +
                "&Expires=" +expires;
        return url;

    }
    private String getSignature(String key, String value) throws SignatureException, UnsupportedEncodingException {
        String HMAC_SHA1_ALGORITHM = "HmacSHA1";
        String result;

        try {
            SecretKeySpec signingKey =
                    new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);

            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(value.getBytes());
            result = Base64.encodeBase64String(rawHmac).trim();
        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }

        return URLEncoder.encode(result, "UTF-8");
    }
}
