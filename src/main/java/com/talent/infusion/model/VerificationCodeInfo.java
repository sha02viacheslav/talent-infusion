package com.talent.infusion.model;

import lombok.Getter;

import java.util.Date;

@Getter
public class VerificationCodeInfo {
    private String code;
    private Date expiration;

    public VerificationCodeInfo(String code, Date expiration) {
        this.code = code;
        this.expiration = expiration;
    }

}
