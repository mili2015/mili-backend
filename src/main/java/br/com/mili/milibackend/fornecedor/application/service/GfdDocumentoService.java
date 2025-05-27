package br.com.mili.milibackend.fornecedor.application.service;

import br.com.mili.milibackend.fornecedor.application.dto.gfdDocumento.*;
import br.com.mili.milibackend.fornecedor.domain.entity.GfdDocumento;
import br.com.mili.milibackend.fornecedor.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.fornecedor.domain.specification.GfdDocumentoSpecification;
import br.com.mili.milibackend.fornecedor.infra.repository.gfdDocumento.GfdDocumentoRepository;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdDocumentoService;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.shared.infra.aws.S3ServiceImpl;
import br.com.mili.milibackend.shared.infra.aws.StorageFolderEnum;
import br.com.mili.milibackend.shared.infra.aws.dto.AttachmentDto;
import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.shared.page.pagination.PageBaseImpl;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.mili.milibackend.fornecedor.adapter.exception.GfdDocumentoCodeException.FORNECEDOR_DOCUMENTO_NAO_ENCONTRADO;

@Service
public class GfdDocumentoService implements IGfdDocumentoService {
    private final GfdDocumentoRepository gfdDocumentoRepository;
    private final ModelMapper modelMapper;
    private final S3ServiceImpl s3Service;
    private final Gson gson;

    public GfdDocumentoService(GfdDocumentoRepository gfdDocumentoRepository, ModelMapper modelMapper, S3ServiceImpl s3Service, Gson gson) {
        this.gfdDocumentoRepository = gfdDocumentoRepository;
        this.modelMapper = modelMapper;
        this.s3Service = s3Service;
        this.gson = gson;
    }

    @Override
    public GfdDocumentoCreateOutputDto create(GfdDocumentoCreateInputDto inputDto) {
        var gfdDocumento = modelMapper.map(inputDto.getGfdDocumentoDto(), GfdDocumento.class);

        var gfdDocumentoCreated = gfdDocumentoRepository.save(gfdDocumento);

        var attachmentDtoModified = new AttachmentDto(inputDto.getBase64File(), gfdDocumento.getNomeArquivo());
        var json = gson.toJson(attachmentDtoModified);

        s3Service.upload(StorageFolderEnum.GFD, json);

        return modelMapper.map(gfdDocumentoCreated, GfdDocumentoCreateOutputDto.class);
    }

    @Override
    public List<FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto> findLatestDocumentsGroupedByTipoAndFornecedorId(Integer fornecedorId) {
        var listGfdDocumento = gfdDocumentoRepository.findLatestDocumentsGroupedByTipoAndFornecedorId(fornecedorId);

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

    @Override
    public GfdDocumentoUpdateOutputDto update(GfdDocumentoUpdateInputDto inputDto) {
        var fornecedor = gfdDocumentoRepository.findById(inputDto.getId())
                .orElseThrow(() ->
                        new NotFoundException(FORNECEDOR_DOCUMENTO_NAO_ENCONTRADO.getMensagem(), FORNECEDOR_DOCUMENTO_NAO_ENCONTRADO.getCode())
                );

        modelMapper.map(inputDto, fornecedor);

        fornecedor.setStatus(GfdDocumentoStatusEnum.valueOf(inputDto.getStatus()));

        gfdDocumentoRepository.save(fornecedor);

        return modelMapper.map(fornecedor, GfdDocumentoUpdateOutputDto.class);
    }

    @Override
    public GfdDocumentoDownloadOutputDto download(GfdDocumentoDownloadInputDto inputDto) {
        var fornecedor = gfdDocumentoRepository.findById(inputDto.getId())
                .orElseThrow(() ->
                        new NotFoundException(FORNECEDOR_DOCUMENTO_NAO_ENCONTRADO.getMensagem(), FORNECEDOR_DOCUMENTO_NAO_ENCONTRADO.getCode())
                );

        // recupera no s3
        var url = s3Service.getPresignedUrl(StorageFolderEnum.GFD, fornecedor.getNomeArquivo());

        return new GfdDocumentoDownloadOutputDto(fornecedor.getId(), url);
    }

}
