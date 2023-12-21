package com.talent.infusion.service.auth;

import lombok.Getter;

@Getter
public enum CheckVerificationCodeResult {
    INVALID(-1), EXPIRED(0), VALID(1);
    private final int result;

    CheckVerificationCodeResult(int result) {
        this.result = result;
    }

}
