package com.talent.infusion.repository.user;


import com.talent.infusion.dto.UserRegisterDto;
import com.talent.infusion.entiry.user.User;
import org.javalite.activejdbc.DB;
import org.javalite.activejdbc.LazyList;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
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

    public Optional<User> getByStripeCustomerId(String stripeCustomerId) {
        return db.withDb(() -> Optional.ofNullable(User.findFirst("stripe_customer_id = ?", stripeCustomerId)));
    }

    public Optional<User> getUserByResetToken(String email, String token) {
        return db.withDb(() -> Optional.ofNullable(User.findFirst("email = ? and reset_password_token = ? and reset_password_expires > ?", email, token, new Timestamp(new Date().getTime()))));
    }

    public LazyList<User> getUsersByParentUserId(int parentUserId) {
        return db.withDb(() -> User.find("parent_user_id = ?", parentUserId));
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

    public Optional<User> updateUser(int id, HashMap<String, Object> data) {
        return db.withDb(() -> {
            User user = User.findById(id);
            if (user == null) {
                return Optional.empty();
            }

            for (HashMap.Entry<String, Object> entry : data.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                user.set(key, value);
            }
            user.saveIt();
            return Optional.of(user);
        });
    }

    public void updateUserByEmail(String email, HashMap<String, Object> data) {
        db.withDb(() -> {
            User user = User.findFirst("email = ?", email);
            if (user == null) {
                return Optional.empty();
            }

            for (HashMap.Entry<String, Object> entry : data.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                user.set(key, value);
            }
            user.saveIt();
            return Optional.of(user);
        });
    }

    public boolean deleteUser(int id) {
        return db.withDb(() -> {
            User user = User.findById(id);

            if (user == null) {
                return false;
            }

            return user.delete();
        });
    }
}
