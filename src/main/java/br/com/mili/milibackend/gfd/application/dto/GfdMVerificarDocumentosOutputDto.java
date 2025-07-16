package br.com.mili.milibackend.gfd.application.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class GfdMVerificarDocumentosOutputDto {
        List<DocumentoDto> documentos = new ArrayList<>();
        String title;


        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        @EqualsAndHashCode
       public static class DocumentoDto {
        private String status;
        private String nome;
        private Integer idTipoDocumento;
    }
}
