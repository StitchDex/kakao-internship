package com.kakaocorp.iamguide.controller;

import com.kakaocorp.iamguide.model.Admin;
import com.kakaocorp.iamguide.model.GuideDoc;

import com.kakaocorp.iamguide.model.GuideTag;
import com.kakaocorp.iamguide.model.GuideUpdate;
import com.kakaocorp.iamguide.service.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/**/**")
public class AdminController {

    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;
    @Autowired
    private GuideDocService guideDocService;
    @Autowired
    private GuideUpdateService guideUpdateService;
    @Autowired
    private GuideTagService guideTagService;
    @Autowired
    private GuideDirService guideDirService;
    @Autowired
    private UploadService uploadService;


    @GetMapping("document")
    public String guideDocumentPage(@RequestParam(required = false) String doc_key, Model model) {
        if (doc_key != null) {
            model.addAttribute("selected", doc_key);
        }
        return "admin-document";
    }

    @GetMapping("admin_tree")
    public ModelAndView adminTreePage(ModelAndView model) {
        String mainKey = guideDocService.selectMain("2");
        if (mainKey == null) {
            mainKey = guideDocService.selectMain("1");
        }
        model.setViewName("admin_tree");
        model.addObject("mainDocument", mainKey);
        return model;
    }

    @PostMapping(value = "admin_tree/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public int createGuideTree(@RequestBody GuideDoc guideDoc) throws Exception {

        if (guideDoc.getType().equals("DOC")) {
            return Integer.parseInt(guideDocService.createGuideTree(guideDoc));
        } else {
            return Integer.parseInt(guideDirService.createGuideDir(guideDoc));
        }
    }

    @PostMapping(value = "admin_tree/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateGuideTree(@RequestBody List<GuideDoc> guideDocList) throws Exception {

        int listSize = guideDocList.size();

        while (listSize > 0) {
            GuideDoc guideDoc = guideDocList.get(listSize - 1);
            if (guideDoc.getType().equals("DOC")) {
                guideDocService.updateGuideTree(guideDoc);
            } else {
                guideDirService.updateGuideDir(guideDoc);
            }
            listSize--;
        }
        return "redirect:admin/admin_tree";
    }


    @PostMapping(value = "admin_tree/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteGuideTree(@RequestBody Map<String, Object> parm) throws Exception {

        String key = (String) parm.get("id");
        String type = (String) parm.get("type");

        if (type.equals("DOC")) {
            guideDocService.deleteGuideTree(key);
        } else {
            guideDirService.deleteGuideDir(key);
        }
        return "redirect:admin/admin_tree";
    }

    /**
     * AJAX : admin(AUTHENTICATED)
     * edit Guide_Doc
     */
    @PostMapping(value = "edit_doc", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    boolean updateGuideDoc(@RequestBody Map<String, Object> parm) throws Exception {

        String id = (String) parm.get("id");
        String content = (String) parm.get("content");

        guideDocService.updateGuideDoc(id, content); // guide_doc edit
        uploadService.updateImaging(id, (List) parm.get("insertUrl"), (List) parm.get("deleteUrl")); // update Imaging table

        logger.info("edit : {}", id);
        return true;
    }

    /**
     * AJAX : admin(AUTHENTICATED)
     * set Guide_Update
     */
    @PostMapping(value = "set_update", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    void createGuideUpdate(@RequestBody GuideUpdate guideUpdate) throws Exception {
        guideUpdateService.createGuideUpdate(guideUpdate); // guide_doc edit
    }

    @GetMapping("admin_auth")
    public String adminAuthPage() {
        return "admin_auth";
    }

    /**
     * AJAX : admin(AUTHENTICATED)
     * ldap search
     */
    @RequestMapping("suggest")
    public @ResponseBody
    List suggestId(HttpServletRequest req, @RequestParam String accountId) throws Exception {
        logger.debug("Query : {}", accountId);
        if (accountId != null && !accountId.isEmpty()) {
            List ret = adminService.suggest(accountId);
            return ret;
        }
        return new ArrayList<>();
    }

    /**
     * JSON : GET ADMIN LIST
     * ldap search
     */
    @RequestMapping("getadminall")
    public @ResponseBody
    List getAdminList() throws Exception {
        return adminService.getAdminList();
    }

    /**
     * AJAX : admin(AUTHENTICATED)
     * ldap search
     */
    @RequestMapping(value = "insertAdmin", method = RequestMethod.POST)
    public @ResponseBody
    void createAdmin(@RequestBody List<Admin> admins) throws Exception {
        adminService.createAdmin(admins);
    }

    @RequestMapping(value = "deleteAdmin", method = RequestMethod.POST)
    public @ResponseBody
    void deleteAdmin(@RequestBody List<Admin> admins) throws Exception {
        adminService.deleteAdmin(admins);
    }

    @GetMapping(value = "search")
    public String getSearchResults(@RequestParam("tag") String tag, Model model) {
        ArrayList<GuideDoc> documentList = (ArrayList<GuideDoc>) guideTagService.retrieveGuideList(tag);
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
        model.addAttribute("test", "test");
        model.addAttribute("tag", tag);
        return "search-result";
    }

    @RequestMapping(value = "suggestTags")
    public @ResponseBody
    List<GuideTag> suggestTags() {
        return guideTagService.suggestGuideTagList();
    }

    @RequestMapping(value = "updateTags", method = RequestMethod.POST)
    public @ResponseBody
    void updateTags(@RequestBody Object tags) {
        guideTagService.updateGuideTag(tags);
    }

}