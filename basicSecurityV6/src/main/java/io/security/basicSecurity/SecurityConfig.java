package io.security.basicSecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    //사용자 생성
    @Override
    protected void configure(AuthenticationManagerBuilder auth)throws  Exception{


        auth.inMemoryAuthentication().withUser("user").password("{noop}1234").roles("USER");
        auth.inMemoryAuthentication().withUser("sys").password("{noop}1234").roles("SYS");
        auth.inMemoryAuthentication().withUser("admin").password("{noop}1234").roles("ADMIN");

    }

    @Override
    protected  void configure(HttpSecurity http) throws Exception{

        //인가 정책
        http

                .authorizeRequests()
                // 로그인은 누구나 // denined 는 실패하긴 햇지만 '인증'을 한 유저 그래서 permitAll x
                .antMatchers("/user").hasRole("USER")
                .antMatchers("/login").permitAll()
                .antMatchers("/admin/pay").hasRole("ADMIN")
                .antMatchers("/admin/** ").access("hasRole('ADMIN') or hasRole('SYS')")
                .anyRequest().authenticated(); // 어떠한 요청에도 보안'인증'이 필요하다.

        //인증 정책
        http
                .formLogin()
                // 인증안하고 권한 사이트 접속 -> 로그인화면 ->성공 -> 다시 그 요청화면으
                        .successHandler(new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

                                //여기에 저장 하고 로그인 성공하면 꺼내온다
                                RequestCache requestCache = new HttpSessionRequestCache();

                                //세션에서 그 저장된 값을 가져온다
                                SavedRequest savedRequest = requestCache.getRequest(request, response);
                                // 그 값에대한 URL 추출
                                String redirectUrl = savedRequest.getRedirectUrl();
                                //그 URL 로 다시 보내기
                                response.sendRedirect(redirectUrl);

                            }
                        });


        http
                .exceptionHandling()
                //인증 실패
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

                        //인증 예외발생시 다시  ' 우리가 만든' 로그인 페이지로
                        response.sendRedirect("/login");

                    }
                })
                //인가 실패
                .accessDeniedHandler(new AccessDeniedHandler() {

                    //인가 실패시
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        response.sendRedirect("/denined ");
                    }
                });






        //세션 고정보호, 세션 정책
       /* http
                .sessionManagement()
                //세선고정보호  = 세션은 그대로 세션id만 인증했을때마다 바뀐다
                .sessionFixation().changeSessionId()
                //세션정책 //Always//If Required//Never//stateless
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

*/

        //세션 기본
        /*
        http
                .sessionManagement()
                //세션 계수
                .maximumSessions(1)
                //세션 동시접 불가(현재 사용자 접속불가 // fals = 이전 사용자 세션만료
                .maxSessionsPreventsLogin(true)
        */







        /*//로그아웃은 기본적을 post 방식으로 처리한다
        http
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("login")
               //로그아웃 처리할떄 세션종료 등 다른 핸들러들 많다. 이건 따로 다른 작업을 원할때 사용
                //로그아웃 할떄 하는 로직
                .addLogoutHandler(new LogoutHandler() {
                    @Override
                    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                        //세션 무효화 처리
                        HttpSession session = request.getSession();
                        session.invalidate();

                    }
                })
                //logoutSuccessUrl은 그냥 이동만 하고 이건 좀더 (성공시) 다양한 로직 구현 가능
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.sendRedirect("/login");
                    }
                })
                //서버에서 만든 쿠키 삭제
                .deleteCookies("remember-me")


                .and()


                .rememberMe()
                //원하는 이름으로 변경 가능
                .rememberMeParameter("remember")
                //토큰 생존시간 설정
                .tokenValiditySeconds(3600)
                // rememberme 인증과정시 유저 계정 조회시 사용되는 객체
                .userDetailsService(userDetailsService());







*/        /////로그인//////


                /*.loginPage("/loginPage") // 로그인 페이지
                .defaultSuccessUrl("/") //로그인 성공시
                .failureUrl("/loginPage")//로그인 실패시
                .usernameParameter("userId")
                .passwordParameter("passWd")
                .loginProcessingUrl("login_proc")
                //로그인 성공시
                //인증한 결과를 담는다
                //Authentication 는 인증하고난뒤 '인증결과' 를 담은 곳
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        System.out.println("authentication = " + authentication.getName());  //인증에 성공한 유저 네임
                        response.sendRedirect("/"); //인증 성공뒤 루트페이지로
                    }
                })
                //실패했을경우 이 handler 호출
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        System.out.println("exception = " + exception.getMessage());
                        response.sendRedirect("/loginPage");
                    }
                })
                .permitAll();*/


    }
}
