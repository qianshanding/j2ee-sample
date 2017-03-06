package com.qianshanding.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet生命周期演示
 * Created by zhengyu on 2017/3/6.
 */
@WebServlet(urlPatterns = "/life_circle")
public class LifeCircleServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("content-type", "text/html;charset=UTF-8");

        System.out.println("running service method。");
        resp.getWriter().println("Servlet生命周期演示。<br> Servlet 执行了！ hashcode:" + this.hashCode());
    }

    @Override
    public void destroy() {
        System.out.println("Servlet destroy！");
    }

    @Override
    public void init() throws ServletException {
        System.out.println("Servlet init！\t（Servlet是单例的。）");
    }


}