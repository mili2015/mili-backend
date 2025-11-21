package br.com.mili.milibackend.academia.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcademiaGetCourseByUserOutputDto {
    private Integer id;
    private String title;
    private String link;
}
