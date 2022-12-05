package com.its.member.intercepter;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//로그인 여부 확인
// 로그인 하지 않은 상태라면 로그인 페이지로 보내고 로그인을 수행하면 직전에
// 요청한 주소로 보내줌.
// 로그인 상태라면 넘어감.
public class LoginCheckIntercepter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestURL = request.getRequestURI();
        //요청한 주소값을 가져오는 코드
        // HttpSession을 정의하지 않는 이유
        // 상속클래스이기 때문 (재정의 어노테이션)
        HttpSession session = request.getSession();
        if(session.getAttribute("loginEmail") == null ){
            response.sendRedirect("/member/login?redirectURL="+requestURL);
            return false;
        }
        return true;
    }
}
