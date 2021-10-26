package com.fjt.mvc;
import java.lang.annotation.*;
/**
 * 自定义注解：
 * 用这个注解的方法就表示，要返回文字内容给用户(发送请求者)
 * 注解的作用：
 *    被此注解添加的方法，会被用于处理请求。
 *  方法返回的内容，会以文字的形式返回客户端。(返回字符串给客户端)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseBody {
    String value();
}
