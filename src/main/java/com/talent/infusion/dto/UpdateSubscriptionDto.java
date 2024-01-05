package com.talent.infusion.dto;

import com.stripe.model.Subscription;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateSubscriptionDto {
    String status;
    boolean cancel_at_period_end;
    Date current_period_start;
    Date current_period_end;
    String customerId;

    public UpdateSubscriptionDto(Subscription subscription) {
        this.status = subscription.getStatus();
        this.cancel_at_period_end = subscription.getCancelAtPeriodEnd();
        this.current_period_start = new Date(subscription.getCurrentPeriodStart());
        this.current_period_end = new Date(subscription.getCurrentPeriodEnd());
        this.customerId = subscription.getCustomer();
    }


    public boolean getCancelAtPeriodEnd() {
        return cancel_at_period_end;
    }

    public Date getCurrentPeriodStart() {
        return current_period_start;
    }

    public Date getCurrentPeriodEnd() {
        return current_period_end;
    }
}
