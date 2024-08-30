package dev.shubham.userservice.services;

import dev.shubham.userservice.exceptions.UserPresentException;
import dev.shubham.userservice.models.Token;
import dev.shubham.userservice.models.User;

public interface UserService {
    User signUp(String name, String email, String password) throws UserPresentException;
    Token login(String email, String password);

    void logout(String token);

    User validate(String token);

    User getUser(Long id);
}
