package com.talent.infusion.service.auth;

import com.talent.infusion.entiry.user.User;
import com.talent.infusion.jwt.JWT;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
    public String signPayload(User user) {
        JWT jwt = new JWT();
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", user.getId());
        payload.put("email", user.getEmail());
        payload.put("firstName", user.getFirstName());
        payload.put("lastName", user.getLastName());
        payload.put("companyName", user.getCompanyName());
        payload.put("bossMode", user.getBossMode());
        payload.put("userType", user.getUserType());
        payload.put("isChild", user.getIsChild());
        payload.put("parentUserId", user.getParentUserId());
        return jwt.generateJWT(String.valueOf(user.getId()), payload);
    }
}
