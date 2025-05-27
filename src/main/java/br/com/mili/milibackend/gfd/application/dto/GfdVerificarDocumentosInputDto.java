package br.com.mili.milibackend.gfd.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GfdVerificarDocumentosInputDto {
    private Integer codUsuario;
    private Integer id;
}
