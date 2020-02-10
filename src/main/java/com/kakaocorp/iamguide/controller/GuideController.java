package com.kakaocorp.iamguide.controller;

import com.kakaocorp.iamguide.model.GuideDoc;
import com.kakaocorp.iamguide.model.GuideTag;
import com.kakaocorp.iamguide.service.GuideDocService;
import com.kakaocorp.iamguide.service.GuideTagService;
import com.kakaocorp.iamguide.service.GuideUpdateService;
import groovy.transform.Trait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/guide/**")
public class GuideController {


    private Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private GuideDocService guideDocService;
    @Autowired
    private GuideTagService guideTagService;
    @Autowired
    private GuideUpdateService guideUpdateService;

    @GetMapping(value = "tree", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<GuideDoc> retrieveGuideTreeList() {
        return guideDocService.retrieveGuideTreeList();
    }

    @GetMapping("menu")
    public @ResponseBody
    GuideDoc retrieveGuideDoc(HttpServletRequest req, @RequestParam("doc_key") String doc_key) {
        logger.info("clicked_menu_num:{}", doc_key);
        return guideDocService.retrieveGuideDoc(doc_key);
    }

    /*
     * 태그 검색 자동완성 데이터 가져오기
     */
    @GetMapping(value = "tag")
    public @ResponseBody
    List getTags(@RequestParam("tag") String tag) {
        return guideTagService.suggestGuideTagList(tag);
    }

    /*
     * 검색결과 출력
     */
    @GetMapping(value = "search")
    public String getSearchResults(@RequestParam("tag") String tag, Model model) {
        ArrayList<GuideDoc> result = (ArrayList<GuideDoc>) guideTagService.retrieveGuideList(tag);
        for(int i = 0; i < result.size(); i++){
            String id = result.get(i).getId();
            result.get(i).setTags(guideTagService.retrieveGuideTagList(id));
        }

        model.addAttribute("Results", result);
        model.addAttribute("test", "test");
        model.addAttribute("tag",tag);
        return "search-result";
    }

    @GetMapping("document")
    public String guideDocumentPage(@RequestParam(required = false) String doc_key, Model model) {
        if (doc_key != null) {
            model.addAttribute("selected", doc_key);
        }
        return "guide-document";
    }

    @GetMapping("get_update")
    public @ResponseBody
    String retrieveGuideUpdate(@RequestParam("documentKey") String documentKey) {
        logger.info("/guide_update{}", documentKey);
        return guideUpdateService.retrieveGuideUpdate(documentKey);
    }

    @RequestMapping(value = "getTags")
    public @ResponseBody
    List<GuideTag> retrieveTags(@RequestParam("doc_key") String doc_key) {
        return guideTagService.retrieveGuideTagList(doc_key);
    }
}

