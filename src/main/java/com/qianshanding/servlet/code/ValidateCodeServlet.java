package com.qianshanding.servlet.code;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Fish Exp
 * @version V1.0
 * @FileName ValidateCodeServlet.java
 * @Description: <servlet>
 * <servlet-name>ValidateCodeServlet</servlet-name>
 * <servlet-class>com.qianshanding.framework.servlet.ValidateCodeServlet</servlet-class>
 * <load-on-startup>0</load-on-startup>
 * <init-param>
 * <param-name>length</param-name>
 * <param-value>传入的参数值1</param-value>
 * </init-param>
 * <init-param>
 * <param-name>width</param-name>
 * <param-value>传入的参数值2</param-value>
 * </init-param>
 * </servlet>
 * <br />
 * code的session key为：verify_code
 * <p>
 * /**
 * Servlet异步调用
 * Created by zhengyu on 2017/3/6.
 * <p>
 * 使用@WebServlet将一个继承于javax.servlet.http.HttpServlet的类定义为Servlet组件。
 * @WebServlet有很多的属性：<br />
 * <p>
 * 属性名            类型             属性描述
 * name	            String	        指定servlet的name属性,等价于<Servlet-name>.如果没有显示指定，则该servlet的取值即为类的全限定名.
 * value	        String[]	    等价于urlPatterns,二者不能共存.
 * urlPatterns	    String[]	    指定一组servlet的url的匹配模式,等价于<url-pattern>标签.
 * loadOnStartup	int	            指定servlet的加载顺序,等价于<load-on-startup>标签.
 * initParams	    WebInitParam[]	指定一组初始化参数,等价于<init-param>标签.
 * asyncSupported	boolean	        申明servlet是否支持异步操作模式,等价于<async-supported>标签.
 * displayName	    String	        servlet的显示名,等价于<display-name>标签.
 * description	    String	        servlet的描述信息,等价于<description>标签.
 * <p>
 * Servlet的访问URL是Servlet的必选属性，可以选择使用urlPatterns或者value定义。
 * <p>
 * <p>
 * Servlet注解技术：
 * @WebServlet 取代servlet配置
 * @WebFilter 取代filter配置
 * @WebInitParam 取代初始化参数配置（servlet、filter）
 * @WebListener 取代listener配置
 * @Date 2016年7月1日 下午2:20:35
 */

@WebServlet(urlPatterns = "/validate_code", loadOnStartup = 0, initParams = {
        @WebInitParam(name = "width", value = "50"),
        @WebInitParam(name = "length", value = "200")})
public class ValidateCodeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest reqeust, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //加载初始化传入参数
        String length = this.getInitParameter("length");
        String width = this.getInitParameter("width");
        HttpSession session = reqeust.getSession();
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        session.setAttribute("verify_code", verifyCode);
        VerifyCodeUtils.outputImage(Integer.parseInt(length), Integer.parseInt(width), response.getOutputStream(), verifyCode);
    }
}