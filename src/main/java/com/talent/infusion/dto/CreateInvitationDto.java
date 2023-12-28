package com.talent.infusion.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInvitationDto {
    String email;
    String name;
    int parent_user_id;


    public int getParentUserId() {
        return parent_user_id;
    }
}
