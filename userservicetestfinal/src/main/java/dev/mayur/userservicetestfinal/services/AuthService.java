package dev.mayur.userservicetestfinal.services;

import dev.mayur.userservicetestfinal.dtos.UserDto;
import dev.mayur.userservicetestfinal.models.Session;
import dev.mayur.userservicetestfinal.models.User;
import dev.mayur.userservicetestfinal.repositories.SessionRepository;
import dev.mayur.userservicetestfinal.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    @Autowired
    public AuthService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository,SessionRepository sessionRepository){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }
//---------------------------------------------------------------------------------
     public UserDto signUp(String email, String password) {
         User user = new User();
         user.setEmail(email);
         user.setPassword(bCryptPasswordEncoder.encode(password));

         User savedUser = userRepository.save(user);

         UserDto userDto = UserDto.from(savedUser);

         return userDto;
     }
//------------------------------------------------------------------------------------


    public ResponseEntity<UserDto> login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        System.out.println("inside /auth/login function");
        if(userOptional.isEmpty()){
            return null;
        }

        User user = userOptional.get();

        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Wrong username password");
        }
        System.out.println("after sccessfull decrption of password ");
        String token = RandomStringUtils.randomAlphanumeric(30);

        Session session = new Session();
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);

        UserDto userDto = UserDto.from(user);

//        Map<String,String> headers = new HashMap<>();
//        headers.put(HttpHeaders.SET_COOKIE, token);

        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + token);

        ResponseEntity<UserDto> response = new ResponseEntity<>(userDto, headers, HttpStatus.OK);


 //         response.getHeaders().add(HttpHeaders.SET_COOKIE,token);

        System.out.println(response.getHeaders().toString());
        return response;
    }

//----------------------------------------------------------------------------------------
}
