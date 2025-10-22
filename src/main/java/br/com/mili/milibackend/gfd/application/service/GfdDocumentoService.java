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

import java.time.LocalDate;
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
    public List<FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto> findLatestDocumentsGroupedByTipoAndFornecedorId(Integer fornecedorId, Integer idFuncionario, LocalDate periodo, String setor) {
        var listGfdDocumento = gfdDocumentoRepository.findLatestDocumentsByPeriodoAndFornecedorOrFuncionario(fornecedorId, idFuncionario, periodo, setor);

        return listGfdDocumento.stream()
                .map(gfdDocumento ->
                        modelMapper.map(gfdDocumento, FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto.class)).toList();
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
