package com.rj.sunbase.Service;

import com.rj.sunbase.Model.User;

public interface UserService {

    public User registerUser(User user);

    public User authenticateUser(String username, String password);

}
