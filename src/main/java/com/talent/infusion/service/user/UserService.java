package com.talent.infusion.service.user;

import com.talent.infusion.dto.UpdateUserDto;
import com.talent.infusion.dto.UserRegisterDto;
import com.talent.infusion.entiry.user.User;
import com.talent.infusion.repository.user.UserRepository;
import org.javalite.activejdbc.LazyList;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Optional;
import javax.inject.Singleton;

@Singleton
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User sanitizeUser(User user) {
        user.setPassword(null);
        return user;
    }

    public Optional<User> getUserById(int id) {
        return userRepository.getUserById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public User createUser(UserRegisterDto userRegisterDto) {
        String password = userRegisterDto.getPassword();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        userRegisterDto.setPassword(hashedPassword);
        return userRepository.createUser(userRegisterDto);
    }

    public Optional<User> updateUser(int id, UpdateUserDto updateUserDto) {
        HashMap<String, Object> data = new HashMap<>();
        String companyName = updateUserDto.getCompany_name();
        data.put("first_name", updateUserDto.getFirst_name());
        data.put("last_name", updateUserDto.getLast_name());
        data.put("photo", updateUserDto.getPhoto());
        data.put("stripe_customer_id", updateUserDto.getStripe_customer_id());
        data.put("stripe_checkout_session_id", updateUserDto.getStripe_checkout_session_id());
        data.put("stripe_billing_portal_session_id", updateUserDto.getStripe_billing_portal_session_id());
        data.put("boss_mode", updateUserDto.getBoss_mode());
        if (!companyName.isEmpty()) {
            data.put("company_name", companyName);
        }

        Optional<User> updatedUser = userRepository.updateUser(id, data);

        if (updatedUser.isPresent() && !companyName.isEmpty()) {
            LazyList<User> childUsers = userRepository.getUsersByParentUserId(id);

            HashMap<String, Object> companyNameDto = new HashMap<>();
            companyNameDto.put("company_name", companyName);

            for (User child : childUsers) {
                userRepository.updateUser(child.getId(), companyNameDto);
            }
        }

        return updatedUser;
    }
}
