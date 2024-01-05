package com.talent.infusion.entiry.user;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("ti_users")
public class User extends Model {

    public Integer getId() {
        return getInteger("id");
    }

    public String getEmail() {
        return getString("email");
    }

    public String getPassword() {
        return getString("password");
    }

    public String getFirstName() {
        return getString("first_name");
    }

    public String getLastName() {
        return getString("last_name");
    }

    public String getCompanyName() {
        return getString("company_name");
    }

    public Boolean getBossMode() {
        return getBoolean("boss_mode");
    }

    public String getStripeCheckoutSessionId() {
        return getString("stripe_checkout_session_id");
    }

    public String getUserType() {
        return getString("user_type");
    }

    public Boolean getIsChild() {
        return getBoolean("is_child");
    }

    public String getParentUserId() {
        return getString("parent_user_id");
    }

    public void setEmail(String email) {
        setString("email", email);
    }

    public void setPassword(String password) {
        setString("password", password);
    }

    public void setUserType(String userType) {
        setString("user_type", userType);
    }

    public void setIsChild(Boolean isChild) {
        setBoolean("is_child", isChild);
    }

    public void setParentId(String parentId) {
        setString("parent_user_id", parentId);
    }
}
