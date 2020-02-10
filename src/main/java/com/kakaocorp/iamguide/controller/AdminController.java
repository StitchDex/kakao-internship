package com.kakaocorp.iamguide.controller;

import com.kakaocorp.iamguide.model.GuideDoc;
<<<<<<< HEAD
=======
import com.kakaocorp.iamguide.model.GuideUpdate;
>>>>>>> 75b638c9af5656ffcc2a3e6e9138e2bad160bee6
import com.kakaocorp.iamguide.service.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/**/**")
public class AdminController {

    private Logger logger = LoggerFactory.getLogger(CommonController.class);

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
    public String admin_treePage() {
        return "admin_tree";
    }

    @PostMapping(value = "admin_tree/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public String createGuideTree(HttpServletRequest req, @RequestBody Map<String, Object> parm) throws Exception {
        String parent = (String) parm.get("parent");
        String text = (String) parm.get("text");
        String type = (String) parm.get("type");
        boolean state = (boolean) parm.get("state");

        if (type.equals("DOC")) {
            guideDocService.createGuideTree(parent, text, state);
        } else {
            guideDirService.createGuideDir(parent, text, state);
        }
        return "redirect:admin/admin_tree";
    }

    @PostMapping(value = "admin_tree/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateGuideTree(HttpServletRequest req, @RequestBody Map<String, Object> parm) throws Exception {
        String key = (String) parm.get("id");
        String parent = (String) parm.get("parent");
        String text = (String) parm.get("text");
        boolean state = (boolean) parm.get("state");
        String type = (String) parm.get("type");

        if (type.equals("DOC")) {
            guideDocService.updateGuideTree(key, parent, text, state);
        } else {
            guideDirService.updateGuideDir(key, parent, text, state);
        }
        return "redirect:admin/admin_tree";
    }

    @PostMapping(value = "admin_tree/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteGuideTree(HttpServletRequest req, @RequestBody Map<String, Object> parm) throws Exception {
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
    boolean updateGuideDoc(HttpServletRequest req, @RequestBody Map<String, Object> parm) throws Exception {
        String id = (String) parm.get("id");
        String content = (String) parm.get("content");

        guideDocService.updateGuideDoc(id, content); // guide_doc edit=
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
    void createGuideUpdate(HttpServletRequest req, @RequestBody Map<String, Object> parm) throws Exception {

        String admin = (String) parm.get("admin");
        String documentKey = (String) parm.get("documentKey");
        String title = (String) parm.get("title");
        String CRUD = (String) parm.get("CRUD");

        guideUpdateService.createGuideUpdate(admin, documentKey, title, CRUD); // guide_doc edit
    }

    @GetMapping("admin_auth")
    public String admin_authPage() {
        return "admin_auth";
    }

    /**
     * AJAX : admin(AUTHENTICATED)
     * ldap search
     */
    @RequestMapping("suggest")
    public @ResponseBody
    List suggest(HttpServletRequest req, @RequestParam String accountId) throws Exception {
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
    void createAdmin(@RequestBody List<Object> admins) throws Exception {
        adminService.createAdmin(admins);
    }

    @RequestMapping(value = "deleteAdmin", method = RequestMethod.POST)
    public @ResponseBody
    void deleteAdmin(@RequestBody List<Object> admins) throws Exception {
        adminService.deleteAdmin(admins);
    }

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

    @RequestMapping(value = "suggestTags")
    public @ResponseBody
    List suggestTags(@RequestParam("tag") String tag) {
        return guideTagService.suggestGuideTagList(tag);
    }

    @RequestMapping(value = "updateTags", method = RequestMethod.POST)
    public @ResponseBody
    void updateTags(@RequestBody Object tags) {
        guideTagService.updateGuideTag(tags);
    }


<<<<<<< HEAD
=======
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
>>>>>>> 75b638c9af5656ffcc2a3e6e9138e2bad160bee6
}