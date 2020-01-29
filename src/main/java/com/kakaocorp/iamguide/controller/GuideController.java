package com.kakaocorp.iamguide.controller;

import com.kakaocorp.iamguide.model.GuideDoc;
import com.kakaocorp.iamguide.service.GuideDocService;
import com.kakaocorp.iamguide.service.GuideTagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/guide/**")
public class GuideController {


    private Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    GuideDocService guideDocService;
    @Autowired
    GuideTagService guideTagService;

    @GetMapping(value = "tree", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<GuideDoc> getGuideTree(){
        logger.info("/guide/tree {}");
        return guideDocService.getGuideTreeList();
    }

    @GetMapping("menu")
    public @ResponseBody String setGuide_Doc(HttpServletRequest req, @RequestParam("doc_key") String doc_key){
        logger.info("{}", doc_key);
        return guideDocService.setGuide_Doc(doc_key);
    }

    @GetMapping(value = "tag")
    public @ResponseBody
    List GetTags(@RequestParam("tag") String tag) {
        return guideTagService.suggestTags(tag);
    }

    @GetMapping(value = "search")
    public String GetSearchResults(@RequestParam("tag") String tag, Model model) {

        model.addAttribute("Results", guideTagService.getGuideList(tag));
        return "guide";
    }
}

