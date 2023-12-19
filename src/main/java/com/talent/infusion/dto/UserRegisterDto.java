package com.talent.infusion.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDto {
    String email;
    String password;
    String user_type;
    Boolean is_child;
    String parent_id;
}
