package net.zx.web.ztalker.push.provider;

import com.google.common.base.Strings;
import net.zx.web.ztalker.push.bean.api.base.ResponseModel;
import net.zx.web.ztalker.push.bean.db.User;
import net.zx.web.ztalker.push.factory.UserFactory;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.security.Principal;

/**
 * @author Administrator
 * @date 2020/2/20
 * @time 9:43
 * 过滤器判断token
 */
public class AuthRequestFilter implements ContainerRequestFilter {
    /*接口过滤*/
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        /*请求路径*/
        String relationPath = ((ContainerRequest) requestContext).getPath(false);
        /*是登录注册自己返回*/
        if (relationPath.startsWith("account/login") ||
                relationPath.startsWith("account/register")) {
            return;
        }

        String token = requestContext.getHeaders().getFirst("token");
        if (!Strings.isNullOrEmpty(token)) {
            final User self = UserFactory.findByToken(token);
            if (null != self) {
                /*给当前请求添加一个上下文*/
                requestContext.setSecurityContext(new SecurityContext() {
                    /*主体部分*/
                    @Override
                    public Principal getUserPrincipal() {
                        /*User 需要实现Principal*/
                        return self;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        // 可以在这里写入用户的权限，role 是权限名，
                        // 可以管理管理员权限等等
                        return false;
                    }

                    @Override
                    public boolean isSecure() {
                        // 默认false即可，HTTPS
                        return false;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return null;
                    }
                });
                // 写入上下文后就返回
                return;
            }
        }

        /*构建一个需要登录的返回*/
        ResponseModel model = ResponseModel.buildLoginError();
        /*构建返回*/
        Response response = Response.status(Response.Status.OK)
                .entity(model)
                .build();
        /*拦截，停止一个请求的继续分发，调用后直接返回请求，不会走service*/
        requestContext.abortWith(response);
    }
}
