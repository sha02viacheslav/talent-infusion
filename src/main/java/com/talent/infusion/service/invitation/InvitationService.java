package com.talent.infusion.service.invitation;


import com.talent.infusion.dto.CreateInvitationDto;
import com.talent.infusion.entiry.invitation.Invitation;
import com.talent.infusion.repository.invitation.InvitationRepository;
import org.javalite.activejdbc.LazyList;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class InvitationService {
    private final InvitationRepository invitationRepository;

    public InvitationService(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    public Optional<Invitation> getInvitationById(int id) {
        return invitationRepository.getInvitationById(id);
    }

    public Optional<Invitation> getInvitationByEmail(String email) {
        return invitationRepository.getInvitationByEmail(email);
    }

    public LazyList<Invitation> getInvitationByParentUserId(int parentUserId) {
        return invitationRepository.getInvitationByParentUserId(parentUserId);
    }

    public Invitation createInvitation(CreateInvitationDto createInvitationDto) {
        return invitationRepository.createInvitation(createInvitationDto);
    }

    public Optional<Invitation> deleteInvitation(int id) {
        return invitationRepository.deleteInvitation(id);
    }
}
