package dev.mayur.userservicetestfinal.services;

import dev.mayur.userservicetestfinal.dtos.UserDto;
import dev.mayur.userservicetestfinal.models.Role;
import dev.mayur.userservicetestfinal.models.Session;
import dev.mayur.userservicetestfinal.models.SessionStatus;
import dev.mayur.userservicetestfinal.models.User;
import dev.mayur.userservicetestfinal.repositories.SessionRepository;
import dev.mayur.userservicetestfinal.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.*;

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
        System.out.println(email);
         System.out.println(password);
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
//        System.out.println("inside /auth/login function");
        if(userOptional.isEmpty()){
            return null;
        }
        User user = userOptional.get();

        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Wrong username password");
        }
//        System.out.println("after sccessfull decrption of password ");
        String token = RandomStringUtils.randomAlphanumeric(30);

        MacAlgorithm alg = Jwts.SIG.HS256; //or HS384 or HS256
        SecretKey key = alg.key().build();

        Map<String, Object> jsonForJwt = new HashMap<>();
        jsonForJwt.put("email", user.getEmail());
//        jsonForJwt.put("roles", user.getRoles());
        jsonForJwt.put("createdAt", new Date());
        jsonForJwt.put("expiryAt", new Date(LocalDate.now().plusDays(3).toEpochDay()));

        token = Jwts.builder()
                .claims(jsonForJwt)
                .signWith(key, alg)
                .compact();

        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);

        UserDto userDto = UserDto.from(user);

//        Map<String,String> headers = new HashMap<>();
//        headers.put(HttpHeaders.SET_COOKIE, token);

        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + token);

        ResponseEntity<UserDto> response = new ResponseEntity<>(userDto, headers, HttpStatus.OK);

 //       response.getHeaders().add(HttpHeaders.SET_COOKIE,token);
 //       System.out.println(response.getHeaders().toString());
        return response;
    }

//----------------------------------------------------------------------------------------

    public ResponseEntity<Void> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return null;
        }

        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.ENDED);
        sessionRepository.save(session);
        return ResponseEntity.ok().build();
    }
//-----------------------------------------------------------------------------------

    public SessionStatus validate(String token, Long userId){
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return SessionStatus.ENDED;
        }

        Session session = sessionOptional.get();

        if (!session.getSessionStatus().equals(SessionStatus.ACTIVE)) {
            return SessionStatus.ENDED;
        }
        Jws<Claims> claimsJws = Jwts.parser()
                .build()
                .parseSignedClaims(token);

        String email = (String) claimsJws.getPayload().get("email");
        List<Role> roles = (List<Role>) claimsJws.getPayload().get("roles");
        Date createdAt = (Date) claimsJws.getPayload().get("createdAt");

        if (createdAt.before(new Date())) {
            return SessionStatus.ENDED;
        }

        return SessionStatus.ACTIVE;
    }
//---------------------------------------------------------------

    public SessionStatus validate1(String token){
        System.out.println("Inside Us-validate");
        Optional<Session> sessionOptional = sessionRepository.findByToken(token);

        if (sessionOptional.isEmpty()) {
            return SessionStatus.ENDED;
        }

        Session session = sessionOptional.get();

        if (!session.getSessionStatus().equals(SessionStatus.ACTIVE)) {
            return SessionStatus.ENDED;
        }
        return SessionStatus.ACTIVE;
    }
}
