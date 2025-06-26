package br.com.mili.milibackend.gfd.application.dto;

import br.com.mili.milibackend.shared.infra.aws.dto.AttachmentDto;
import br.com.mili.milibackend.shared.validation.annotation.ValidaIntervaloData;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GfdMUploadDocumentoInputDto {
    private String usuario;
    private Integer codUsuario;
    private Integer id;

    @Valid
    private List<GfdDocumentoDto> listGfdDocumento;
    private GfdTipoDocumentoDto gfdTipoDocumento;
    private GfdFuncionarioDto funcionario;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GfdFuncionarioDto {
        private Integer id;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GfdTipoDocumentoDto {
        private Integer id;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ValidaIntervaloData(inicio = "dataEmissao", fim = "dataValidade", message = "A data de validade não pode ser menor que a data de emissão")
    public static class GfdDocumentoDto {
        AttachmentDto base64File;
        LocalDate dataEmissao;
        LocalDate dataValidade;
    }
}
