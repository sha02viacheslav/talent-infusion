package com.talent.infusion.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCheckoutSessionDto {
    int userId;
    String productId;
    boolean tryAgain;
}
