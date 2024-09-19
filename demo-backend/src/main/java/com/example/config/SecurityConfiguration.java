package com.example.config;

import com.example.entity.RestBean;
import com.example.entity.dto.auth.Account;
import com.example.entity.vo.response.AuthorizeVO;
import com.example.filter.JwtAuthorizeFilter;
import com.example.service.AuthorizeService;
import com.example.util.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Resource
    JwtUtils utils;

    @Resource
    JwtAuthorizeFilter filter;

    @Resource
    AuthorizeService authorizeService;

    @Resource
    DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           PersistentTokenRepository repository) throws Exception {
        return http
                .authorizeHttpRequests(conf -> {
                    conf.requestMatchers("/api/auth/**").permitAll();
                    conf.anyRequest().authenticated();
                })
                .formLogin(conf -> {
                    conf.loginProcessingUrl("/api/auth/login");
                    conf.successHandler(this::handleProcess);
                    conf.failureHandler(this::handleProcess);
                })
                .logout(conf -> {
                    conf.logoutUrl("/api/auth/logout");
                    conf.logoutSuccessHandler(this::onLogoutSuccess);
                })
                .cors(conf -> {
                    CorsConfiguration cors = new CorsConfiguration();
                    //添加前端站点地址
                    cors.addAllowedOrigin("http://localhost:5173");
                    cors.setAllowCredentials(true);
                    cors.addAllowedHeader("*");
                    cors.addAllowedMethod("*");
                    cors.addExposedHeader("*");
                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", cors);  //直接针对于所有地址生效
                    conf.configurationSource(source);
                })
                .rememberMe(conf -> {
                    conf.rememberMeParameter("remember");
                    conf.tokenRepository(repository);
                    conf.tokenValiditySeconds(3600 * 24 * 7);
                })
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(conf -> conf
                        .accessDeniedHandler(this::handleProcess)
                        .authenticationEntryPoint(this::handleProcess)
                )
                .build();
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(authorizeService);
        return new ProviderManager(daoAuthenticationProvider);
    }

    /**
     * 多种类型的Handler整合到一起：
     * 1. 登录成功
     * 2. 登录失败
     * 3. 权限不足/未登录拦截
     * @param request 请求
     * @param response 响应
     * @param exceptionOrAuthentication 异常或者认证信息
     * @throws IOException 可能的异常
     */
    public void handleProcess(HttpServletRequest request,
                              HttpServletResponse response,
                              Object exceptionOrAuthentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        if(exceptionOrAuthentication instanceof AccessDeniedException exception) {
            writer.write(RestBean.failure(403, exception.getMessage()).asJsonString());
        } else if(exceptionOrAuthentication instanceof Exception exception) {
            writer.write(RestBean.failure(401, exception.getMessage()).asJsonString());
        } else if (exceptionOrAuthentication instanceof Authentication authentication) {
            User user = (User) authentication.getPrincipal();
            Account account = authorizeService.findAccountByNameOrEmail(user.getUsername());
            String token = utils.createJwt(user, account.getUid(), account.getUsername());
            if (token == null) {
                writer.write(RestBean.failure(403, "登录验证频繁，请稍后再试！").asJsonString());
            } else {
                AuthorizeVO vo = account.asViewObject(AuthorizeVO.class, v -> {
                    v.setExpire(utils.expireTime());
                    v.setToken(token);
                });
                writer.write(RestBean.success(vo).asJsonString());
            }
        }
    }

    /**
     * 退出登录处理，并将对应的Jwt令牌列入黑名单
     * @param request 请求
     * @param response 响应
     * @param authentication 认证信息
     * @throws IOException 可能的异常
     */
    private void onLogoutSuccess(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("Authorization");
        if(utils.invalidateJwt(authorization)) {
            writer.write(RestBean.success("退出登录成功").asJsonString());
            return;
        }
        writer.write(RestBean.failure(400, "退出登录失败").asJsonString());
    }

}
