package net.zx.web.ztalker.push.service;

import net.zx.web.ztalker.push.bean.db.User;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * @author Administrator
 * @date 2020/2/20
 * @time 10:05
 */
public class BaseService {
    // 添加一个上下文注解，该注解会给securityContext赋值
    // 具体的值为我们的拦截器中所返回的SecurityContext
    @Context
    protected SecurityContext securityContext;

    /**
     * 从上下文获取自己的信息
     *
     * @return
     */
    protected User getSelf() {
        return (User) securityContext.getUserPrincipal();
    }
}
