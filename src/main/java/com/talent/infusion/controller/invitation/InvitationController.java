package com.talent.infusion.controller.invitation;

import com.talent.infusion.dto.CreateInvitationDto;
import com.talent.infusion.entiry.invitation.Invitation;
import com.talent.infusion.entiry.user.User;
import com.talent.infusion.service.invitation.InvitationService;
import com.talent.infusion.service.user.UserService;
import com.talent.infusion.utils.mailchimp.MailChimp;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import lombok.extern.slf4j.Slf4j;
import org.javalite.activejdbc.LazyList;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
public class InvitationController {
    static int MAX_INVITATIONS = 5;
    private InvitationService invitationService;
    private UserService userService;

    public InvitationController(InvitationService invitationService, UserService userService) {

        this.invitationService = invitationService;
        this.userService = userService;
    }

    public Handler create = ctx -> {
        JavalinJackson jackson = new JavalinJackson();
        HashMap<String, Object> resultMap = new HashMap<>();
        CreateInvitationDto createInvitationDto;

        try {
            createInvitationDto = jackson.fromJsonString(ctx.body(), CreateInvitationDto.class);
            String email = createInvitationDto.getEmail();
            int parentUserId = createInvitationDto.getParentUserId();
            Optional<Invitation> invitation = invitationService.getInvitationByEmail(email);

            if (invitation.isPresent()) {
                resultMap.put("success", false);
                resultMap.put("message", String.format("User with email: %s already invited", email));
                ctx.status(HttpStatus.BAD_REQUEST).json(resultMap);
                return;
            }

            Optional<User> user = userService.getUserByEmail(email);

            if (user.isPresent()) {
                resultMap.put("success", false);
                resultMap.put("message", String.format("User with email: %s already exist", email));
                ctx.status(HttpStatus.BAD_REQUEST).json(resultMap);
                return;
            }

            LazyList<Invitation> totalInvitations = invitationService.getInvitationByParentUserId(parentUserId);

            if (totalInvitations.size() >= MAX_INVITATIONS) {
                resultMap.put("success", false);
                resultMap.put("message", String.format("Not more than %d invitee allowed", MAX_INVITATIONS));
                ctx.status(HttpStatus.BAD_REQUEST).json(resultMap);
                return;
            }

            Invitation newInvitation = invitationService.createInvitation(createInvitationDto);

            MailChimp mailChimp = new MailChimp();
            mailChimp.sendInvitationEmail(email, createInvitationDto.getName());

            resultMap.put("success", true);
            resultMap.put("invitation", newInvitation);
            ctx.status(HttpStatus.OK).json(resultMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
            ctx.status(HttpStatus.SERVICE_UNAVAILABLE).json(resultMap);
        }
    };
}
