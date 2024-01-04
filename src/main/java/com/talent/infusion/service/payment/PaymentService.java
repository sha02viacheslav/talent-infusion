package com.talent.infusion.service.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.talent.infusion.dto.CreateCheckoutSessionDto;
import com.talent.infusion.dto.CreatePaymentDto;
import com.talent.infusion.entiry.payment.Payment;
import com.talent.infusion.repository.payment.PaymentRepository;
import io.github.cdimascio.dotenv.Dotenv;

import javax.inject.Singleton;
import java.util.*;

@Singleton
public class PaymentService {
    static Dotenv dotenv = Dotenv.configure().load();
    static String frontendUrl = dotenv.get("FRONTEND_URL");
    static String stripeApikey = dotenv.get("STRIPE_PUBLIC_KEY");
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        Stripe.apiKey = stripeApikey;
    }

    public Optional<Payment> getPaymentIntentId(String intentId) {
        return paymentRepository.getPaymentIntentId(intentId);
    }

    public Payment insertPayment(int userId, CreatePaymentDto createPaymentDto) {
        return paymentRepository.insertPayment(userId, createPaymentDto);
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
                .addAllPaymentMethodType(List.of(
                        SessionCreateParams.PaymentMethodType.CARD))
                .setAllowPromotionCodes(true)
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .build();

        return Session.create(createParams);
    }
}
