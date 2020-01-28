package com.kakaocorp.iamguide.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CommonAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException e) throws IOException, ServletException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            log.info("User '" + auth.getName() + "' attempted to access the protected URL: " + req.getRequestURL());
            if(req.getRequestURI().equalsIgnoreCase("/login")){
                res.sendRedirect("/guide");
                return;
            }
        } else
            log.info("request, {} {}", req.getMethod() , req.getRequestURL());
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}

