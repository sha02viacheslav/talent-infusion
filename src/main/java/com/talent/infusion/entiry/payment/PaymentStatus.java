package com.talent.infusion.entiry.payment;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PAID("paid"), UNPAID("unpaid"), OPEN("open");
    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }
}