package br.com.mili.milibackend.gfd.application.service;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.*;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.gfd.infra.specification.GfdDocumentoSpecification;
import br.com.mili.milibackend.gfd.infra.repository.gfdDocumento.GfdDocumentoRepository;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdDocumentoService;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.shared.infra.aws.S3ServiceImpl;
import br.com.mili.milibackend.shared.infra.aws.StorageFolderEnum;
import br.com.mili.milibackend.shared.infra.aws.dto.AttachmentDto;
import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.shared.page.pagination.PageBaseImpl;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdDocumentoCodeException.GFD_DOCUMENTO_DELETE_PERMISSAO_INVALIDA;
import static br.com.mili.milibackend.gfd.adapter.exception.GfdDocumentoCodeException.GFD_DOCUMENTO_NAO_ENCONTRADO;

@RequiredArgsConstructor
@Service
public class GfdDocumentoService implements IGfdDocumentoService {
    private final GfdDocumentoRepository gfdDocumentoRepository;
    private final ModelMapper modelMapper;
    private final S3ServiceImpl s3Service;

    @Override
    public List<FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto> findLatestDocumentsGroupedByTipoAndFornecedorId(Integer fornecedorId, Integer idFuncionario) {
        var listGfdDocumento = gfdDocumentoRepository.findLatestDocumentsGroupedByTipoAndFornecedorId(fornecedorId, idFuncionario);

        return listGfdDocumento.stream()
                .map(gfdDocumento ->
                        modelMapper.map(gfdDocumento, FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto.class)).toList();
    }

    @Override
    public MyPage<GfdDocumentoGetAllOutputDto> getAll(GfdDocumentoGetAllInputDto inputDto) {
        Specification<GfdDocumento> spec = Specification.where(null);

        //filtra por fornecedor
        if (inputDto.getCtforCodigo() != null) {
            spec = spec.and(GfdDocumentoSpecification.filtroPorFornecedor(inputDto.getCtforCodigo()));
        }

        //filtra por funcionario
        spec = filtroFuncionario(inputDto, spec);


        //filtra por nome do arquivo
        if (inputDto.getNomeArquivo() != null) {
            spec = spec.and(GfdDocumentoSpecification.filtroNomeArquivoContem(inputDto.getNomeArquivo()));
        }

        //filtra por status
        if (inputDto.getStatus() != null) {
            spec = spec.and(GfdDocumentoSpecification.filtroStatus(GfdDocumentoStatusEnum.valueOf(inputDto.getStatus())));
        }

        //filtra por tipo
        if (inputDto.getTipoDocumentoId() != null) {
            spec = spec.and(GfdDocumentoSpecification.filtroPorTipo(inputDto.getTipoDocumentoId()));
        }

        //filtra por range dataCadastro
        spec = spec.and(GfdDocumentoSpecification.filtroRangeDataCadastro(inputDto.getDataCadastroInic(), inputDto.getDataCadastroFinal()));

        //filtra por range dataValidade
        spec = spec.and(GfdDocumentoSpecification.filtroRangeDataValidade(inputDto.getDataValidadeInic(), inputDto.getDataValidadeFinal()));

        //filtra por range dataEmissao
        spec = spec.and(GfdDocumentoSpecification.filtroRangeDataEmissao(inputDto.getDataEmissaoInic(), inputDto.getDataEmissaoFinal()));

        //page
        var pageNumber = inputDto.getPageable().getPage() > 0 ? inputDto.getPageable().getPage() - 1 : 0;
        var pageSize = inputDto.getPageable().getSize() > 0 ? inputDto.getPageable().getSize() : 20;

        var pageGfdDocumentos = gfdDocumentoRepository.getAll(spec, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id")));

        List<GfdDocumentoGetAllOutputDto> gfdDocumentoGetAllOutputDto = pageGfdDocumentos.getContent().stream()
                .map(gfdDocumento -> modelMapper.map(gfdDocumento, GfdDocumentoGetAllOutputDto.class))
                .toList();

        return new PageBaseImpl<>(gfdDocumentoGetAllOutputDto, pageGfdDocumentos.getPageable().getPageNumber() + 1, pageGfdDocumentos.getSize(), pageGfdDocumentos.getTotalElements()) {
        };
    }

    private static Specification<GfdDocumento> filtroFuncionario(GfdDocumentoGetAllInputDto inputDto, Specification<GfdDocumento> spec) {
        var funcionario = inputDto.getFuncionario();

        if (funcionario != null) {
            if (funcionario.getId() != null) {
                spec = spec.and(GfdDocumentoSpecification.filtroFornecedorId(funcionario.getId()));
            }

            // filtra por nome
            if (funcionario.getNome() != null) {
                spec = spec.and(GfdDocumentoSpecification.filtroFornecedorNome(funcionario.getNome()));
            }

            // filtra por cpf
            if (funcionario.getCpf() != null) {
                spec = spec.and(GfdDocumentoSpecification.filtroFornecedorCpf(funcionario.getCpf()));
            }
        }

        return spec;
    }

    @Override
    public GfdDocumentoUpdateOutputDto update(GfdDocumentoUpdateInputDto inputDto) {
        GfdDocumento gfdDocumento = gfdDocumentoRepository.findById(inputDto.getId())
                .orElseThrow(() ->
                        new NotFoundException(GFD_DOCUMENTO_NAO_ENCONTRADO.getMensagem(), GFD_DOCUMENTO_NAO_ENCONTRADO.getCode())
                );

        modelMapper.map(inputDto, gfdDocumento);

        gfdDocumento.setStatus(GfdDocumentoStatusEnum.valueOf(inputDto.getStatus()));

        gfdDocumentoRepository.save(gfdDocumento);

        return modelMapper.map(gfdDocumento, GfdDocumentoUpdateOutputDto.class);
    }

    @Override
    public void delete(GfdDocumentoDeleteInputDto inputDto) {
        var gfdDocumento = gfdDocumentoRepository.findById(inputDto.getId())
                .orElseThrow(() ->
                        new NotFoundException(GFD_DOCUMENTO_NAO_ENCONTRADO.getMensagem(), GFD_DOCUMENTO_NAO_ENCONTRADO.getCode())
                );

        boolean isNotStatusEnviado = !gfdDocumento.getStatus().equals(GfdDocumentoStatusEnum.ENVIADO);

        if (isNotStatusEnviado) {
            throw new NotFoundException(GFD_DOCUMENTO_DELETE_PERMISSAO_INVALIDA.getMensagem(), GFD_DOCUMENTO_DELETE_PERMISSAO_INVALIDA.getCode());
        }

        gfdDocumentoRepository.delete(gfdDocumento);
    }

    @Override
    public GfdDocumentoDownloadOutputDto download(GfdDocumentoDownloadInputDto inputDto) {
        var fornecedor = gfdDocumentoRepository.findById(inputDto.getId())
                .orElseThrow(() ->
                        new NotFoundException(GFD_DOCUMENTO_NAO_ENCONTRADO.getMensagem(), GFD_DOCUMENTO_NAO_ENCONTRADO.getCode())
                );

        // recupera no s3
        var url = s3Service.getPresignedUrl(StorageFolderEnum.GFD, fornecedor.getNomeArquivo());

        return new GfdDocumentoDownloadOutputDto(fornecedor.getId(), url);
    }



}
