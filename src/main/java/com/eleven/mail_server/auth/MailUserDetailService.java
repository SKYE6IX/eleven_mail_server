package com.eleven.mail_server.auth;

import com.eleven.mail_server.auth.entity.UserEntity;
import com.eleven.mail_server.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MailUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByUserName(username).orElse(null);

        if(user != null){
            return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
        }else {
         throw new UsernameNotFoundException("User isn't on our database!");
        }
    }
}
