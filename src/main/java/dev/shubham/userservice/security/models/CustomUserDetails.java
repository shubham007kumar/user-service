package dev.shubham.userservice.security.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.shubham.userservice.models.Role;
import dev.shubham.userservice.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JsonDeserialize
public class CustomUserDetails implements UserDetails {
    private List<GrantedAuthority> authorities;
    private String password;
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public CustomUserDetails(){};

    public CustomUserDetails(User user){
       this.password = user.getPassword();
       this.username = user.getEmail();
       this.credentialsNonExpired = true;
       this.accountNonExpired = true;
       this.accountNonLocked = true;
       this.enabled = true;

       this.authorities = new ArrayList<>();
       for(Role role:user.getRoles()){
           authorities.add(new CustomGrantedAuthority(role));
       }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
