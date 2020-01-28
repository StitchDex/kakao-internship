package com.example.iam_guide_project.controller;

import com.example.iam_guide_project.model.Guide_Doc;
import com.example.iam_guide_project.model.Guide_Tag;
import com.example.iam_guide_project.service.Guide_DocService;
import com.example.iam_guide_project.service.Guide_TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/guide/**")
public class GuideController {


    private Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    Guide_DocService guideDocService;
    @Autowired
    Guide_TagService guideTagService;

    @GetMapping(value = "tree", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Guide_Doc> getGuideTree(){
        logger.info("/guide/tree {}");
        return guideDocService.getGuideTreeList();
    }

    @GetMapping("menu")
    public @ResponseBody String setGuide_Doc(HttpServletRequest req, @RequestParam("doc_key") String doc_key){
        logger.info("{}", doc_key);
        return guideDocService.setGuide_Doc(doc_key);
    }



}

