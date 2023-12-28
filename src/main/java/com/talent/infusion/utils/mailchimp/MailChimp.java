package com.talent.infusion.utils.mailchimp;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

@Slf4j
public class MailChimp {
    static Dotenv dotenv = Dotenv.configure().load();
    static String apiKey = dotenv.get("MAILCHIMP_APIKEY");
    static String frontendUrl = dotenv.get("FRONTEND_URL");

    private void sendTemplate(String email, String name, String templateName, String subject, JSONArray globalMergeVarsArray) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://mandrillapp.com/api/1.0/messages/send-template";

        JSONObject jsonRequest = new JSONObject();

        jsonRequest.put("key", apiKey);
        jsonRequest.put("template_name", templateName);
        jsonRequest.put("template_content", new JSONObject());

        JSONObject messageObject = new JSONObject();
        messageObject.put("subject", subject);
        messageObject.put("from_email", "noreply@talentinfusion.io");
        messageObject.put("from_name", "Blavity Support");

        JSONArray toArray = new JSONArray();
        JSONObject toObject = new JSONObject();
        toObject.put("email", email);
        toObject.put("type", "to");
        toArray.put(toObject);

        messageObject.put("to", toArray);

        messageObject.put("global_merge_vars", globalMergeVarsArray);

        jsonRequest.put("message", messageObject);

        String jsonString = jsonRequest.toString();
        RequestBody requestBody = RequestBody.create(jsonString, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                log.error(response.message());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    public void sendResetPasswordLinkEmail(String email, String name, String token) {
        JSONArray globalMergeVarsArray = new JSONArray();
        JSONObject nameObject = new JSONObject();
        nameObject.put("name", "fullname");
        nameObject.put("content", name);
        globalMergeVarsArray.put(nameObject);

        JSONObject tokenObject = new JSONObject();
        nameObject.put("name", "verificationcode");
        nameObject.put("content", token);
        globalMergeVarsArray.put(tokenObject);

        sendTemplate(email, name, "Talent Infusion - Reset Password Link Email", "Verification Code Sent - Action Required", globalMergeVarsArray);
    }

    public void sendResetPasswordConfirmationEmail(String email, String name) {
        JSONArray globalMergeVarsArray = new JSONArray();
        JSONObject nameObject = new JSONObject();
        nameObject.put("name", "fullname");
        nameObject.put("content", name);
        globalMergeVarsArray.put(nameObject);
        sendTemplate(email, name, "Talent Infusion - Reset Password Confirmation", "Reset Password Confirmation", globalMergeVarsArray);
    }

    public void sendInvitationEmail(String email, String name) {
        JSONArray globalMergeVarsArray = new JSONArray();
        JSONObject nameObject = new JSONObject();
        nameObject.put("name", "applink");
        nameObject.put("content", String.format("%s/signup", frontendUrl));
        globalMergeVarsArray.put(nameObject);
        sendTemplate(email, name, "Talent Infusion - Invite a child", "Invitation", globalMergeVarsArray);
    }
}
