package br.com.mili.milibackend.user;


import br.com.mili.milibackend.infra.security.model.CustomUserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

public abstract class UserAuhentication {

    public static CustomUserPrincipal getCustomUser()
    {
        Object userObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userObj instanceof User)
            return buildCustomUser((User)userObj);

        return (CustomUserPrincipal) userObj;
    }

    private static CustomUserPrincipal buildCustomUser(User userObj) {

        br.com.mili.milibackend.user.domain.entity.User user = new br.com.mili.milibackend.user.domain.entity.User();
        user.setUsername(userObj.getUsername());
        user.setPassword(userObj.getPassword());
        List<String> listAuth = new ArrayList<>();
        userObj.getAuthorities().forEach( auth -> listAuth.add(auth.getAuthority()));
        user.setAuthorities(listAuth);

        return new CustomUserPrincipal(user);
    }



}
