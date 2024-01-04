package com.talent.infusion.repository.payment;

import com.talent.infusion.dto.CreatePaymentDto;
import com.talent.infusion.entiry.payment.Payment;
import org.javalite.activejdbc.DB;

import java.util.Optional;

public class PaymentRepository {

    private final DB db;

    public PaymentRepository(DB db) {
        this.db = db;
    }

    public Optional<Payment> getPaymentIntentId(String intentId) {
        return db.withDb(() -> Optional.ofNullable(Payment.findFirst("intent_id = ?", intentId)));
    }

    public Payment insertPayment(int userId, CreatePaymentDto createPaymentDto) {
        return db.withDb(() -> {
            Payment payment = new Payment();
            payment.setUserId(userId);
            payment.setIntentId(createPaymentDto.getPaymentIntent());
            payment.setStatus(createPaymentDto.getStatus());
            payment.saveIt();
            return payment;
        });
    }

}
