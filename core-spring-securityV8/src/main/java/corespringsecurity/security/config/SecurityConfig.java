package corespringsecurity.security.config;


import corespringsecurity.security.common.FormAuthenticationDetailsSource;
import corespringsecurity.security.handler.CustomAccessDeniedHandler;
import corespringsecurity.security.provider.FormAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(0)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailService;
    private final FormAuthenticationDetailsSource formAuthenticationDetailsSource;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;




    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {


        //provider??? ????????? ?????? userDetailService ??? ???????????? ?????????(?????? ?????????????????????) provider ??????
        auth.authenticationProvider(authenticationProvider());

        // ????????? ??????????????? ????????? ?????? userDetailService ??? ???????????? '?????? ??????'??? ???
        //auth.userDetailsService(userDetailService);

    }

    //????????? ?????? provider ??????
    @Bean
    public AuthenticationProvider authenticationProvider() {

        return new FormAuthenticationProvider(passwordEncoder());
    }


    //???????????? ?????????
    @Bean
    public PasswordEncoder passwordEncoder() {

       return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    //?????? ???????????? ??????????????? ????????? ?????????.
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        http
                .authorizeRequests()
                .antMatchers("/","/users","user/login/**","login*").permitAll()
                .antMatchers("/mypage").hasRole("USER")
                .antMatchers("/message").hasRole("MANAGER")
                .antMatchers("/config").hasRole("ADMIN")

                .anyRequest().authenticated()

        .and()

                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .authenticationDetailsSource(formAuthenticationDetailsSource)
                .defaultSuccessUrl("/")
                //??????????????? ???????????? ->?????? ????????? ?????????
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll()


        .and()
                //?????? ????????????(?????????????????????) exceptionTranslateFilter??? ??????
                .exceptionHandling()
                .accessDeniedHandler(accessDenidedHandler())


                ;

    }

    @Bean
    private AccessDeniedHandler accessDenidedHandler() {

        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
        accessDeniedHandler.setErrorPage("/denied");

        return accessDeniedHandler;
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }




}
