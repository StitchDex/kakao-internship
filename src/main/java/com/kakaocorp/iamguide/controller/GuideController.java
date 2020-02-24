package com.kakaocorp.iamguide.controller;

import com.kakaocorp.iamguide.model.GuideDoc;
import com.kakaocorp.iamguide.model.GuideTag;
import com.kakaocorp.iamguide.service.GuideDocService;
import com.kakaocorp.iamguide.service.GuideTagService;
import com.kakaocorp.iamguide.service.GuideUpdateService;
import groovy.transform.Trait;
import org.apache.ibatis.jdbc.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/guide/**")
public class GuideController {

    private Logger logger = LoggerFactory.getLogger(GuideController.class);

    private final GuideDocService guideDocService;
    private final GuideTagService guideTagService;
    private final GuideUpdateService guideUpdateService;

    @Autowired
    public GuideController(GuideDocService guideDocService, GuideTagService guideTagService, GuideUpdateService guideUpdateService) {
        this.guideDocService = guideDocService;
        this.guideTagService = guideTagService;
        this.guideUpdateService = guideUpdateService;
    }

    @GetMapping(value = "tree", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<GuideDoc> retrieveGuideTreeList() {
        return guideDocService.retrieveGuideTreeList();
    }

    @GetMapping("document")
    public String guideDocumentPage(HttpServletResponse res, @RequestParam("doc_key") String docKey, Model model) throws IOException{

        GuideDoc guideDoc = guideDocService.retrieveGuideDoc(docKey);

        if (guideDoc == null || guideDoc.getState() == 0) {
            res.sendError(404);
            return "guide-document";
        }
        model.addAttribute("selected", docKey);
        model.addAttribute("guideTitle", guideDoc.getText());
        model.addAttribute("guideContent", guideDoc.getContent());
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
    List<GuideTag> retrieveTags(@RequestParam("doc_key") String docKey) {
        return guideTagService.retrieveGuideTagList(docKey);
    }

    /*
     * 태그 검색 자동완성 데이터 가져오기
     */
    @GetMapping(value = "tag")
    public @ResponseBody
    List<GuideTag> retrieveSuggestTagList() {
        return guideTagService.suggestGuideTagList("guide");
    }

    /*
     * 검색결과 출력
     */
    @GetMapping(value = "search")
    public String getSearchResults(@RequestParam("tag") String tag, Model model) {
        ArrayList<GuideDoc> documentList = (ArrayList<GuideDoc>) guideTagService.retrieveGuideList(tag, "guide");
        for (int i = 0; i < documentList.size(); i++) {
            String id = documentList.get(i).getId();
            documentList.get(i).setTags(guideTagService.retrieveGuideTagList(id));
        }

        HashMap<String, ArrayList<GuideDoc>> result = new HashMap<>();
        for (GuideDoc document : documentList) {
            if (!result.containsKey(document.getParent())) {
                ArrayList<GuideDoc> temp = new ArrayList<>();
                temp.add(document);
                result.put(document.getParent(), temp);
            } else {
                result.get(document.getParent()).add(document);
            }
        }

        model.addAttribute("Results", result);
        model.addAttribute("tag", tag);
        return "search-result";
    }
}

