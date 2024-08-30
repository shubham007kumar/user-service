package dev.shubham.userservice.security.models;

import dev.shubham.userservice.models.Role;
import org.springframework.security.core.GrantedAuthority;

public class CustomGrantedAuthority implements GrantedAuthority {
    private String authority;
    public CustomGrantedAuthority(Role role){
        this.authority = role.getValue();
    }
    @Override
    public String getAuthority() {
        return authority;
    }
}
