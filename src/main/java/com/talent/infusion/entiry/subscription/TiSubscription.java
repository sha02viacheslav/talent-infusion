package com.talent.infusion.entiry.subscription;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.Date;

@Table("ti_subscriptions")
public class TiSubscription extends Model {

    public Integer getId() {
        return getInteger("id");
    }

    public Date getCurrentPeriodEnd() {
        return getDate("current_period_end");
    }

    public Date getCreatedAt() {
        return getDate("created_at");
    }
}
