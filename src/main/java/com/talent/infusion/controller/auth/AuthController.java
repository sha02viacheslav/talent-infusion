package com.talent.infusion.controller.auth;

import com.talent.infusion.dto.UserLoginDto;
import com.talent.infusion.dto.UserRegisterDto;
import com.talent.infusion.entiry.user.User;
import com.talent.infusion.service.auth.AuthService;
import com.talent.infusion.service.user.UserService;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
public class AuthController {
    private UserService userService;
    private AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    public Handler register = ctx -> {
        JavalinJackson jackson = new JavalinJackson();
        HashMap<String, Object> resultMap = new HashMap<>();
        UserRegisterDto userRegisterDto;

        try {
            userRegisterDto = jackson.fromJsonString(ctx.body(), UserRegisterDto.class);
            String email = userRegisterDto.getEmail();
            Optional<User> user = userService.getUserByEmail(email);

            if (user.isPresent()) {
                resultMap.put("success", false);
                resultMap.put("message", "User already exists");
                ctx.status(HttpStatus.BAD_REQUEST).json(resultMap);
                return;
            }

            User newUser = userService.createUser(userRegisterDto);
            String token = authService.signPayload(newUser);
            resultMap.put("success", true);
            resultMap.put("user", userService.sanitizeUser(newUser));
            resultMap.put("token", token);
            ctx.status(HttpStatus.OK).json(resultMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
            ctx.status(HttpStatus.SERVICE_UNAVAILABLE).json(resultMap);
        }
    };

    public Handler login = ctx -> {
        JavalinJackson jackson = new JavalinJackson();
        HashMap<String, Object> resultMap = new HashMap<>();
        UserLoginDto userLoginDto;

        try {
            userLoginDto = jackson.fromJsonString(ctx.body(), UserLoginDto.class);
            String email = userLoginDto.getEmail();
            String password = userLoginDto.getPassword();
            Optional<User> user = userService.getUserByEmail(email);

            if (password == null || password.isEmpty()) {
                resultMap.put("success", false);
                resultMap.put("message", "Invalid credential");
                ctx.status(HttpStatus.BAD_REQUEST).json(resultMap);
                return;
            }

            if (user.isEmpty()) {
                resultMap.put("success", false);
                resultMap.put("message", "User with provided email doesn't exist");
                ctx.status(HttpStatus.BAD_REQUEST).json(resultMap);
                return;
            }

            if(!BCrypt.checkpw(password, user.get().getPassword())) {
                resultMap.put("success", false);
                resultMap.put("message", "Invalid credential");
                ctx.status(HttpStatus.BAD_REQUEST).json(resultMap);
                return;
            }

            String token = authService.signPayload(user.get());
            resultMap.put("success", true);
            resultMap.put("user", userService.sanitizeUser(user.get()));
            resultMap.put("token", token);
            ctx.status(HttpStatus.OK).json(resultMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
            ctx.status(HttpStatus.SERVICE_UNAVAILABLE).json(resultMap);
        }
    };
}
