package br.com.mili.milibackend.fornecedor.application.dto.gfdDocumento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GfdDocumentoDownloadOutputDto {
    private Integer id;
    private String link;
}
