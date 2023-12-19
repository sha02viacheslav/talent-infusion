package com.talent.infusion.controller.user;

import java.util.HashMap;
import java.util.Optional;

import com.talent.infusion.entiry.user.User;
import com.talent.infusion.service.user.UserService;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;

public class UserController {
    public static final String USER_ID_PATH_PARAM = "id";
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public Handler getUserById = ctx -> {
        String id = ctx.pathParam(USER_ID_PATH_PARAM);

        HashMap<String, Object> resultMap = new HashMap<>();

        if (!id.isEmpty()) {
            Optional<User> user = userService.getUserById(Integer.parseInt(id));

            if (user.isEmpty()) {
                resultMap.put("success", false);
                resultMap.put("message", "User not found");
                ctx.status(HttpStatus.OK).json(resultMap);
            } else {
                resultMap.put("success", true);
                resultMap.put("data", userService.sanitizeUser(user.get()));
                ctx.status(HttpStatus.OK).json(resultMap);
            }
        } else {
            resultMap.put("success", false);
            resultMap.put("message", "Invalid user id");
            ctx.status(HttpStatus.OK).json(resultMap);
        }
    };
}
