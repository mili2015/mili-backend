package br.com.mili.milibackend.gfd.application.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class GfdMVerificarDocumentosOutputDto {
        private Integer idTipoDocumento;
        private String nome;
        private String status;
}
