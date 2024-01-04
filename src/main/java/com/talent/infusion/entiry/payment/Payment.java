package com.talent.infusion.entiry.payment;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;


@Table("ti_payments")
public class Payment extends Model {

    public Integer getId() {
        return getInteger("id");
    }

    public void setUserId(int userId) {
        setInteger("user_id", userId);
    }

    public void setIntentId(String intentId) {
        setString("intent_id", intentId);
    }

    public void setStatus(String status) {
        setString("status", status);
    }
}
