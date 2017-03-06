package com.qianshanding.servlet.async;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

/**
 * Servlet异步调用
 * Created by zhengyu on 2017/3/6.
 * <p>
 * 使用@WebServlet将一个继承于javax.servlet.http.HttpServlet的类定义为Servlet组件。
 *
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
 */
@WebServlet(urlPatterns = "/test_async", asyncSupported = true)
public class AsyncServlet extends HttpServlet {

    private static final long serialVersionUID = -6795872120748178723L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setContentType("text/html;charset=UTF-8");

            // 通过Request获取AsyncContext
            AsyncContext asyncContext = req.startAsync();
            // 设置异步调用的超时时间(30秒)
            asyncContext.setTimeout(30 * 1000);

            Date startTime = new Date();
            System.out.println("Servlet开始时间 --- " + startTime);
            // 开始异步请求
            asyncContext.start(new AsyncProcessThread(asyncContext));

            Date endTime = new Date();
            System.out.println("Servlet结束时间 --- " + endTime);
            //都是无阻塞的，所以开始和结束的时间差基本都是为0
            System.out.println("开始和结束时间差 --- " + (startTime.getTime() - endTime.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}