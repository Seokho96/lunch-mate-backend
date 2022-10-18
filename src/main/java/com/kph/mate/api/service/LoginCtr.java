package com.kph.mate.api.service;

import com.kph.mate.common.util.StringUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController("/api/login")
public class LoginCtr {

    @GetMapping("/")
    public void DoitTest(HttpServletRequest req, HttpServletResponse res, @RequestParam Map<String, Object> param){

        String check = StringUtil.convNull(param.get("check"), "error");

        System.out.println( "@@@@@@@@@@@@@@@@@@@"  );
        System.out.println( check );
        System.out.println( "@@@@@@@@@@@@@@@@@@@" );
    }
}
