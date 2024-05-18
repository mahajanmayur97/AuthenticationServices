package dev.mayur.userservicetestfinal.controllers;

import dev.mayur.userservicetestfinal.dtos.*;
import dev.mayur.userservicetestfinal.models.SessionStatus;
import dev.mayur.userservicetestfinal.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }
//-------------------------------------------------------------------------------------
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignUpRequestDto request){
        UserDto userDto = authService.signUp(request.getEmail(), request.getPassword());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
//--------------------------------------------------------------------------------------

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto request) {
        return authService.login(request.getEmail(), request.getPassword());
    }
//--------------------------------------------------------------------------------------

        @PostMapping("/logout")
        public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto request){
            return authService.logout(request.getToken(), request.getUserId());
    }
//--------------------------------------------------------------------------------------
       @PostMapping("/validate")
       public ResponseEntity<SessionStatus> validateToken(@RequestBody ValidateTokenRequestDto request){
//           System.out.println(request.getToken());
//           System.out.println(request.getUserId());
           SessionStatus sessionStatus = this.authService.validate(request.getToken(), request.getUserId());

           return new ResponseEntity<>(sessionStatus, HttpStatus.OK);
       }
//--------------------------------------------------------------------------------------
@PostMapping("/validate1")
public ResponseEntity<Boolean> validateToken1(@RequestBody String token){
    SessionStatus sessionStatus = this.authService.validate1(token);
    return new ResponseEntity<>(sessionStatus == SessionStatus.ACTIVE, HttpStatus.OK);
 }
}
