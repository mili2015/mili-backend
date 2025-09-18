package br.com.mili.milibackend.gfd.application.usecases.GfdCategoriaDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdCategoriaDocumento.GfdCategoriaDocumentoCreateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdCategoriaDocumento.GfdCategoriaDocumentoCreateOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdCategoriaDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoContratacao;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoFornecedor;
import br.com.mili.milibackend.gfd.domain.usecases.gfdCategoriaDocumento.CreateGfdCategoriaDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdCategoriaDocumentoRepository;
import br.com.mili.milibackend.gfd.infra.repository.GfdTipoContratacaoRepository;
import br.com.mili.milibackend.gfd.infra.repository.GfdTipoFornecedorRepository;
import br.com.mili.milibackend.gfd.shared.TiposGfdCategoriaDocumentoEnum;
import br.com.mili.milibackend.shared.exception.types.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdCategoriaDocumentoCodeException.GFD_CATEGORIA_DOCUMENTO_NOME_DA_CONTRATACAO;

@Service
@RequiredArgsConstructor
public class CreateGfdCategoriaDocumentoUseCaseImpl implements CreateGfdCategoriaDocumentoUseCase {
    private final GfdCategoriaDocumentoRepository gfdCategoriaDocumentoRepository;
    private final GfdTipoFornecedorRepository gfdTipoFornecedorRepository;
    private final GfdTipoContratacaoRepository gfdTipoContratacaoRepository;

    @Override
    @Transactional
    public GfdCategoriaDocumentoCreateOutputDto execute(GfdCategoriaDocumentoCreateInputDto inputDto) {
        var gfdCategoriaDocumento = GfdCategoriaDocumento.builder()
                .nome(inputDto.getNome())
                .tipo(inputDto.getTipo())
                .build();

        var gfdCategoriaDocumentoCreated = gfdCategoriaDocumentoRepository.save(gfdCategoriaDocumento);

        var gfdTipoContratacao = GfdTipoContratacao.builder()
                .descricao(inputDto.getNomeContratacao())
                .categoriaDocumento(gfdCategoriaDocumentoCreated)
                .build();

        if (inputDto.getTipo().equals(TiposGfdCategoriaDocumentoEnum.FUNCIONARIO.getDescricao())) {
            if (inputDto.getNomeContratacao() == null) {
                throw new BadRequestException(GFD_CATEGORIA_DOCUMENTO_NOME_DA_CONTRATACAO.getMensagem()
                        , GFD_CATEGORIA_DOCUMENTO_NOME_DA_CONTRATACAO.getCode());
            }

            gfdTipoContratacaoRepository.save(gfdTipoContratacao);
        }

        if (inputDto.getTipo().equals(TiposGfdCategoriaDocumentoEnum.FORNECEDOR.getDescricao())) {
            var gfdTipoFornecedor =  GfdTipoFornecedor.builder()
                    .descricao(inputDto.getNome())
                    .categoriaDocumento(gfdCategoriaDocumentoCreated)
                    .descricao(inputDto.getNome())
                    .build();

            gfdTipoFornecedorRepository.save(gfdTipoFornecedor);
        }

        return GfdCategoriaDocumentoCreateOutputDto.builder()
                .id(gfdCategoriaDocumentoCreated.getId())
                .nomeContratacao(inputDto.getNomeContratacao())
                .nome(gfdCategoriaDocumentoCreated.getNome())
                .tipo(gfdCategoriaDocumentoCreated.getTipo())
                .build();
    }
}
