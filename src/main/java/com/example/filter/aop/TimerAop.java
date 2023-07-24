package com.example.filter.aop;

import com.example.filter.model.UserRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
public class TimerAop {

    //주의 점
    //스프링에게서 관리되는 빈들에게만 aop가 작동됨
    //스프링이 관리 하는 빈이 아닌 다른 클래스에 적용하고 싶으면 다른 AspectJ 라이브러리 사용
    @Pointcut(value = "within(com.example.filter.controller.UserApiController)")//지시어
    
    public void timerPointCut() {

    }

    @Before(value = "timerPointCut()")
    public void before(JoinPoint joinPoint){
        System.out.println("before");
    }
    @After(value = "timerPointCut()")
    public void after(JoinPoint joinPoint){
        System.out.println("after");
    }
    @AfterReturning(value = "timerPointCut()",returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result){
        System.out.println("after returning");
    }

    @AfterThrowing(value = "timerPointCut()",throwing = "tx")
    public void afterThrowing(JoinPoint joinPoint,Throwable tx){
        System.out.println("after throwing");
    }

    @Around(value = "timerPointCut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("메소드 실행전");
        Arrays.stream(joinPoint.getArgs()).forEach(
                it -> {
                    if (it instanceof UserRequest){
                        var tempUser = (UserRequest)it;
                        var phoneNumber = tempUser.getPhoneNumber().replace("-","");
                        tempUser.setPhoneNumber(phoneNumber);
                    }
                }
        );
        // 꺼냇다 치고 새로운 배열
        //암/복호화 / 로깅

        var newObj = Arrays.asList(
                new UserRequest()
        );

        var stopWatch = new StopWatch();
        stopWatch.start();
        joinPoint.proceed(newObj.toArray());
        stopWatch.stop();
        System.out.println("총 소요된 시간 MS : "+stopWatch.getTotalTimeMillis());
        System.out.println("메소드 실행후");
    }


}
