package com.simulation.shop.config;

import com.simulation.shop.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        boolean isPreHandle = true;

        List<String> authEndpoints = new ArrayList<String>(Arrays.asList("/analytics", "/machine", "/orders", "/process", "/stock", "/transactions"));
        String auth = request.getHeader("Authorization");
        try {

            boolean isAuthorizedEndpoint = authEndpoints.stream().anyMatch(e -> request.getRequestURI().startsWith(e));
            if (isAuthorizedEndpoint && !request.getMethod().equals("OPTIONS")) {
                Claims claims = jwtUtils.verify(auth);
            }
        } catch (Exception e) {
            isPreHandle = false;
            throw new IllegalAccessException("Invalid token for " + request.getRequestURI() + " - " + e.getLocalizedMessage());
        }
        return isPreHandle;
    }
}
