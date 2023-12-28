package com.talent.infusion.entiry.invitation;

import lombok.Getter;

@Getter
public enum InvitationStatus {
    ACTIVE("active"), PENDING("pending");
    private final String value;

    InvitationStatus(String value) {
        this.value = value;
    }
}
