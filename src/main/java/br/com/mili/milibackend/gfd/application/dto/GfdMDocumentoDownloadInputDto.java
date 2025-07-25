package br.com.mili.milibackend.gfd.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GfdMDocumentoDownloadInputDto {
    private Integer id;
    private Integer codUsuario;
    private Integer fornecedorId;
}
