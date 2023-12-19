package com.talent.infusion.controller.user;

import java.util.HashMap;
import java.util.Optional;

import com.talent.infusion.dto.UpdateUserDto;
import com.talent.infusion.entiry.user.User;
import com.talent.infusion.service.auth.AuthService;
import com.talent.infusion.service.user.UserService;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserController {
    public static final String USER_ID_PATH_PARAM = "id";
    private UserService userService;
    private AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
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

    public Handler updateUser = ctx -> {
        String id = ctx.pathParam(USER_ID_PATH_PARAM);

        JavalinJackson jackson = new JavalinJackson();
        HashMap<String, Object> resultMap = new HashMap<>();
        UpdateUserDto updateUserDto;

        try {
            updateUserDto = jackson.fromJsonString(ctx.body(), UpdateUserDto.class);
            Optional<User> user = userService.updateUser(Integer.parseInt(id), updateUserDto);

            if (user.isEmpty()) {
                resultMap.put("success", false);
                resultMap.put("message", "User not found");
                ctx.status(HttpStatus.OK).json(resultMap);
                return;
            }

            String token = authService.signPayload(user.get());
            resultMap.put("success", true);
            resultMap.put("token", token);
            resultMap.put("updatedUser", userService.sanitizeUser(user.get()));
            ctx.status(HttpStatus.OK).json(resultMap);

        } catch (Exception e) {
            log.error(e.getMessage());
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
            ctx.status(HttpStatus.SERVICE_UNAVAILABLE).json(resultMap);
        }
    };
}
