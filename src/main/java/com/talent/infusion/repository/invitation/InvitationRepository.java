package com.talent.infusion.repository.invitation;

import com.talent.infusion.dto.CreateInvitationDto;
import com.talent.infusion.entiry.invitation.Invitation;
import com.talent.infusion.entiry.invitation.InvitationStatus;
import org.javalite.activejdbc.DB;
import org.javalite.activejdbc.LazyList;

import java.util.Optional;

public class InvitationRepository {

    private final DB db;

    public InvitationRepository(DB db) {
        this.db = db;
    }

    public Optional<Invitation> getInvitationById(int id) {
        return db.withDb(() -> Optional.ofNullable(Invitation.findById(id)));
    }

    public Optional<Invitation> getInvitationByEmail(String email) {
        return db.withDb(() -> Optional.ofNullable(Invitation.findFirst("email = ?", email)));
    }

    public LazyList<Invitation> getInvitationByParentUserId(int parentUserId) {
        return db.withDb(() -> Invitation.find("parent_user_id = ?", parentUserId));
    }

    public Invitation createInvitation(CreateInvitationDto createInvitationDto) {
        return db.withDb(() -> {
            Invitation invitation = new Invitation();
            invitation.setEmail(createInvitationDto.getEmail());
            invitation.setName(createInvitationDto.getName());
            invitation.setParentId(createInvitationDto.getParentUserId());
            invitation.setStatus(InvitationStatus.PENDING);
            invitation.saveIt();
            return invitation;
        });
    }

    public Optional<Invitation> deleteInvitation(int id) {
        return db.withDb(() -> {
            Invitation invitation = Invitation.findById(id);

            if (invitation == null) {
                return Optional.empty();
            }

            invitation.delete();

            return Optional.of(invitation);
        });
    }
}
