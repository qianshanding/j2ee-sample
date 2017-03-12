# j2ee-sample
 J2EE的一些实例。包含通过Jetty实用Main启动服务、验证码、Servlet异步请求。
##目录
* 启动
```
mvn clean install exec:exec
```
* 项目结构
```
├── com.qianshanding.Launcher                   //Jetty Main启动类
├── com.qianshanding.servlet                    //servlet相关实例
├── com.qianshanding.servlet.LifeCircleServlet  //查看Servlet生命周期
├── com.qianshanding.servlet.async
├── com.qianshanding.servlet.async.AsyncProcessThread   //异步线程
├── com.qianshanding.servlet.async.AsyncServlet         //异步Servlet
├── com.qianshanding.servlet.async.code
├── com.qianshanding.servlet.async.code.VerifyCodeUtils     //验证码工具类
├── com.qianshanding.servlet.async.code.ValidateCodeServlet //验证码servlet 
├── com.qianshanding.servlet.async.filter.AsyncFilter       //过滤器
```
* 实例
    * 验证码
    ```
    http://localhost:9999/validate_code
    ```
    效果：
        ![code](https://github.com/qianshanding/j2ee-sample/blob/master/validate_code.jpg "验证码")
        
    * 异步调用
    ```
    http://localhost:9999/test_async
    ```
    * 生命周期
    ```
    http://localhost:9999/life_circle
    ```
    


* Servlet相关注解
@WebServlet有很多的属性：

|参数|类型|描述|
|---|---|---
|name|String|指定servlet的name属性,等价于<Servlet-name>.如果没有显示指定，则该servlet的取值即为类的全限定名.
|value|String[]|等价于urlPatterns,二者不能共存.
|urlPatterns|String[]|指定一组servlet的url的匹配模式,等价于<url-pattern>标签.
|loadOnStartup|int|指定servlet的加载顺序,等价于<load-on-startup>标签.
|initParams|WebInitParam[]|指定一组初始化参数,等价于<init-param>标签.
|asyncSupported|boolean|申明servlet是否支持异步操作模式,等价于<async-supported>标签.
|displayName|String|servlet的显示名,等价于<display-name>标签.
|description|String|servlet的描述信息,等价于<description>标签.
@WebFilter是过滤器的注解，不需要在web.xml进行配置常用属性如下：

|参数|类型|描述|
|---|---|---
|filterName|String|指定过滤器的 name 属性，等价于 <filter-name>
|value|String[]|该属性等价于 urlPatterns 属性。但是两者不应该同时使用。
|urlPatterns|String[]|指定一组过滤器的 URL 匹配模式。等价于 <url-pattern> 标签。
|servletNames|String[]|指定过滤器将应用于哪些 Servlet。取值是 @WebServlet 中的 name 属性的取值，或者是 web.xml 中 <servlet-name> 的取值。
|dispatcherTypes|DispatcherType|指定过滤器的转发模式。具体取值包括： ASYNC、ERROR、FORWARD、INCLUDE、REQUEST。
|initParams|WebInitParam[]|指定一组过滤器初始化参数，等价于 <init-param> 标签。
|asyncSupported|boolean|声明过滤器是否支持异步操作模式，等价于 <async-supported> 标签。
|description|String|该过滤器的描述信息，等价于 <description> 标签。
|displayName|String|该过滤器的显示名，通常配合工具使用，等价于 <display-name> 标签。
```
 @WebFilter(value = {"/*", "/test"}, asyncSupported = true)
 异步请求必须异步Filter
 示例A:
 @WebFilter(servletNames = {"MyFourServlet", "MyFiveServlet"})
 @WebFilter(
      urlPatterns = "/five",
      filterName = "FiveFilter",
      initParams = {
              @WebInitParam(name = "name", value = "username"),
              @WebInitParam(name = "value", value = "password")
      },
      description = "MyFiveFilter",
      dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD}
 )
 
 示例B:
 @WebFilter(servletNames = {"MyFourServlet", "MyFiveServlet"})
 @WebFilter(servletNames = "MyFourServlet")
 ```
 Servlet注解技术：
 ```
 @WebServlet 取代servlet配置
 @WebFilter 取代filter配置
 @WebInitParam 取代初始化参数配置（servlet、filter）
 @WebListener 取代listener配置
 ```