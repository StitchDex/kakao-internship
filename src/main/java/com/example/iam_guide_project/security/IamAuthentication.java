package com.example.iam_guide_project.security;

import com.example.iam_guide_project.model.User;
import com.example.iam_guide_project.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Component
public class IamAuthentication implements AuthenticationProvider {
    private String ROLE = "ROLE_";

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    private CommonService commonService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String)authentication.getCredentials();
        try{
            // Hello MIS client
            //HelloIdentityServiceClient client = HelloIdentityServiceClient.getHelloIdentityServiceClient();
            //PersonInfo userInfo = new PersonInfo(client.getMemberById(username));
//            if(userInfo != null){ // TODO dev code
            //if(client.authenticationId(username, password, req.getRemoteAddr())
            //      && !userInfo.getIdentityDisplayName().endsWith("/업무용")){

            List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
            User user = new User();
            user.setUser_id(username);

            String isAdmin = commonService.isAdmin(username);
            if(isAdmin != null) {
                user.setIsadmin(true);
                roles.add(new SimpleGrantedAuthority(ROLE + "ADMIN"));
            }
            else
                roles.add(new SimpleGrantedAuthority(ROLE + "USER"));
            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(username, password, roles);
            result.setDetails(user);
            System.out.println(username +"\n"+ password+"\n"+roles+"\n"+authentication.isAuthenticated());
            return result;
        }
        catch(BadCredentialsException e){
            System.out.println("BadCredentials");
            throw e;
        }
        catch(Exception e){
            e.printStackTrace();
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
