package tn.sopra.continuix.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.sopra.continuix.entities.Users;
import tn.sopra.continuix.repositories.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
@Autowired
    private  UserRepository userRepository;

    @Cacheable
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        System.out.println("Loading user: email=" + users.getEmail() + ", username=" + users.getUsername() + ", role=" + users.getRole());
        return UserDetailsImpl.build(users);
    }
}