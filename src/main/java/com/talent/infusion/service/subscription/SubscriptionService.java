package com.talent.infusion.service.subscription;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.stripe.param.SubscriptionUpdateParams;
import com.talent.infusion.dto.UpdateSubscriptionDto;
import com.talent.infusion.entiry.subscription.TiSubscription;
import com.talent.infusion.entiry.user.User;
import com.talent.infusion.repository.subscription.SubscriptionRepository;
import com.talent.infusion.service.user.UserService;
import io.github.cdimascio.dotenv.Dotenv;

import javax.inject.Singleton;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@Singleton
public class SubscriptionService {
    static Dotenv dotenv = Dotenv.configure().load();
    static String stripeApikey = dotenv.get("STRIPE_PUBLIC_KEY");
    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;


    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserService userService) {
        this.subscriptionRepository = subscriptionRepository;
        this.userService = userService;
        Stripe.apiKey = stripeApikey;
    }

    public Optional<TiSubscription> getSubscriptionById(int id) {
        return subscriptionRepository.getSubscriptionById(id);
    }

    public void insertSubscription(String stripeCustomerId, Subscription stripeSubscription) throws Exception {
        Optional<TiSubscription> subscription = getSubscriptionById(Integer.getInteger(stripeSubscription.getId()));
        if (subscription.isPresent()) {
            updateSubscription(subscription.get().getId(), new UpdateSubscriptionDto(stripeSubscription));
        }

        Optional<User> user = userService.getByStripeCustomerId(stripeCustomerId);
        if (user.isEmpty()) {
            throw new Exception(String.format("User with stripe customer id %s doesn't exist", stripeCustomerId));
        }

        int userId = user.get().getId();

        cancelOldSubscription(userId);

        userService.updateBossMode(userId, true);

        HashMap<String, Object> data = new HashMap<>();
        data.put("user_id", userId);
        data.put("stripe_subscription_id", stripeSubscription.getId());
        data.put("subscription_item_id", stripeSubscription.getItems().getData().getFirst().getId());
        data.put("cancel_at_period_end", stripeSubscription.getCancelAtPeriodEnd());
        data.put("current_period_start", new Date(stripeSubscription.getCurrentPeriodStart()));
        data.put("current_period_end", new Date(stripeSubscription.getCurrentPeriodEnd()));
        data.put("productId", stripeSubscription.getItems().getData().getFirst().getPlan().getProduct());
        data.put("planId", stripeSubscription.getItems().getData().getFirst().getPlan().getId());
        data.put("status", stripeSubscription.getStatus());
        data.put("recurring_type", stripeSubscription.getItems().getData().getFirst().getPrice().getRecurring().getInterval());

        subscriptionRepository.createSubscription(data);
    }

    public void updateSubscription(int stripeSubscriptionId, UpdateSubscriptionDto updateSubscriptionDto) throws Exception {
        Optional<TiSubscription> subscription = getSubscriptionById(stripeSubscriptionId);
        if (subscription.isEmpty()) {
            throw new Exception("Payment already made for this month");
        }

        updateBossMode(updateSubscriptionDto, subscription.get());

        HashMap<String, Object> data = new HashMap<>();
        data.put("status", updateSubscriptionDto.getStatus());
        data.put("cancel_at_period_end", updateSubscriptionDto.getCancelAtPeriodEnd());
        data.put("current_period_start", updateSubscriptionDto.getCurrentPeriodStart());
        data.put("current_period_end", updateSubscriptionDto.getCancelAtPeriodEnd());

        subscriptionRepository.updateSubscription(subscription.get().getId(), data);
    }

    private void cancelOldSubscription(int userId) {
        subscriptionRepository.updateStatusByUserId(userId, "canceled");
    }

    private void updateBossMode(UpdateSubscriptionDto updateSubscriptionDto, TiSubscription subscription) throws Exception {
        String stripeCustomerId = updateSubscriptionDto.getCustomerId();
        Optional<User> user = userService.getByStripeCustomerId(stripeCustomerId);
        if (user.isEmpty()) {
            throw new Exception(String.format("User with stripe customer id %s doesn't exist", stripeCustomerId));
        }

        int userId = user.get().getId();

        long remainingDate = Duration.between(subscription.getCurrentPeriodEnd().toInstant(), subscription.getCreatedAt().toInstant()).toDays();

        userService.updateBossMode(userId, remainingDate > 0);
    }

    public Optional<TiSubscription> getSubscriptionUserId(int userId) throws StripeException {
        return subscriptionRepository.getSubscriptionUserId(userId);
    }

    public void cancelSubscription(String stripeSubscriptionId) throws StripeException {
        Subscription resource = Subscription.retrieve(stripeSubscriptionId);
        SubscriptionUpdateParams params = SubscriptionUpdateParams.builder()
                .setCancelAtPeriodEnd(true)
                .build();

        resource.update(params);
    }
}
