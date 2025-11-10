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
public class AcademiaGetUserByEmailOutputDto {
    private Integer id;
    private String nome;
    private String url;
    private String descricao;
    private List<LinkItem> courses;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LinkItem {
        private String href;
    }
}
