package com.qianshanding.jetty.server;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.TimeZone;

/**
 * Created by zhengyu on 2017/3/6.
 */
public class EmbeddedJettyServer {
    private static final String JETTY_PORT = "jetty.port";
    private static final String JETTY_CONTEXT_PATH = "jetty.context.path";
    private static final String JETTY_THREAD_COUNTS = "jetty.thread.counts";
    private static final String JETTY_ACCESS_LOG = "jetty.access.log";
    /**
     * jetty 端口
     */
    protected int port;
    /**
     * webapp contextpath
     */
    protected String contextPath = "/";
    /**
     * http线程池大小
     */
    protected int threadCounts = 50;
    /**
     * http连接空闲超时时间
     */
    protected int idleMilliseconds = 30000;

    /**
     * 请求缓存大小(当headerBuffer用尽的时候才使用，如果requestBuffer也用尽则返回413错误) these buffers
     * are only used for active connections that have requests with bodies that
     * will not fit within the header buffer
     */
    protected int requestBufferSize = 8 * 1024; // 默认8k

    /**
     * 头文件缓存大小 Set the size of the buffer to be used for request and response
     * headers http://docs.codehaus.org/display/JETTY/Configuring+Connectors#
     * ConfiguringConnectors-headerBufferSize
     */
    protected int headerBufferSize = 16 * 1024; // 默认4k

    /**
     * 默认不打印access日志
     */
    protected boolean enableAccessLog = false;

    /**
     * 　jetty　server　实体对象
     */
    private Server server;

    public EmbeddedJettyServer(int port) {
        super();
        this.port = port;
    }

    public EmbeddedJettyServer() {
        this(8080);
    }

    /**
     * 　启动jetty
     */
    final public void start() {
        // 初始化外部传入参数配置
        init();

        beforeServerStart();

        try {
            // 启动服务器
            startServer();
        } catch (Exception e) {
            System.exit(-1); // 端口冲突退出一了百了
        }

        afterServerStart();
    }

    /**
     * 在server启动前执行
     */
    protected void beforeServerStart() {

    }

    /**
     * 在server启动后执行
     */
    protected void afterServerStart() {

    }

