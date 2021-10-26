package com.fjt.test;

import com.fjt.mvc.ResponseBody;
import com.fjt.mvc.ResponseView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserController {
    @ResponseBody("/login.do")
    public String login(HttpServletRequest request, HttpServletResponse response){
        return "login success";
    }
    @ResponseView("/reg.do")
    public String reg(HttpServletRequest request, HttpServletResponse response) {
        return "success.jsp";
    }
}
