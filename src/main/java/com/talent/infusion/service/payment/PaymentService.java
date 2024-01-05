package com.talent.infusion.service.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.talent.infusion.dto.CreateCheckoutSessionDto;
import com.talent.infusion.dto.CreatePaymentDto;
import com.talent.infusion.dto.UpdateSubscriptionDto;
import com.talent.infusion.entiry.payment.Payment;
import com.talent.infusion.entiry.user.User;
import com.talent.infusion.repository.payment.PaymentRepository;
import com.talent.infusion.service.subscription.SubscriptionService;
import com.talent.infusion.service.user.UserService;
import io.github.cdimascio.dotenv.Dotenv;

import javax.inject.Singleton;
import java.util.*;

@Singleton
public class PaymentService {
    static Dotenv dotenv = Dotenv.configure().load();
    static String frontendUrl = dotenv.get("FRONTEND_URL");
    static String stripeApikey = dotenv.get("STRIPE_PUBLIC_KEY");
    static String stripeEndpointSecret = dotenv.get("STRIPE_ENDPOINT_SECRET");
    private final PaymentRepository paymentRepository;
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public PaymentService(PaymentRepository paymentRepository, UserService userService, SubscriptionService subscriptionService) {
        this.paymentRepository = paymentRepository;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        Stripe.apiKey = stripeApikey;
    }

    public Optional<Payment> getPaymentIntentId(String intentId) {
        return paymentRepository.getPaymentIntentId(intentId);
    }

    public Payment insertPayment(CreatePaymentDto createPaymentDto) throws Exception {
        String email = createPaymentDto.getCustomerEmail();
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isEmpty()) {
            throw new Exception(String.format("User with email %s doesn't exist", email));
        }

        Optional<Payment> payment = getPaymentIntentId(createPaymentDto.getPaymentIntent());
        if (payment.isPresent()) {
            throw new Exception("Payment already made for this month");
        }

        return paymentRepository.insertPayment(user.get().getId(), createPaymentDto);
    }

    public Session createCheckoutSession(String email, CreateCheckoutSessionDto createCheckoutSessionDto) throws StripeException {
        String successUrl = String.format("%s/signup?payment=success", frontendUrl);
        String cancelUrl = String.format("%s/signin?payment=failed", frontendUrl);
        if (createCheckoutSessionDto.isTryAgain()) {
            successUrl = String.format("%s/account/settings/payment-method?payment=success", frontendUrl);
            cancelUrl = String.format("%s/account/settings/payment-method?payment=failed", frontendUrl);
        }

        String userId = Integer.toString(createCheckoutSessionDto.getUserId());

        SessionCreateParams createParams = new SessionCreateParams.Builder()
                .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.AUTO)
                .setCustomerEmail(email)
                .putMetadata("user_id", userId)
                .setClientReferenceId(userId)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPrice(createCheckoutSessionDto.getProductId())
                        .setQuantity(1L)
                        .build())
                .addAllPaymentMethodType(List.of(SessionCreateParams.PaymentMethodType.CARD, SessionCreateParams.PaymentMethodType.US_BANK_ACCOUNT))
                .setAllowPromotionCodes(true)
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .build();

        return Session.create(createParams);
    }

    public boolean verifyPayments(String payload, String sigHeader) throws Exception {
        Event event = Webhook.constructEvent(payload, sigHeader, stripeEndpointSecret);

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject;

        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            return false;
        }

        switch (event.getType()) {
            case "customer.created": {
                Customer customer = (Customer) stripeObject;
                userService.updateStripeCustomerId(customer.getEmail(), customer.getId());
            }
            case "customer.subscription.created": {
                assert stripeObject instanceof Subscription;
                Subscription subscription = (Subscription) stripeObject;
                subscriptionService.insertSubscription(subscription.getCustomer(), subscription);
            }
            case "customer.subscription.updated":
            case "customer.subscription.deleted": {
                Subscription subscription = (Subscription) stripeObject;
                subscriptionService.updateSubscription(Integer.getInteger(subscription.getId()), new UpdateSubscriptionDto(subscription));
            }
            case "invoice.payment_succeeded":
            case "invoice.payment_failed": {
                assert stripeObject instanceof Invoice;
                CreatePaymentDto paymentDto = new CreatePaymentDto((Invoice) stripeObject);
                insertPayment(paymentDto);
            }
        }
        return true;
    }
}
