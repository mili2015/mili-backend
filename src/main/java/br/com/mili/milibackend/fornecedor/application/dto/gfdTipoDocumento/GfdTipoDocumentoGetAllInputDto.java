package br.com.mili.milibackend.fornecedor.application.dto.gfdTipoDocumento;


import br.com.mili.milibackend.fornecedor.domain.entity.GfdTipoDocumentoTipoEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class GfdTipoDocumentoGetAllInputDto{
    private GfdTipoDocumentoTipoEnum tipo;
}
