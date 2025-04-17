package br.com.mili.milibackend.user.infra.repository.projection;

public interface UserFindUserByUsername {
    Integer getId();
    String getUsername();
    String getPassword();
    String getEmail();
    String getIdSetor();
    String getIsHex();
}
