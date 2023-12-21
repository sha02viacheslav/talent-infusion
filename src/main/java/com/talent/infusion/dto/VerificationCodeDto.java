package com.talent.infusion.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationCodeDto {
    String email;
    String code;
}
