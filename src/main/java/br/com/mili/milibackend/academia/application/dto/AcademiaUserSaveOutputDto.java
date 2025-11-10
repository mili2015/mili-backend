package br.com.mili.milibackend.academia.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcademiaUserSaveOutputDto {
    private Integer id;
    private String email;
    private String username;
    private String nome;
}
