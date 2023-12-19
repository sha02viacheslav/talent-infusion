package com.talent.infusion.service.user;

import com.talent.infusion.dto.UserLoginDto;
import com.talent.infusion.dto.UserRegisterDto;
import com.talent.infusion.entiry.user.User;
import com.talent.infusion.repository.user.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

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
}
