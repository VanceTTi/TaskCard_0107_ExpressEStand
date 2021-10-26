package com.fjt.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 映射器（包含了大量的网址与方法对应的关系）
 * 一个方法处理一个网址的请求
 */
public class HandlerMapping {
    /*
    这个map集合：当用户发送请求就会来Map集合中找对应的方法来处理请求
    HandlerMapping这个类里面有n个MVCMapping且都存在Map集合里
     */
    private static Map<String,MVCMapping> data = new HashMap<>();
    //提供一个get方法，作用：从data集合中获取到某一个的网址对应的方法
    public static MVCMapping get(String uri) {
        //String uri：用户请求的地址是什么，
        return  data.get(uri);//返回指定的key对应的value
    }


    /**
     * 这个方法用来加载配置文件
     * @param is
     */
    public static void load(InputStream is){
        //获取application.properties中描述的类
        Properties ppt = new Properties();
        try {
            ppt.load(is);//读取配置文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        取出(Properties)ppt中所有的值
        这里的values取出的就是application.properties中的类
         */
        //获取配置文件中描述的一个个的类。
        Collection<Object> values = ppt.values();
        for (Object cla: values) {
            String className = (String) cla;//拿到类的名称
            //通过反射创建对象
            try {
        //通过反射，加载配置文件中描述的每一个类//forName获取Class对象
                Class c = Class.forName(className);
                //然后通过创建这个类的对象
                Object obj = c.getConstructor().newInstance();
                //getMethods()返回此Class对象所表示的类或接口的public的方法
                Method[] methods = c.getMethods();
                for(Method m:methods){//遍历获取到的方法
                    Annotation[] as = m.getAnnotations();//得到所有的注解
                    //判断方法有什么注解
                    if (as != null) {
                        for (Annotation annotation:as){
                            if (annotation instanceof ResponseBody){//如果注解是ResponseBody
                                //说明此方法，用于返回字符串给客户端
                                //得到对象obj，得到对应方法m，拥有ResponseBody注解的，就给Text类型返回给用户
                                MVCMapping mapping = new MVCMapping(obj,m,ResponseType.TEXT);
                                //((ResponseBody) annotation).value()得到注解的值//保存到data集合中给用户响应用的
                                Object o = data.put(((ResponseBody) annotation).value(), mapping);
                                if (o != null) {
                                    //存在了重复的请求地址
                                    throw new RuntimeException("请求地址重复：" + ((ResponseBody) annotation).value());
                                }
                            } else if (annotation instanceof ResponseView) {
                                //说明此方法，用于返回界面给客户端，同理
                                MVCMapping mapping = new MVCMapping(obj,m,ResponseType.VIEW);
                                Object o = data.put(((ResponseView) annotation).value(),mapping);
                                if (o != null) {
                                    //存在了重复的请求地址
                                    throw new RuntimeException("请求地址重复：" + ((ResponseBody) annotation).value());
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 映射器，每一个对象封装了一个方法，用于处理请求
     */
    public static class MVCMapping{

        private Object obj;//obj对象
        private Method method;//方法
        private ResponseType type;//枚举的类型，是View还是test
        //构造方法
        public MVCMapping() {
        }

        public MVCMapping(Object obj, Method method, ResponseType type) {
            this.obj = obj;
            this.method = method;
            this.type = type;
        }
        //get/set
        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public ResponseType getType() {
            return type;
        }

        public void setType(ResponseType type) {
            this.type = type;
        }
    }

}
