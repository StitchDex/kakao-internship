package com.kakaocorp.iamguide.controller;

import com.kakaocorp.iamguide.model.Guide_Update;
import com.kakaocorp.iamguide.service.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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
    CommonService common;
    @Autowired
    Guide_DocService guide_docService;
    @Autowired
    Guide_UpdateService guide_updateService;
    @Autowired
    Guide_TagService guide_tagService;
    @Autowired
    Guide_DirService guide_dirService;
    /**
     * PAGE: admin (AUTHENTICATED)
     * @return
     */

    @GetMapping("admin_auth")
    public String admin_authPage(){
        return "admin_auth";
    }

    @GetMapping("admin_tree")
    public String admin_treePage(){
        return "admin_tree";
    }


    @PostMapping(value = "admin_tree/create", produces = MediaType.APPLICATION_JSON_VALUE )
    public String createGuide_node(HttpServletRequest req, @RequestBody Map<String,Object> parm) throws Exception{
        String parent = (String) parm.get("parent");
        String text = (String) parm.get("text");
        String type = (String) parm.get("type");
        boolean state = (boolean) parm.get("state");
        if(type.equals("DOC"))
        guide_docService.createGuide_Doc(parent,text,state);
        else{
            guide_dirService.createGuide_Dir(parent,text,state);
        }
        return "redirect:admin/admin_tree";
    }

    @PostMapping(value = "admin_tree/update", produces = MediaType.APPLICATION_JSON_VALUE )
    public String updateGuide_node(HttpServletRequest req, @RequestBody Map<String,Object> parm) throws Exception{
        String key = (String)parm.get("id");
        String parent = (String) parm.get("parent");
        String text = (String) parm.get("text");
        boolean state = (boolean) parm.get("state");
        String type = (String) parm.get("type");
        if(type.equals("DOC"))
            guide_docService.updateGuide_Doc(key,parent,text,state);
        else{
            guide_dirService.updateGuide_Dir(key,parent,text,state);
        }
        return "redirect:admin/admin_tree";
    }
    @PostMapping(value = "admin_tree/delete", produces = MediaType.APPLICATION_JSON_VALUE )
    public String deleteGuide_node(HttpServletRequest req, @RequestBody Map<String,Object> parm) throws Exception{
        String key = (String) parm.get("id");
        String type = (String) parm.get("type");
        if(type.equals("DOC"))
            guide_docService.deleteGuide_Doc(key);
        else{
            guide_dirService.deleteGuide_Dir(key);
        }
        return "redirect:admin/admin_tree";
    }
    /**
     * AJAX : admin(AUTHENTICATED)
     * edit Guide_Doc
     */
    @PostMapping(value = "edit_doc", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    void editGuide_Doc(HttpServletRequest req, @RequestBody Map<String,Object> parm) throws Exception{
        String id = (String) parm.get("id");
        String content = (String) parm.get("content");
        logger.info("edit : {}",content);
        guide_docService.editGuide_Doc(id,content); // guide_doc edit
    }
    /**
     * AJAX : admin(AUTHENTICATED)
     * set Guide_Update
     */
    @PostMapping(value="set_update", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    void setGuide_Update(HttpServletRequest req, @RequestBody Map<String,Object> parm) throws Exception{
        String admin = (String) parm.get("admin");
        String title = (String) parm.get("title");
        String CRUD = (String) parm.get("CRUD");
        logger.info("edit : {}",title);
        guide_updateService.setGuide_Update(admin,title,CRUD); // guide_doc edit
    }

    /**
     * AJAX : admin(AUTHENTICATED)
     * get Guide_Update
     */
    @GetMapping("get_update")
    public @ResponseBody
    List<Guide_Update> getGuide_Update(HttpServletRequest req, @RequestParam("title") String title){
        logger.info("/guide_update{}",title);
        return guide_updateService.getGuide_Update(title);
    }

    /**
     * AJAX : admin(AUTHENTICATED)
     * ldap search
     */
    @RequestMapping("suggest")
    public @ResponseBody
    List suggest(HttpServletRequest req, @RequestParam String userid) throws Exception {
        logger.debug("Query : {}", userid);
        if(userid != null && !userid.isEmpty()) {
            List ret = common.suggest(userid);
            return ret;
        }
        return new ArrayList<>();
    } /**
     * JSON : GET ADMIN LIST
     * ldap search
     */
    @RequestMapping("getadminall")
    public @ResponseBody
    List getAdminAll() throws Exception {
        return common.getAdminAll();
    }
    /**
     * AJAX : admin(AUTHENTICATED)
     * ldap search
     */
    @RequestMapping(value = "insertAdmin", method = RequestMethod.POST)
    public @ResponseBody void insertAdmin(@RequestBody List<Object> admins) throws Exception {
        common.insertAdmin(admins);
    }

    @RequestMapping(value = "deleteAdmin", method = RequestMethod.POST)
    public @ResponseBody void deleteAdmin(@RequestBody List<Object> admins) throws Exception {
        common.deleteAdmin(admins);
    }

    @RequestMapping(value = "getTags")
    public @ResponseBody List getTags(@RequestParam("doc_key") String doc_key) {
        return guide_tagService.getTags(doc_key);
    }

    @RequestMapping(value = "suggestTags")
    public @ResponseBody List suggestTags(@RequestParam("tag") String tag){
        return guide_tagService.suggestTags(tag);
    }

    @RequestMapping(value = "updateTags", method = RequestMethod.POST)
    public @ResponseBody void updateTags(@RequestBody Object tags){
        guide_tagService.updateTags(tags);
    }



}
