package br.com.mili.milibackend.gfd.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GfdMDocumentoDownloadOutputDto {
    private Integer id;
    private String link;
}
