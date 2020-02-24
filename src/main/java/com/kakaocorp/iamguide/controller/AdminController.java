package com.kakaocorp.iamguide.controller;

import com.daum.mis.remote.client.model.IdentityPersonInfo;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/**/**")
public class AdminController {

    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;
    private final GuideDocService guideDocService;
    private final GuideUpdateService guideUpdateService;
    private final GuideTagService guideTagService;
    private final GuideDirService guideDirService;
    private final UploadService uploadService;

    @Autowired
    public AdminController(AdminService adminService, GuideDocService guideDocService, GuideUpdateService guideUpdateService, GuideTagService guideTagService, GuideDirService guideDirService, UploadService uploadService) {
        this.adminService = adminService;
        this.guideDocService = guideDocService;
        this.guideUpdateService = guideUpdateService;
        this.guideTagService = guideTagService;
        this.guideDirService = guideDirService;
        this.uploadService = uploadService;
    }

    @GetMapping("document")
    public String guideDocumentPage(HttpServletResponse res, @RequestParam("doc_key") String docKey, Model model) throws NullPointerException, IOException {
        GuideDoc guideDoc = guideDocService.retrieveGuideDoc(docKey);
        model.addAttribute("selected", docKey);
        try {
            model.addAttribute("guideTitle", guideDoc.getText());
            model.addAttribute("guideContent", guideDoc.getContent());
        } catch (Exception e) {
            logger.info("{} not found", docKey);
            res.sendError(404);
        }
        return "admin-document";
    }

    @GetMapping("admin_tree")
    public String adminTreePage(Model model) {
        String mainKey = guideDocService.selectMain("2");
        if (mainKey == null) {
            mainKey = guideDocService.selectMain("1");
        }
        model.addAttribute("mainDocument", mainKey);
        return "admin_tree";
    }

    @GetMapping("admin_auth")
    public String adminAuthPage() {
        return "admin_auth";
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
    @ResponseBody
    public boolean updateGuideTree(@RequestBody List<GuideDoc> guideDocList) throws Exception {

        int listSize = guideDocList.size();
        try {
            while (listSize > 0) {
                GuideDoc guideDoc = guideDocList.get(listSize - 1);
                if (guideDoc.getType().equals("DOC")) {
                    guideDocService.updateGuideTree(guideDoc);
                } else {
                    guideDirService.updateGuideDir(guideDoc);
                }
                listSize--;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @PostMapping(value = "admin_tree/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean deleteGuideTree(@RequestBody Map<String, Object> objectMap) throws Exception {
        String key = (String) objectMap.get("id");
        String type = (String) objectMap.get("type");
        try {
            if (type.equals("DOC")) {
                guideDocService.deleteGuideTree(key);
            } else {
                guideDirService.deleteGuideDir(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
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


    /**
     * AJAX : admin(AUTHENTICATED)
     * ldap search
     */
    @RequestMapping("suggest")
    public @ResponseBody
    List<IdentityPersonInfo> suggestId(@RequestParam String accountId) throws Exception {
        logger.debug("Query : {}", accountId);
        if (accountId != null && !accountId.isEmpty()) {
            List<IdentityPersonInfo> ret = adminService.suggest(accountId);
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
    List<Admin> getAdminList() throws Exception {
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
        ArrayList<GuideDoc> documentList = (ArrayList<GuideDoc>) guideTagService.retrieveGuideList(tag, "admin");
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

    @RequestMapping(value = "suggestTags")
    public @ResponseBody
    List<GuideTag> retrieveSuggestTagList() {
        return guideTagService.suggestGuideTagList("admin");
    }

    @RequestMapping(value = "updateTags", method = RequestMethod.POST)
    public @ResponseBody
    void updateTags(@RequestBody Object tags) {
        guideTagService.updateGuideTag(tags);
    }

}