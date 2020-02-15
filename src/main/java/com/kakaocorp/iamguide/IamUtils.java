package com.kakaocorp.iamguide;

import org.springframework.security.core.Authentication;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class IamUtils {
    public static String getTenthPath(String serviceId, String originalName, Authentication auth){
        Date now = new Date();
        return String.format("/%s/%s/%s/%s_%d",
                serviceId,"guide",auth.getName(),originalName.replaceAll("\\.", "_"),now.getTime());
    }
    public static String wrapImagePath(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
    public static String timeConverting(Timestamp timestamp){
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분 ss초");
        return df.format(date);
    }
    public static String keyParsing(String key){
        return key.substring(3);
    }
    public static boolean isEmpty(String test) {
        return test == null || test.trim().equalsIgnoreCase("");
    }
}
