package com.qianshanding.servlet.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by zhengyu on 2017/3/8.
 *
 * @WebFilter是过滤器的注解，不需要在web.xml进行配置常用属性如下：<br />
 * filterName       String	        指定过滤器的 name 属性，等价于 <filter-name>
 * value	        String[]	    该属性等价于 urlPatterns 属性。但是两者不应该同时使用。
 * urlPatterns	    String[]	    指定一组过滤器的 URL 匹配模式。等价于 <url-pattern> 标签。
 * servletNames	    String[]	    指定过滤器将应用于哪些 Servlet。取值是 @WebServlet 中的 name 属性的取值，或者是 web.xml 中 <servlet-name> 的取值。
 * dispatcherTypes	DispatcherType	指定过滤器的转发模式。具体取值包括： ASYNC、ERROR、FORWARD、INCLUDE、REQUEST。
 * initParams	    WebInitParam[]	指定一组过滤器初始化参数，等价于 <init-param> 标签。
 * asyncSupported	boolean	        声明过滤器是否支持异步操作模式，等价于 <async-supported> 标签。
 * description	    String	        该过滤器的描述信息，等价于 <description> 标签。
 * displayName	    String	        该过滤器的显示名，通常配合工具使用，等价于 <display-name> 标签。
 */
@WebFilter(value = {"/*", "/test"}, asyncSupported = true)
/**
 * 异步请求必须异步Filter
 * 示例A:
 * @WebFilter(servletNames = {"MyFourServlet", "MyFiveServlet"})
 * @WebFilter(
 *      urlPatterns = "/five",
 *      filterName = "FiveFilter",
 *      initParams = {
 *              @WebInitParam(name = "name", value = "username"),
 *              @WebInitParam(name = "value", value = "password")
 *      },
 *      description = "MyFiveFilter",
 *      dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD}
 * )
 *
 * 示例B:
 * @WebFilter(servletNames = {"MyFourServlet", "MyFiveServlet"})
 * @WebFilter(servletNames = "MyFourServlet")
 */
public class AsyncFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init ..." + filterConfig.toString());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("do filter ..." + chain.toString());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("destroy ...");
    }
}
