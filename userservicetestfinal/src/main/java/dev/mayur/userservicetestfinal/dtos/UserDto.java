package dev.mayur.userservicetestfinal.dtos;

import dev.mayur.userservicetestfinal.models.Role;
import dev.mayur.userservicetestfinal.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class UserDto {
    private String email;
    private Set<Role> roles = new HashSet<>();

    public static UserDto from(User user){
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
//        userDto.setRoles(user.getRoles());

        return userDto;
    }
}
