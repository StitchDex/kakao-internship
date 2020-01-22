package com.example.iam_guide_project.controller;

import com.example.iam_guide_project.dao.CommonDAO;
import org.apache.ibatis.annotations.Param;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/**")
public class AdminController {

    private Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    CommonDAO common;

    /**
     * PAGE: admin (AUTHENTICATED)
     * @return
     */

    @GetMapping("admin_auth")
    public String admin_auth(){
        return "admin_auth";
    }

    @GetMapping("edit")
    public String admin_edit(){
        return "admin_edit";
    }

    @RequestMapping("suggest")
    public @ResponseBody
    List suggest(HttpServletRequest req, @RequestParam String userid) throws Exception {
        logger.debug("Query : {}", userid);
        if(userid != null && !userid.isEmpty()) {
            List ret = common.suggest(userid);
            return ret;
        }
        return new ArrayList<>();
    }
/*
    @GetMapping("test")
    public String test(){
        return "TEST";
    }*/
}
