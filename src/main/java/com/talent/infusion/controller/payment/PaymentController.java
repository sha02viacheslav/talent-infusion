package com.talent.infusion.controller.payment;

import com.stripe.model.checkout.Session;
import com.talent.infusion.dto.CreateCheckoutSessionDto;
import com.talent.infusion.dto.CreatePaymentDto;
import com.talent.infusion.entiry.payment.Payment;
import com.talent.infusion.entiry.user.User;
import com.talent.infusion.service.payment.PaymentService;
import com.talent.infusion.service.user.UserService;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
public class PaymentController {
    private PaymentService paymentService;
    private UserService userService;

    public PaymentController(PaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }

    public Handler create = ctx -> {
        JavalinJackson jackson = new JavalinJackson();
        HashMap<String, Object> resultMap = new HashMap<>();
        CreatePaymentDto createPaymentDto;

        try {
            createPaymentDto = jackson.fromJsonString(ctx.body(), CreatePaymentDto.class);
            String email = createPaymentDto.getCustomerEmail();

            Optional<User> user = userService.getUserByEmail(email);

            if (user.isEmpty()) {
                resultMap.put("success", false);
                resultMap.put("message", String.format("User with email %s doesn't exist", email));
                ctx.status(HttpStatus.BAD_REQUEST).json(resultMap);
                return;
            }

            Optional<Payment> payment = paymentService.getPaymentIntentId(createPaymentDto.getPaymentIntent());

            if (payment.isPresent()) {
                resultMap.put("success", false);
                resultMap.put("message", "Payment already made for this month");
                ctx.status(HttpStatus.BAD_REQUEST).json(resultMap);
                return;
            }

            Payment newPayment = paymentService.insertPayment(user.get().getId(), createPaymentDto);

            resultMap.put("success", true);
            resultMap.put("payment", newPayment);
            ctx.status(HttpStatus.OK).json(resultMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
            ctx.status(HttpStatus.SERVICE_UNAVAILABLE).json(resultMap);
        }
    };

    public Handler createCheckoutSession = ctx -> {
        JavalinJackson jackson = new JavalinJackson();
        HashMap<String, Object> resultMap = new HashMap<>();
        CreateCheckoutSessionDto createCheckoutSessionDto;

        try {
            createCheckoutSessionDto = jackson.fromJsonString(ctx.body(), CreateCheckoutSessionDto.class);
            int userId = createCheckoutSessionDto.getUserId();

            Optional<User> user = userService.getUserById(userId);

            if (user.isEmpty()) {
                resultMap.put("success", false);
                resultMap.put("message", "User not found or userId is not correct");
                ctx.status(HttpStatus.BAD_REQUEST).json(resultMap);
                return;
            }

            Session checkoutSession = paymentService.createCheckoutSession(user.get().getEmail(), createCheckoutSessionDto);

            userService.updateStripeCheckoutSessionId(userId, checkoutSession.getId());

            resultMap.put("success", true);
            resultMap.put("checkoutSessionId", checkoutSession.getId());
            ctx.status(HttpStatus.OK).json(resultMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
            ctx.status(HttpStatus.SERVICE_UNAVAILABLE).json(resultMap);
        }
    };
}
