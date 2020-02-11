package com.kakaocorp.iamguide;

import org.springframework.security.core.Authentication;

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
    public static boolean isEmpty(String test) {
        return test == null || test.trim().equalsIgnoreCase("");
    }
}
