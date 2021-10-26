package com.fjt.mvc;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

@WebServlet(name = "DispatcherServlet", value = "/DispatcherServlet")
public class DispatcherServlet extends HttpServlet {
    /**
     * 重写初始化方法init
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        //通过config(文件名)来获取初始化参数的文件，contentConfigLocation这个文件用来写初始化参数
        String path = config.getInitParameter("contentConfigLocation");//命名为路径，获取文件中存储的每一个键值对
        //加载path,返回输入流(读取)
        InputStream is = DispatcherServlet.class.getClassLoader().getResourceAsStream(path);
        //使用HandlerMapping.load(is);调取这个方法来加载这个输入流
        HandlerMapping.load(is);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取用户请求的uri  /xx.do的请求都会走这里
        String uri = req.getRequestURI();

        HandlerMapping.MVCMapping mapping = HandlerMapping.get(uri);
        //判断mapping是否存在
        if (mapping == null) {
            resp.sendError(404,"自定义MVC：映射地址不存在" + uri);
            return;
        }
        //取出mapping对象中的属性值
        Object obj = mapping.getObj();
        Method method = mapping.getMethod();//得到对应的方法

        Object result = null;
        try {
            //invoke(obj, req, resp)返回的是使用参数args在obj上指派该对象所表达方法的结果
            result  = method.invoke(obj, req, resp);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        switch (mapping.getType()){//mapping中的类型是什么
            case TEXT:
                resp.getWriter().write((String) result);//文字是String
                break;
            case VIEW:
                resp.sendRedirect((String) result);//重定向也是String
                break;
        }
    }
}
