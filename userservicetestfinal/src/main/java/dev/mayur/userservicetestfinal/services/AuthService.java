package dev.mayur.userservicetestfinal.services;

import dev.mayur.userservicetestfinal.dtos.UserDto;
import dev.mayur.userservicetestfinal.models.User;
import dev.mayur.userservicetestfinal.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;

    @Autowired
    public AuthService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

     public UserDto signUp(String email, String password){
         User user = new User();
         user.setEmail(email);
         user.setPassword(bCryptPasswordEncoder.encode(password));

         User savedUser = userRepository.save(user);

         UserDto userDto = UserDto.from(savedUser);

     return userDto;
     }
}
