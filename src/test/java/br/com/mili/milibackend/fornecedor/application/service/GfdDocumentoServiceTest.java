package br.com.mili.milibackend.fornecedor.application.service;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.*;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumento;
import br.com.mili.milibackend.gfd.infra.dto.GfdDocumentoResumoDto;
import br.com.mili.milibackend.gfd.infra.repository.gfdDocumento.GfdDocumentoRepository;
import br.com.mili.milibackend.gfd.application.service.GfdDocumentoService;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.shared.infra.aws.S3ServiceImpl;
import br.com.mili.milibackend.shared.infra.aws.StorageFolderEnum;
import br.com.mili.milibackend.shared.infra.aws.dto.AttachmentDto;
import br.com.mili.milibackend.shared.page.pagination.MyPageable;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GfdDocumentoServiceTest {

    @Mock
    private GfdDocumentoRepository gfdDocumentoRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private S3ServiceImpl s3Service;

    @Mock
    private Gson gson;

    @InjectMocks
    private GfdDocumentoService gfdDocumentoService;



    @Test
    void test_FindLatestDocumentsGroupedByTipoAndFornecedorId__deve_retornar_lista_mapeada() {
        var gfdDocumento = new GfdDocumento();
        gfdDocumento.setId(1);

        var outputDto = new FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto();

        when(gfdDocumentoRepository.findLatestDocumentsByPeriodoAndFornecedorOrFuncionario(10, null, null, null))
                .thenReturn(List.of(gfdDocumento));

        when(modelMapper.map(gfdDocumento, FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto.class))
                .thenReturn(outputDto);

        var result = gfdDocumentoService.findLatestDocumentsGroupedByTipoAndFornecedorId(10, null, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(outputDto, result.get(0));
    }

/*
    @Test
    void test_GetAll__deve_retornar_pagina_com_filtros() {
        var inputDto = new GfdDocumentoGetAllInputDto();
        inputDto.setCtforCodigo(5);
        inputDto.setPageable(new MyPageable(1, 10));
        inputDto.setNomeArquivo("doc");
        inputDto.setStatus("CONFORME");
        inputDto.setTipoDocumentoId(3);

        var gfdResumoDto = new GfdDocumentoResumoDto();
        gfdResumoDto.setId(1);
        gfdResumoDto.setNomeArquivo("doc-exemplo.pdf");

        var pageDto = new PageImpl<>(List.of(gfdResumoDto), PageRequest.of(0, 10), 1);

        when(gfdDocumentoRepository.getAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(pageDto);

        var outputDto = new GfdDocumentoGetAllOutputDto();
        outputDto.setId(gfdResumoDto.getId());
        outputDto.setNomeArquivo(gfdResumoDto.getNomeArquivo());

        when(modelMapper.map(gfdResumoDto, GfdDocumentoGetAllOutputDto.class))
                .thenReturn(outputDto);

        var result = gfdDocumentoService.getAll(inputDto);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertSame(outputDto, result.getContent().get(0));
    }

    @Test
    void test_Update__deve_atualizar_quando_encontrar_documento() {
        var inputDto = new GfdDocumentoUpdateInputDto();
        inputDto.setId(1);
        inputDto.setDataEmissao(LocalDate.of(2024, 5, 20));
        inputDto.setDataValidade(LocalDate.of(2025, 5, 20));
        inputDto.setStatus("CONFORME");
        inputDto.setObservacao("Observação atualizada");

        var gfdDocumento = new GfdDocumento();
        gfdDocumento.setId(1);
        gfdDocumento.setDataEmissao(LocalDate.of(2023, 5, 20));
        gfdDocumento.setDataValidade(LocalDate.of(2024, 5, 20));
        gfdDocumento.setStatus(GfdDocumentoStatusEnum.CONFORME);
        gfdDocumento.setObservacao("Observação antiga");

        gfdDocumento.setCtforCodigo(123);
        gfdDocumento.setTipoDocumento("Tipo A");
        gfdDocumento.setNomeArquivo("arquivo_antigo.pdf");
        gfdDocumento.setNomeArquivoPath("/path/antigo");
        gfdDocumento.setTamanhoArquivo(1024);
        gfdDocumento.setDataCadastro(LocalDate.of(2022, 5, 20));
        gfdDocumento.setTipoArquivo("PDF");
        gfdDocumento.setUsuario("usuario1");
        gfdDocumento.setMaquina("maquina1");

        var gfdTipoDocumento = new GfdTipoDocumento();
        gfdTipoDocumento.setId(10);
        gfdTipoDocumento.setNome("Documento Tipo X");
        gfdTipoDocumento.setDiasValidade(365);
        gfdDocumento.setGfdTipoDocumento(gfdTipoDocumento);

        var outputDto = new GfdDocumentoUpdateOutputDto();
        outputDto.setId(1);
        outputDto.setCtforCodigo(123);
        outputDto.setTipoDocumento("Tipo A");
        outputDto.setNomeArquivo("arquivo_antigo.pdf");
        outputDto.setNomeArquivoPath("/path/antigo");
        outputDto.setTamanhoArquivo(1024);
        outputDto.setDataCadastro(LocalDate.of(2022, 5, 20));
        outputDto.setDataEmissao(inputDto.getDataEmissao());
        outputDto.setDataValidade(inputDto.getDataValidade());
        outputDto.setTipoArquivo("PDF");
        outputDto.setObservacao(inputDto.getObservacao());
        outputDto.setStatus(GfdDocumentoStatusEnum.CONFORME);

        var gfdTipoDocumentoDto = new GfdDocumentoUpdateOutputDto.GfdTipoDocumentoDto();
        gfdTipoDocumentoDto.setId(10);
        gfdTipoDocumentoDto.setNome("Documento Tipo X");
        gfdTipoDocumentoDto.setDiasValidade(365);
        outputDto.setGfdTipoDocumento(gfdTipoDocumentoDto);

        when(gfdDocumentoRepository.findById(1)).thenReturn(Optional.of(gfdDocumento));


        doAnswer(invocation -> {
            GfdDocumentoUpdateInputDto source = invocation.getArgument(0);
            GfdDocumento target = invocation.getArgument(1);

            target.setDataEmissao(source.getDataEmissao());
            target.setDataValidade(source.getDataValidade());
            target.setObservacao(source.getObservacao());
            return null;
        }).when(modelMapper).map(inputDto, gfdDocumento);

        when(gfdDocumentoRepository.save(gfdDocumento)).thenReturn(gfdDocumento);

        when(modelMapper.map(gfdDocumento, GfdDocumentoUpdateOutputDto.class)).thenReturn(outputDto);

        var result = gfdDocumentoService.update(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(outputDto.getId(), result.getId());
        assertEquals(outputDto.getCtforCodigo(), result.getCtforCodigo());
        assertEquals(outputDto.getTipoDocumento(), result.getTipoDocumento());
        assertEquals(outputDto.getNomeArquivo(), result.getNomeArquivo());
        assertEquals(outputDto.getNomeArquivoPath(), result.getNomeArquivoPath());
        assertEquals(outputDto.getTamanhoArquivo(), result.getTamanhoArquivo());
        assertEquals(outputDto.getDataCadastro(), result.getDataCadastro());
        assertEquals(outputDto.getDataEmissao(), result.getDataEmissao());
        assertEquals(outputDto.getDataValidade(), result.getDataValidade());
        assertEquals(outputDto.getTipoArquivo(), result.getTipoArquivo());
        assertEquals(outputDto.getObservacao(), result.getObservacao());
        assertEquals(outputDto.getStatus(), result.getStatus());

        assertNotNull(result.getGfdTipoDocumento());
        assertEquals(outputDto.getGfdTipoDocumento().getId(), result.getGfdTipoDocumento().getId());
        assertEquals(outputDto.getGfdTipoDocumento().getNome(), result.getGfdTipoDocumento().getNome());
        assertEquals(outputDto.getGfdTipoDocumento().getDiasValidade(), result.getGfdTipoDocumento().getDiasValidade());

        assertEquals(GfdDocumentoStatusEnum.CONFORME, gfdDocumento.getStatus());
    }

    @Test
    void test_Update__deve_lancar_not_found_exception_quando_documento_nao_existir() {
        var inputDto = new GfdDocumentoUpdateInputDto();
        inputDto.setId(999);

        when(gfdDocumentoRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> gfdDocumentoService.update(inputDto));
    }
*/

    @Test
    void test_Download__deve_retornar_url_quando_documento_existir() {
        var inputDto = new GfdDocumentoDownloadInputDto();
        inputDto.setId(1);

        var gfdDocumento = new GfdDocumento();
        gfdDocumento.setId(1);
        gfdDocumento.setNomeArquivo("arquivo.pdf");

        when(gfdDocumentoRepository.findById(1)).thenReturn(Optional.of(gfdDocumento));
        when(s3Service.getPresignedUrl(StorageFolderEnum.GFD, "arquivo.pdf")).thenReturn("http://url.com");

        var result = gfdDocumentoService.download(inputDto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("http://url.com", result.getLink());
    }

    @Test
    void test_Download__deve_lancar_not_found_exception_quando_documento_nao_existir() {
        var inputDto = new GfdDocumentoDownloadInputDto();
        inputDto.setId(999);

        when(gfdDocumentoRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> gfdDocumentoService.download(inputDto));
    }
}
