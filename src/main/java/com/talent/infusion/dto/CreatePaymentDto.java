package com.talent.infusion.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePaymentDto {
    String customer_email;
    String payment_intent;
    String status;

    public String getCustomerEmail() {
        return customer_email;
    }

    public String getPaymentIntent() {
        return payment_intent;
    }
}
