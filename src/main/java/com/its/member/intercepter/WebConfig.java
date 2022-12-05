package com.its.member.intercepter;

import org.hibernate.Interceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 해당 클래스의 설정 정보를 스프링 컨테이너에 등록
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LoginCheckIntercepter()) // 인터셉터로 등록할 클래스
                .order(1) // 해당 인터셉터의 우선순위
                .addPathPatterns("/**") // 인터셉터로 체크할 주소 (모든주소)
                .excludePathPatterns("/","/member/save","/member/login","/js/**",
                                        "/css/**","/error","/images/**",
                                        "/*.ico","/favicon/**" );    // 예외로 할 주소(로그인을 하지않아도 사용할수 있는 주소)
    }
}
