package br.com.mili.milibackend.academia.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcademiaUserUpdateInputDto {
    private Integer userId;
    private String email;
    private String name;
    private String firstName;
    private String lastName;
}