    /**
     * 正式环境运行时，在此处理外部参数
     */
    private void init() {
        String port = System.getProperty(JETTY_PORT);
        if (port != null) {
            System.out.println("EmbeddedJettyServer begin to  set port" + port);
            setPort(Integer.valueOf(port));
        }

        String contextPath = System.getProperty(JETTY_CONTEXT_PATH);
        if (contextPath != null) {
            setContextPath(contextPath);
        }

        String threadCounts = System.getProperty(JETTY_THREAD_COUNTS);
        if (threadCounts != null) {
            setThreadCounts(Integer.valueOf(threadCounts));
        }

        String needAccessLogStr = System.getProperty(JETTY_ACCESS_LOG);
        if (needAccessLogStr != null) {
            try {
                Boolean needAccessLog = Boolean.valueOf(needAccessLogStr);
                setEnableAccessLog(needAccessLog);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 启动服务器
     */
    private void startServer() throws Exception {
        configuration();

        Runtime.getRuntime().addShutdownHook(new Thread("Embedded-Jetty-Shutdown-Hooker") {
            @Override
            public void run() {
                try {
                    server.stop();
                    server.destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        server.start();
        server.dump(System.err);
        server.join();
    }

    /**
     * 准备jetty启动环境，构建标准webapp应用
     */
    private void configuration() throws MalformedURLException {
        QueuedThreadPool threadPool = new QueuedThreadPool(threadCounts);
        threadPool.setName("Embedded-Jetty-ThreadPool");

        server = new Server(threadPool);

        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setRequestHeaderSize(headerBufferSize);
        httpConfig.setResponseHeaderSize(headerBufferSize);
        httpConfig.setHeaderCacheSize(headerBufferSize);

        httpConfig.setOutputBufferSize(requestBufferSize);

        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(httpConfig));
        http.setPort(port);
        http.setIdleTimeout(idleMilliseconds);

        server.setConnectors(new Connector[]{http});


        URI webResourceBase = findWebResourceBase(server.getClass().getClassLoader());
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setBaseResource(Resource.newResource(webResourceBase));
        webAppContext.setConfigurations(new Configuration[]
                {
                        new AnnotationConfiguration(),
                        new WebInfConfiguration(),
                        new WebXmlConfiguration(),
                        new MetaInfConfiguration(),
                        new FragmentConfiguration(),
                        new EnvConfiguration(),
                        new PlusConfiguration(),
                        new JettyWebXmlConfiguration()
                });
        webAppContext.setContextPath("/");
        webAppContext.setParentLoaderPriority(true);

        HandlerCollection handlerCollection = new HandlerCollection();
        handlerCollection.addHandler(webAppContext);

        // 设置jetty access日志
        if (isEnableAccessLog()) {
            RequestLogHandler requestLogHandler = new RequestLogHandler();
            NCSARequestLog requestLog = new NCSARequestLog("./logs/jetty-access.log.yyyy_mm_dd");
            requestLog.setRetainDays(7);
            requestLog.setAppend(true);
            requestLog.setExtended(false);
            requestLog.setLogTimeZone(TimeZone.getDefault().getID());
            requestLog.setLogLatency(true);
            requestLogHandler.setRequestLog(requestLog);
            handlerCollection.addHandler(requestLogHandler);
        }

        server.setHandler(handlerCollection);
    }

    private static URI findWebResourceBase(ClassLoader classLoader) {
        String webResourceRef = "WEB-INF/web.xml";

        try {
            // Look for resource in classpath (best choice when working with archive jar/war file)
            URL webXml = classLoader.getResource('/' + webResourceRef);
            if (webXml != null) {
                URI uri = webXml.toURI().resolve("..").normalize();
                System.err.printf("WebResourceBase (Using ClassLoader reference) %s%n", uri);
                return uri;
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException("Bad ClassPath reference for: " + webResourceRef, e);
        }

        // Look for resource in common file system paths
        try {
            Path pwd = new File(System.getProperty("user.dir")).toPath().toRealPath();
            FileSystem fs = pwd.getFileSystem();

            // Try the generated maven path first
            PathMatcher matcher = fs.getPathMatcher("glob:**/j2ee-*");
            try (DirectoryStream<Path> dir = Files.newDirectoryStream(pwd.resolve("target"))) {
                for (Path path : dir) {
                    if (Files.isDirectory(path) && matcher.matches(path)) {
                        // Found a potential directory
                        Path possible = path.resolve(webResourceRef);
                        // Does it have what we need?
                        if (Files.exists(possible)) {
                            URI uri = path.toUri();
                            System.err.printf("WebResourceBase (Using discovered /target/ Path) %s%n", uri);
                            return uri;
                        }
                    }
                }
            }

            // Try the source path next
            Path srcWebapp = pwd.resolve("src/main/webapp/" + webResourceRef);
            if (Files.exists(srcWebapp)) {
                URI uri = srcWebapp.getParent().toUri();
                System.err.printf("WebResourceBase (Using /src/main/webapp/ Path) %s%n", uri);
                return uri;
            }
        } catch (Throwable t) {
            throw new RuntimeException("Unable to find web resource in file system: " + webResourceRef, t);
        }

        throw new RuntimeException("Unable to find web resource ref: " + webResourceRef);
    }

    //以下都是getter和setter

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getIdleMilliseconds() {
        return idleMilliseconds;
    }

    public void setIdleMilliseconds(int idleMilliseconds) {
        this.idleMilliseconds = idleMilliseconds;
    }

    public int getThreadCounts() {
        return threadCounts;
    }

    public void setThreadCounts(int threadCounts) {
        this.threadCounts = threadCounts;
    }

    public Server getServer() {
        return server;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public boolean isEnableAccessLog() {
        return enableAccessLog;
    }

    public void setEnableAccessLog(boolean enableAccessLog) {
        this.enableAccessLog = enableAccessLog;
    }

}
