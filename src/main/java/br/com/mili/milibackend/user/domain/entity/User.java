package br.com.mili.milibackend.user.domain.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "CT_USUARIO")

public class User implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CTUSU_CODIGO")
    private Integer id;

    @Column(name = "CTUSU_NOME")
    private String username;

    @Column(name = "CTUSU_SENHA")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "IDSETOR")
    private String idSetor;

    @Transient
    private List<String> authorities;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public String getIdSetor() {
        return idSetor;
    }

    public void setIdSetor(String idSetor) {
        this.idSetor = idSetor;
    }

    @Override
    public String toString() {
        return "User{id=" + id +", nome=" + username + "}";
    }

    public String getEmailOfuscado() {

        String [] emailArray = email.split("@");

        if (emailArray.length <= 0)
            return "sem email";

        String mail = emailArray[0];
        String first = mail.substring(0, 1);
        String last = mail.substring(mail.length() - 1, mail.length());

        return first + "***" + last + "@" + emailArray[1];
    }
}
