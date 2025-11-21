package br.com.mili.milibackend.academia.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcademiaUserEnrollInputDto {
    private Integer userId;
    private List<Integer> idCursos;
}
