package com.talent.infusion.entiry.invitation;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("ti_invitations")
public class Invitation extends Model {

    public Integer getId() {
        return getInteger("id");
    }

    public void setEmail(String email) {
        setString("email", email);
    }

    public void setName(String name) {
        setString("name", name);
    }

    public void setStatus(InvitationStatus status) {
        setString("status", status.getValue());
    }
    public void setParentId(int parentId) {
        setInteger("parent_user_id", parentId);
    }
}
