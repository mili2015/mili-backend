package br.com.mili.milibackend.academia.adapter.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademiaExternalMatricularUserResponse {
    private Integer courseId;
    private String status;
    private String code;
    private String message;
}
