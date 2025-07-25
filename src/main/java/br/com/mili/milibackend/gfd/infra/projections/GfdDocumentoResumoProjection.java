package br.com.mili.milibackend.gfd.infra.projections;

import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;

import java.time.LocalDate;

public interface GfdDocumentoResumoProjection {
    public Integer getId();
    public Integer getCtforCodigo();
    public String getTipoDocumento();
    public String getNomeArquivo();
    public String getNomeArquivoPath();
    public Integer getTamanhoArquivo();
    public LocalDate getDataCadastro();
    public LocalDate getDataValidade();
    public String getTipoArquivo();
    public GfdDocumentoStatusEnum getStatus();

    GfdTipoDocumentoProjection getGfdTipoDocumento();

    interface GfdTipoDocumentoProjection {
        Integer getId();
    }
}
