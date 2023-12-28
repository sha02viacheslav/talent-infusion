package com.talent.infusion;

import com.talent.infusion.controller.auth.AuthController;
import com.talent.infusion.controller.invitation.InvitationController;
import com.talent.infusion.controller.user.UserController;
import com.talent.infusion.repository.invitation.InvitationRepository;
import com.talent.infusion.repository.user.UserRepository;
import com.talent.infusion.service.auth.AuthService;
import com.talent.infusion.service.invitation.InvitationService;
import com.talent.infusion.service.user.UserService;
import org.javalite.activejdbc.DB;

public class Configuration {
    private static final DB db = new DB();
    private static final UserRepository userRepository = new UserRepository(db);
    private static final InvitationRepository invitationRepository = new InvitationRepository(db);

    private static final UserService userService = new UserService(userRepository);
    private static final AuthService authService = new AuthService();
    private static final InvitationService invitationService = new InvitationService(invitationRepository);

    private static final UserController userController = new UserController(userService, authService);
    private static final AuthController authController = new AuthController(userService, authService);
    private static final InvitationController invitationController = new InvitationController(invitationService, userService);

    public static UserService userService() {
        return userService;
    }

    public static UserRepository userRepository() {
        return userRepository;
    }

    public static UserController userController() {
        return userController;
    }

    public static AuthController authController() {
        return authController;
    }

    public static InvitationController invitationController() {
        return invitationController;
    }

    public static DB db() {
        return db;
    }
}
