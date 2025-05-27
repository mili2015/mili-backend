package br.com.mili.milibackend.shared.infra.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CustomUserDetails implements UserDetails {

    private List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    protected Integer idUser;
    protected String username;
    protected String password;

    public Integer getIdUser() {
        return idUser;
    }

    protected void addAuthority(String auth) {
        authorities.add(new SimpleGrantedAuthority(auth));
    }

    public final List<String> getListAuthorities()
    {
        return authorities.stream().map(SimpleGrantedAuthority::toString).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }

    @Override
    public String getPassword()
    {
        return new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public String getUsername() {
        return username;
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

    @Override
    public String toString() {
        return "CustomUserDetails{" +
                "authorities=" + authorities +
                ", idUser=" + idUser +
                ", username='" + username + '\'' +
                '}';
    }
}
