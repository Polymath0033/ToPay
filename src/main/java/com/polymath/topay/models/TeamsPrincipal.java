package com.polymath.topay.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class TeamsPrincipal implements UserDetails {
    private final Teams teams;

    public TeamsPrincipal(Teams teams) {
        this.teams = teams;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return teams.isActive();
    }


    @Override
    public boolean isEnabled() {
        return teams.isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_"+teams.getRole().name()));
    }

    @Override
    public String getPassword() {
        return teams.getPassword();
    }

    @Override
    public String getUsername() {
        return teams.getEmail();
    }
}
