package dev.shubham.userservice.dtos;

import dev.shubham.userservice.models.Token;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private Token token;
    private ResponseStatus responseStatus;
}
