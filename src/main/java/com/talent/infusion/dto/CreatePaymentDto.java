package com.talent.infusion.dto;

import com.stripe.model.Invoice;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePaymentDto {
    String customer_email;
    String payment_intent;
    String status;

    public CreatePaymentDto(Invoice invoice) {
        this.customer_email = invoice.getCustomerEmail();
        this.payment_intent = invoice.getPaymentIntent();
        this.status = invoice.getStatus();
    }


    public String getCustomerEmail() {
        return customer_email;
    }

    public String getPaymentIntent() {
        return payment_intent;
    }
}
