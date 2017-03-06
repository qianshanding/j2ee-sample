package com.qianshanding.servlet.async;

import javax.servlet.AsyncContext;
import javax.servlet.ServletRequest;
import java.util.Date;

/**
 * 异步处理线程
 * Created by zhengyu on 2017/3/6.
 */
public class AsyncProcessThread implements Runnable {
    // 异步Context
    private AsyncContext asyncContext = null;

    public AsyncProcessThread(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    public void run() {
        try {
            Date startDate = new Date();
            Thread.sleep(5 * 1000);
            Date endDate = new Date();

            // 通过异步Context获取请求对象
            ServletRequest request = asyncContext.getRequest();
            request.setAttribute("name", "ServletAsync");
            request.setAttribute("startTime", startDate);
            request.setAttribute("endTime", endDate);
            asyncContext.dispatch("/async/servlet_async.jsp");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
