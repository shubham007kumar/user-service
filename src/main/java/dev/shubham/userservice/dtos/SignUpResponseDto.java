package dev.shubham.userservice.dtos;

import dev.shubham.userservice.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpResponseDto {
    private User user;
    private ResponseStatus responseStatus;
}
