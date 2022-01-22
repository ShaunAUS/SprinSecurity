package corespringsecurity.security.config;


import corespringsecurity.security.provider.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.cert.Extension;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailService;
    private final AuthenticationDetailsSource authenticationDetailsSource;


    //userDetailService 를 구현한게 user + 우리가 만든것(?)


    // 스프링 시큐리티가 우리가 만든 userDetailService 를 사용해서 '인증 처리'를 함
    // userDetailService 를 구현한 CustomUserDetailService(우리가 만든)를 실행!
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

       // auth.userDetailsService(userDetailService);

        auth.authenticationProvider(authenticationProvider());
    }


    //우리가 만든 provider 사용
    @Bean
    public AuthenticationProvider authenticationProvider() {

        return new CustomAuthenticationProvider();
    }


    //패스워드 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {

       return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    //정적 파일들은 보안필터를 거치지 않는다.
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        http
                .authorizeRequests()
                .antMatchers("/","/users").permitAll()
                .antMatchers("/mypage").hasRole("USER")
                .antMatchers("/message").hasRole("MANAGER")
                .antMatchers("/config").hasRole("ADMIN")

                .anyRequest().authenticated()

                .and()

                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .authenticationDetailsSource(authenticationDetailsSource)
                .defaultSuccessUrl("/")
                .permitAll();
    }


}
