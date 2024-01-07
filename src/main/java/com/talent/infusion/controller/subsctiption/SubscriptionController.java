package com.talent.infusion.controller.subsctiption;

import com.talent.infusion.entiry.subscription.TiSubscription;
import com.talent.infusion.entiry.user.User;
import com.talent.infusion.service.subscription.SubscriptionService;
import com.talent.infusion.service.user.UserService;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Optional;


@Slf4j
public class SubscriptionController {
    public static final String SUBSCRIPTION_USERID_PATH_PARAM = "id";
    private SubscriptionService subscriptionService;
    private UserService userService;

    public SubscriptionController(SubscriptionService subscriptionService, UserService userService) {
        this.subscriptionService = subscriptionService;
        this.userService = userService;
    }

    public Handler cancelSubscription = ctx -> {
        String userId = ctx.pathParam(SUBSCRIPTION_USERID_PATH_PARAM);
        HashMap<String, Object> resultMap = new HashMap<>();

        try {
            Optional<User> user = userService.getUserById(Integer.parseInt(userId));
            if (user.isEmpty()) {
                resultMap.put("success", false);
                resultMap.put("message", "User not found or userId is not correct");
                ctx.status(HttpStatus.BAD_REQUEST).json(resultMap);
                return;
            }

            Optional<TiSubscription> subscription = subscriptionService.getSubscriptionUserId(Integer.parseInt(userId));
            if (subscription.isEmpty()) {
                resultMap.put("success", false);
                resultMap.put("message", "Subscription not found");
                ctx.status(HttpStatus.OK).json(resultMap);
                return;
            }

            subscriptionService.cancelSubscription(subscription.get().getStripeSubscriptionId());

            resultMap.put("success", true);
            ctx.status(HttpStatus.OK).json(resultMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
            ctx.status(HttpStatus.SERVICE_UNAVAILABLE).json(resultMap);
        }
    };
}
