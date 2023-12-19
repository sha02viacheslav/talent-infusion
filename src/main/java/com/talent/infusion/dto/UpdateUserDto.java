package com.talent.infusion.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDto {
    String first_name;
    String last_name;
    String company_name;
    String photo;
    String stripe_customer_id;
    String stripe_checkout_session_id;
    String stripe_billing_portal_session_id;
    Boolean boss_mode;
}
