package br.com.mili.milibackend.shared.infra.security.service;

import br.com.mili.milibackend.shared.infra.security.model.CustomUserPrincipal;
import br.com.mili.milibackend.user.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService
{
    private final UserService userBo;

    public CustomUserDetailsService(UserService userBo) {
        this.userBo = userBo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
    {
        var userFounded = userBo.findByUsername(username);
        userFounded.setPassword(null);
        return new CustomUserPrincipal(userFounded);
    }
}
