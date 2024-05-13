package dev.mayur.userservicetestfinal.controllers;

import dev.mayur.userservicetestfinal.dtos.LoginRequestDto;
import dev.mayur.userservicetestfinal.dtos.LogoutRequestDto;
import dev.mayur.userservicetestfinal.dtos.SignUpRequestDto;
import dev.mayur.userservicetestfinal.dtos.UserDto;
import dev.mayur.userservicetestfinal.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

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


}
