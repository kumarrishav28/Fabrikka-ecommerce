package com.userservice.user.security;

import com.userservice.user.entity.Roles;
import com.userservice.user.entity.User;
import com.userservice.user.repository.userRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    public static Map<String, Object> userCache = new ConcurrentHashMap<>();

    private final userRepository userRepository;

    public CustomUserDetailsService(userRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserEmail(userEmail);

        if (user.isPresent()) {
            // Cache the user details
            userCache.put("user", user.get());
            return new org.springframework.security.core.userdetails.User(user.get().getUserEmail(),
                    user.get().getPassword(),
                    mapRolesToAuthorities(user.get().getRoles()));
        }else{
            throw new UsernameNotFoundException("Invalid username or password.");
        }
    }

    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Collection <Roles> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
