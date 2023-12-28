package com.talent.infusion;


import com.talent.infusion.controller.main.MainController;
import com.talent.infusion.controller.user.UserController;
import com.talent.infusion.guard.AuthGuard;
import com.talent.infusion.guard.AuthRole;
import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;
import org.javalite.activejdbc.Base;

import static com.talent.infusion.Configuration.authController;
import static com.talent.infusion.Configuration.userController;
import static io.javalin.apibuilder.ApiBuilder.*;

@Slf4j
public class Main {
    public static void main(String[] args) {
        try {
            // Load environment variables from the .env file
            Dotenv dotenv = Dotenv.configure().load();

            // Access environment variables
            String portStr = dotenv.get("PORT");

            Javalin app = Javalin.create(config -> {
                config.accessManager(AuthGuard::accessManager);
            }).start((portStr != null && !portStr.isEmpty()) ? Integer.parseInt(portStr) : 7070);
            configureEndpoints(app);

            // Ensure ActiveJDBC connection is closed when the application stops
            app.before(ctx -> {
                if (!Base.hasConnection()) {
                    Base.open(dotenv.get("DB_DRIVER"), dotenv.get("DB_URL"), dotenv.get("DB_USERNAME"), dotenv.get("DB_PASSWORD"));
                }
            });

            app.after(ctx -> {
                if (Base.hasConnection()) {
                    Base.close();
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage() + " app error");
        }
    }

    private static void configureEndpoints(Javalin app) {
        app.routes(() -> {
            path("/", () -> {
                get(MainController::getMain);
            });

            path("/auth", () -> {
                path("register", () -> {
                    post(authController().register);
                });
                path("login", () -> {
                    post(authController().login);
                });
                path("forgot-password", () -> {
                    post(authController().forgotPassword);
                });
                path("check-code", () -> {
                    post(authController().checkVerificationCode);
                });
                path("resend-code", () -> {
                    post(authController().resendCode);
                });
                path("reset-password", () -> {
                    post(authController().resetPassword);
                });
                path("onlyauth", () -> {
                    get(authController().hiddenInformation, AuthRole.LOGGED_IN_USER);
                });
            });

            path("/user", () -> {
                path(String.format("{%s}", UserController.USER_ID_PATH_PARAM), () -> {
                    get(userController().getUserById, AuthRole.LOGGED_IN_USER);
                    put(userController().updateUser, AuthRole.LOGGED_IN_USER);
                    delete(userController().deleteUser, AuthRole.LOGGED_IN_USER);
                });
            });
        });
    }
}