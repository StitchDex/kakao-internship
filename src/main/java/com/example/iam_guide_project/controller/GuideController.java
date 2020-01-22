package com.example.iam_guide_project.controller;

import com.example.iam_guide_project.model.Guide_Doc;
import com.example.iam_guide_project.model.Tag;
import com.example.iam_guide_project.service.GuideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/guide/**")
public class GuideController {


    private Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    GuideService guideService;

    @GetMapping(value = "tree", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Guide_Doc> getGuideTree(){
        logger.info("/guide/tree {}");
        return guideService.getGuideTreeList();
    }

    @GetMapping("menu")
    public @ResponseBody String readGuide_Doc(HttpServletRequest req, @RequestParam("doc_key") String doc_key){
        logger.info("{}", doc_key);
        return guideService.readGuide_Doc(doc_key);
    }

    @GetMapping("tag")
    public @ResponseBody
    List<Tag> search_Doc_tag(@RequestParam String tag) {
        return guideService.getTagInfo(tag);
    }
}