package br.com.mili.milibackend.infra.security.service;

import br.com.mili.milibackend.infra.security.model.CustomUserPrincipal;
import br.com.mili.milibackend.infra.security.model.Roles;
import br.com.mili.milibackend.user.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService
{
    @Autowired
    private UserService userBo;

    @Override
    public UserDetails loadUserByUsername(String username)
    {
        var userFounded = userBo.findByUsername(username);
        userFounded.setPassword(null);



        return new CustomUserPrincipal(userFounded);
    }
}
