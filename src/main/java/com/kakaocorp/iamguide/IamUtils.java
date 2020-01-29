package com.kakaocorp.iamguide;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IamUtils {
    public static String getTenthPath(String serviceId, String originalName){
        Date now = new Date();
        String dir = new SimpleDateFormat("yyyy_MM").format(now);
        return String.format("/%s/%s/%s/%s_%d",
                serviceId,"guide",dir, originalName.replaceAll("\\.", "_"), now.getTime());
    }

    public static boolean isEmpty(String test) {
        return test == null || test.trim().equalsIgnoreCase("");
    }
}
