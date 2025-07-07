package com.polymath.topay.services;

import com.polymath.topay.models.Teams;
import com.polymath.topay.models.TeamsPrincipal;
import com.polymath.topay.repositories.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final TeamRepository teamRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return teamRepository.findByEmail(username).map(TeamsPrincipal::new).orElseThrow(()->new UsernameNotFoundException("User not found with email: "+username));
    }
}
