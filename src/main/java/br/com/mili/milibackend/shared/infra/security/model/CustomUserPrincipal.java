package br.com.mili.milibackend.shared.infra.security.model;

import br.com.mili.milibackend.user.domain.entity.User;

public class CustomUserPrincipal extends CustomUserDetails
{
    private User user;

    public CustomUserPrincipal(User user) {
        this.user = user;
        this.idUser = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        buildAuthority();
    }

    private void buildAuthority()
    {
        if (user.getAuthorities() == null)
            return;

        for (String auth : user.getAuthorities()) {
            addAuthority(auth);
        }
    }
}
