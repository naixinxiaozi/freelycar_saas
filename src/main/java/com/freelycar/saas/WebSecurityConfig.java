package com.freelycar.saas;

import com.freelycar.saas.permission.configuration.CustomUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author tangwei [toby911115@gmail.com]
 * @date 2018/9/27
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    UserDetailsService customUserService() {
        return new CustomUserService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                //注册请求开放
                .antMatchers("/register/**").permitAll()
                //测试请求
                .antMatchers("/mobile/**").permitAll()
                //具有管理员权限访问的请求
                .antMatchers("/superadmin/**").hasAnyRole("SUPERADMIN", "ADMIN")
                .anyRequest().authenticated()
                .and().formLogin()
//                .loginPage("/login")
//                .successForwardUrl("/superadmin/index")
                .permitAll()
                .and()
                //开启cookie保存用户数据
                .rememberMe()
                //设置cookie有效期
                .tokenValiditySeconds(60 * 60 * 24 * 2)
                //设置cookie的私钥
//                .key("freelycar")
                .and()
                .logout()
                //默认注销行为为logout，可以通过下面的方式来修改
//                .logoutUrl("/custom-logout")
                //设置注销成功后跳转页面，默认是跳转到登录页面
//                .logoutSuccessUrl("")
                .permitAll();
    }
}
