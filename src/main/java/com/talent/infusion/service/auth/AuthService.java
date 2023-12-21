package com.talent.infusion.service.auth;

import com.talent.infusion.entiry.user.User;
import com.talent.infusion.jwt.JWT;
import com.talent.infusion.model.VerificationCodeInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AuthService {
    private final Map<String, VerificationCodeInfo> verificationCodes = new HashMap<>();

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

    private String generateVerificationCode() {
        // Generate a random verification code
        Random random = new Random();
        int codeLength = 6;
        int maxCodeValue = (int) Math.pow(10, codeLength);
        int verificationCode = random.nextInt(maxCodeValue);

        // Format the verification code to have leading zeros if needed
        return String.format("%0" + codeLength + "d", verificationCode);
    }

    private void updateVerificationCode(String email, String verificationCode) {
        int codeExpirationMinutes = 5;
        Date expiration = new Date(new Date().getTime() + 1000 * 60 * codeExpirationMinutes);

        // Update verification codes
        verificationCodes.put(email, new VerificationCodeInfo(verificationCode, expiration));
    }

    public Boolean sendResetPasswordEmail(String email) {
        String verificationCode = generateVerificationCode();
        updateVerificationCode(email, verificationCode);
        // TODO Send email
        return true;
    }

    public CheckVerificationCodeResult checkVerificationCode(String email, String code) {
        VerificationCodeInfo verificationCodeInfo = this.verificationCodes.get(email);

        if (verificationCodeInfo == null) {
            return CheckVerificationCodeResult.INVALID;
        }

        if (verificationCodeInfo.getExpiration().before(new Date())) {
            this.verificationCodes.remove(email);
            return CheckVerificationCodeResult.EXPIRED;
        }

        if (!verificationCodeInfo.getCode().equals(code)) {
            return CheckVerificationCodeResult.INVALID;
        }

        return CheckVerificationCodeResult.VALID;
    }
}
