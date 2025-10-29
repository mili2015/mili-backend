package br.com.mili.milibackend.gfd.application.dto.manager.documentos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GfdMVerificarDocumentosInputDto {
    private Integer codUsuario;
    private Integer idFuncionario;
    private Integer id;
    private boolean isAnalista = false;

    private String setor;

    private LocalDate periodo;
}
