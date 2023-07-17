package com.board.whitelabel.config;

import com.board.whitelabel.service.CustomOAuth2UserService;
import com.board.whitelabel.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Log4j2
@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

   private CustomOAuth2UserService customOAuth2UserService;
   private LoginService loginService;
   @Bean
   PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }


   @Bean
   @Override
   public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
   }

   @Override
   public void configure(WebSecurity web) throws Exception {
           StrictHttpFirewall firewall = new StrictHttpFirewall();
           firewall.setAllowUrlEncodedDoubleSlash(true);
           web.httpFirewall(firewall);
       }

   @Override
   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(loginService).passwordEncoder(passwordEncoder());
   }

   @Override
   protected void configure(HttpSecurity http) throws Exception {


      http.csrf().disable()
           .authorizeRequests()
         .antMatchers("/whitelabel/").permitAll()
         .antMatchers("/whitelabel/myPage","/whitelabel/register").hasRole("ADMIN")
            .regexMatchers("^.*(?<!/)/{2}.*$").permitAll()


            .and().formLogin()
            .loginPage("/whitelabel/loginPage")
            .loginProcessingUrl("/whitelabel/loginPage")
            .usernameParameter("loginId")
            .passwordParameter("password")
            .defaultSuccessUrl("/")
                  .permitAll()

              .and()
              .oauth2Login()
              .defaultSuccessUrl("/")
              .userInfoEndpoint()
              .userService(customOAuth2UserService)
              .and()


              .and().logout()
            .logoutUrl("/whitelabel/logout")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")




            .and().
         headers().frameOptions().sameOrigin()

            .and().requiresChannel()
            .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
            .requiresSecure()



            .and()
            .sessionManagement()
            .maximumSessions(1) //세션 최대 허용 수
            .maxSessionsPreventsLogin(false);
      
      


   }


}