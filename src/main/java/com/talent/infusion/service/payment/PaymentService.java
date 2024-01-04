package com.talent.infusion.service.payment;

import com.talent.infusion.dto.CreatePaymentDto;
import com.talent.infusion.entiry.payment.Payment;
import com.talent.infusion.repository.payment.PaymentRepository;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Optional<Payment> getPaymentIntentId(String intentId) {
        return paymentRepository.getPaymentIntentId(intentId);
    }

    public Payment insertPayment(int userId, CreatePaymentDto createPaymentDto) {
        return paymentRepository.insertPayment(userId, createPaymentDto);
    }
}
