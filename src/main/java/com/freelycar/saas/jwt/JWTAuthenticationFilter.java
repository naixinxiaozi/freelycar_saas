package com.freelycar.saas.jwt;

import com.freelycar.saas.jwt.bean.JSONResult;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author tangwei - Toby
 * @date 2018-12-18
 * @email toby911115@gmail.com
 */
public class JWTAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            Authentication authentication = TokenAuthenticationUtil.getAuthentication((HttpServletRequest) request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException eje) {
            logger.error("JWT过期异常捕获，返回前端提示信息");
            logger.error(eje.getMessage(), eje);
            response.setContentType("application/json");
            response.getOutputStream().print(JSONResult.fillResultString(-1, "JWT Expired", null));
        } catch (JwtException je) {
            logger.error("JWT其他异常捕获，返回前端提示信息");
            logger.error(je.getMessage(), je);
            response.setContentType("application/json");
            response.getOutputStream().print(JSONResult.fillResultString(-1, "JWT Error", null));
        }
    }
}