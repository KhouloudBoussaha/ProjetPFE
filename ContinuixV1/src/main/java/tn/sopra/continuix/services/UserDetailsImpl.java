package tn.sopra.continuix.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tn.sopra.continuix.entities.Role;
import tn.sopra.continuix.entities.Users;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {
    private final Long id;
    private final String email;
    private final String username;
    private final String password;
    private final Role role; // Store Role enum directly
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String email, String username, String password, Role role,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(Users users) {
        GrantedAuthority authority = users.getRole() != null ?
                new SimpleGrantedAuthority("ROLE_" + users.getRole().name()) :
                new SimpleGrantedAuthority("ROLE_USER"); // Default role if none provided
        return new UserDetailsImpl(
                users.getId(),
                users.getEmail(),
                users.getUsername(),
                users.getPassword(),
                users.getRole(),
                Collections.singletonList(authority)
        );
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
    public String getRealUsername() {
        return username; // le pseudo réel
    }

    @Override
    public String getUsername() {
        return email; // Use email as username for consistency with UserDetailsService
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}