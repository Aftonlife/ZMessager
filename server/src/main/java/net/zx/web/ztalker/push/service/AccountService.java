package net.zx.web.ztalker.push.service;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.awt.*;

/**
 * @author Administrator
 * @date 2020/2/7
 * @time 18:46
 */
// 127.0.0.1/api/account/...
@Path("/account")
public class AccountService {
    @GET
    @Path("/login")
    public String get() {
        return "You get the login";
    }

    @POST
    @Path("/login")
    // 指定请求与返回的相应体为JSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String login(String name) {
        return "login:" + name;
    }
}
