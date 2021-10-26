package com.fjt.test2;

import com.fjt.mvc.ResponseBody;
import com.fjt.mvc.ResponseView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XXXController {
    @ResponseBody("/test1.do")
    public String test1(HttpServletRequest request, HttpServletResponse response){
        return "hhhh";
    }

    @ResponseBody("/test1")
    public String test2(HttpServletRequest request, HttpServletResponse response){
        return "hhhh";
    }

    @ResponseView("/test3.do")
    public String test3(HttpServletRequest request, HttpServletResponse response){
        return "xxx.jsp";
    }

}
