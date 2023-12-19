package com.talent.infusion.guard;

import com.talent.infusion.jwt.JWT;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.security.RouteRole;

import java.util.Set;


public class AuthGuard {

    private static final JWT jwt = new JWT();

    public static void accessManager(Handler handler, Context ctx, Set<? extends RouteRole> permittedRoles) throws Exception {
        if (permittedRoles.contains(AuthRole.LOGGED_IN_USER) && !isAuthenticated(ctx)) {
            throw new UnauthorizedResponse();
        }

        handler.handle(ctx);
    }

    private static boolean isAuthenticated(Context ctx) {
        String token = ctx.header("Authorization");
        return token != null && jwt.verifyJWT(token);
    }
}
