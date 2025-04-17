package br.com.mili.milibackend.infra.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Roles {
    private int systemId;
    private List<String> authorities;

    public void addAuthority(String authority) {
        this.authorities.add(authority);
    }
}
