package com.talent.infusion.repository.user;


import com.talent.infusion.dto.UserRegisterDto;
import com.talent.infusion.entiry.user.User;
import org.javalite.activejdbc.DB;

import java.util.Optional;

public class UserRepository {

    private final DB db;

    public UserRepository(DB db) {
        this.db = db;
    }

    public Optional<User> getUserById(int id) {
        return db.withDb(() -> Optional.ofNullable(User.findById(id)));
    }

    public Optional<User> getUserByEmail(String email) {
        return db.withDb(() -> Optional.ofNullable(User.findFirst("email = ?", email)));
    }

    public User createUser(UserRegisterDto userRegisterDto) {
        return db.withDb(() -> {
            User user = new User();
            user.setEmail(userRegisterDto.getEmail());
            user.setPassword(userRegisterDto.getPassword());
            user.setUserType(userRegisterDto.getUser_type());
            user.setIsChild(userRegisterDto.getIs_child());
            user.setParentId(userRegisterDto.getParent_id());
            user.saveIt();
            return user;
        });
    }
}
