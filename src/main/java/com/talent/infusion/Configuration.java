package com.talent.infusion;

import com.talent.infusion.controller.auth.AuthController;
import com.talent.infusion.controller.invitation.InvitationController;
import com.talent.infusion.controller.payment.PaymentController;
import com.talent.infusion.controller.subsctiption.SubscriptionController;
import com.talent.infusion.controller.talent.TalentController;
import com.talent.infusion.controller.user.UserController;
import com.talent.infusion.repository.invitation.InvitationRepository;
import com.talent.infusion.repository.payment.PaymentRepository;
import com.talent.infusion.repository.subscription.SubscriptionRepository;
import com.talent.infusion.repository.talent.TalentRepository;
import com.talent.infusion.repository.user.UserRepository;
import com.talent.infusion.service.auth.AuthService;
import com.talent.infusion.service.invitation.InvitationService;
import com.talent.infusion.service.payment.PaymentService;
import com.talent.infusion.service.subscription.SubscriptionService;
import com.talent.infusion.service.talent.TalentService;
import com.talent.infusion.service.user.UserService;
import org.javalite.activejdbc.DB;

public class Configuration {
    private static final DB db = new DB();
    private static final UserRepository userRepository = new UserRepository(db);
    private static final InvitationRepository invitationRepository = new InvitationRepository(db);
    private static final TalentRepository talentRepository = new TalentRepository();
    private static final PaymentRepository paymentRepository = new PaymentRepository(db);
    private static final SubscriptionRepository subscriptionRepository = new SubscriptionRepository(db);

    private static final UserService userService = new UserService(userRepository);
    private static final AuthService authService = new AuthService();
    private static final InvitationService invitationService = new InvitationService(invitationRepository);
    private static final TalentService talentService = new TalentService(talentRepository);
    private static final SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository, userService);
    private static final PaymentService paymentService = new PaymentService(paymentRepository, userService, subscriptionService);


    private static final UserController userController = new UserController(userService, authService);
    private static final AuthController authController = new AuthController(userService, authService);
    private static final InvitationController invitationController = new InvitationController(invitationService, userService);
    private static final TalentController talentController = new TalentController(talentService);
    private static final PaymentController paymentController = new PaymentController(paymentService, userService);
    private static final SubscriptionController subscriptionController = new SubscriptionController(subscriptionService, userService);

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

    public static TalentController talentController() {
        return talentController;
    }

    public static PaymentController paymentController() {
        return paymentController;
    }

    public static SubscriptionController subscriptionController() {
        return subscriptionController;
    }

    public static DB db() {
        return db;
    }
}
