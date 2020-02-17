package com.kakaocorp.iamguide.security;

import com.daum.mis.remote.client.HelloIdentityServiceClient;
import com.kakaocorp.iamguide.GuideDictionary;

import com.kakaocorp.iamguide.model.DevAdmin;
import com.kakaocorp.iamguide.model.UserInfo;
import com.kakaocorp.iamguide.service.AdminService;
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
    private static final String ROLE = "ROLE_";

    @Autowired
    private HttpServletRequest req;

    @Autowired
    private AdminService adminService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        /*String username = authentication.getName();
        String password = (String)authentication.getCredentials();
        if(username.equals("local") && password.equals("local")) {
            DevAdmin user = new DevAdmin();
            List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
            roles.add(new SimpleGrantedAuthority(ROLE + GuideDictionary.ADMIN));
            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(username, password, roles);
            result.setDetails(user);
            return result;
        }
        throw new BadCredentialsException("인증되지 않은 사용자 입니다.");*/
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        try {
            // Hello MIS client
            HelloIdentityServiceClient client = HelloIdentityServiceClient.getHelloIdentityServiceClient();
            UserInfo user = new UserInfo(client.getMemberById(username));
            if (client.authenticationId(username, password, req.getRemoteAddr())) {
                List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();

                if (adminService.isAdmin(username) != null) {
                    roles.add(new SimpleGrantedAuthority(ROLE + GuideDictionary.ADMIN));
                } else {
                    roles.add(new SimpleGrantedAuthority(ROLE + GuideDictionary.USER));
                }

                UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(username, password, roles);
                result.setDetails(user);
                return result;
            }
            throw new BadCredentialsException("인증되지 않은 사용자 입니다.");
        } catch (BadCredentialsException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
