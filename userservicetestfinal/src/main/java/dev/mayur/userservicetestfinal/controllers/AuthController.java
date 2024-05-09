package dev.mayur.userservicetestfinal.controllers;

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
}
