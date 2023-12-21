package com.talent.infusion.model;

import lombok.Getter;

import java.util.Date;

@Getter
public record VerificationCodeInfo(String code, Date expiration) {

}
