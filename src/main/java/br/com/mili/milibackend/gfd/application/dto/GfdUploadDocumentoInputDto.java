package br.com.mili.milibackend.gfd.application.dto;

import br.com.mili.milibackend.shared.infra.aws.dto.AttachmentDto;
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
public class GfdUploadDocumentoInputDto {
    private String usuario;
    private Integer codUsuario;
    private Integer id;

    private List<GfdDocumentoDto> listGfdDocumento;
    private GfdTipoDocumentoDto gfdTipoDocumento;


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
    public static class GfdDocumentoDto {
        AttachmentDto base64File;
        LocalDate dataEmissao;
    }
}
