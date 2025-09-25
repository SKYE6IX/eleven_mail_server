package com.eleven.mail_server.auth;


import com.eleven.mail_server.auth.dto.AuthRequest;
import com.eleven.mail_server.auth.dto.AuthResponse;
import com.eleven.mail_server.auth.entity.UserEntity;
import com.eleven.mail_server.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){
        try{
            authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

        } catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtUtil.generateToken(authRequest.getUsername());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest authRequest){

//        Check if User with the username already exist.
        UserEntity user = userRepository.findByUserName(authRequest.getUsername())
                .orElse(null);
        if(user != null){
            String responseMessage = "A user with this username " + authRequest.getUsername() +
                    " already exist.";
            return ResponseEntity.status(HttpStatus.FOUND).body(responseMessage);
        }
        //  Continue to create a new user if there is no existing User
        passwordEncoder = new BCryptPasswordEncoder(16);

        String harshPassword = passwordEncoder.encode(authRequest.getPassword());

        UserEntity newUser = new UserEntity(null, authRequest.getUsername(),harshPassword);

        userRepository.save(newUser);

        String responseMessage = "New user with userName of " + newUser.getUsername() + " successfully created";

        return ResponseEntity.ok(responseMessage);
    }
}
