package dev.shubham.userservice.controllers;

import dev.shubham.userservice.dtos.*;
import dev.shubham.userservice.dtos.ResponseStatus;
import dev.shubham.userservice.exceptions.UserPresentException;
import dev.shubham.userservice.models.Token;
import dev.shubham.userservice.models.User;
import dev.shubham.userservice.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class usercontroller {
   private UserService userService;
   public usercontroller(UserService userService){
       this.userService = userService;
   }

   @PostMapping("/signup")
    public SignUpResponseDto signUp(@RequestBody SignUpRequestDto signUpRequestDto) throws UserPresentException {
        User user = userService.signUp(
                signUpRequestDto.getName(),
                signUpRequestDto.getEmail(),
                signUpRequestDto.getPassword());
        SignUpResponseDto signUpResponseDto = new SignUpResponseDto();
        signUpResponseDto.setResponseStatus(ResponseStatus.SUCCESS);
        signUpResponseDto.setUser(user);
        return signUpResponseDto;
    }
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto){
        Token token = userService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setResponseStatus(ResponseStatus.SUCCESS);
        loginResponseDto.setToken(token);

        return loginResponseDto;
    }
    @PostMapping("/logout")
    public LogoutResponseDto logout(@RequestBody LogoutResquestDto logoutResquestDto ){
       LogoutResponseDto logoutResponseDto = null;
       try {
           userService.logout(logoutResquestDto.getToken());
            logoutResponseDto = new LogoutResponseDto();
           logoutResponseDto.setResponseStatus(ResponseStatus.SUCCESS);
       }catch (Exception e){

       }
       return  logoutResponseDto;
    }

    @GetMapping("/validate/{token}")
    public UserDto validate(@PathVariable String token){
        User user = userService.validate(token);

        return UserDto.fromUser(user);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") Long id){
        System.out.println("received getUserDedails Api request");
      return UserDto.fromUser(userService.getUser(id));
    }
}
