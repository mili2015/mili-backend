package br.com.mili.milibackend.gfd.application.usecases;

import br.com.mili.milibackend.fornecedor.infra.repository.fornecedorRepository.FornecedorRepository;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoCreateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoCreateOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumento;
import br.com.mili.milibackend.gfd.domain.interfaces.FileProcessingService;
import br.com.mili.milibackend.gfd.domain.usecases.CreateDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdTipoDocumentoRepository;
import br.com.mili.milibackend.gfd.infra.repository.gfdDocumento.GfdDocumentoRepository;
import br.com.mili.milibackend.shared.infra.aws.IS3Service;
import br.com.mili.milibackend.shared.infra.aws.S3ServiceImpl;
import br.com.mili.milibackend.shared.infra.aws.StorageFolderEnum;
import br.com.mili.milibackend.shared.infra.aws.dto.AttachmentDto;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateDocumentoUseCaseImplTest {

    @Mock
    private GfdDocumentoRepository gfdDocumentoRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Gson gson;

    @Mock
    private IS3Service s3Service;

    @InjectMocks
    private CreateDocumentoUseCaseImpl useCase;


    @Test
    void test_Create__deve_criar_fornecedor_documento_e_enviar_s3() {
        var inputDto = new GfdDocumentoCreateInputDto();
        var gfdDocumentoDto = new GfdDocumentoCreateInputDto.GfdDocumentoDto();
        gfdDocumentoDto.setNomeArquivo("arquivo.pdf");
        inputDto.setGfdDocumentoDto(gfdDocumentoDto);
        inputDto.setBase64File("base64content");

        var gfdDocumentoEntity = new GfdDocumento();
        gfdDocumentoEntity.setNomeArquivo("arquivo.pdf");

        var gfdDocumentoSaved = new GfdDocumento();
        gfdDocumentoSaved.setId(1);
        gfdDocumentoSaved.setNomeArquivo("arquivo.pdf");

        var outputDto = new GfdDocumentoCreateOutputDto();
        outputDto.setId(1);

        when(modelMapper.map(gfdDocumentoDto, GfdDocumento.class)).thenReturn(gfdDocumentoEntity);
        when(gfdDocumentoRepository.save(gfdDocumentoEntity)).thenReturn(gfdDocumentoSaved);

        // mock gson.toJson para o upload S3
        when(gson.toJson(any(AttachmentDto.class))).thenReturn("{json}");

        when(modelMapper.map(gfdDocumentoSaved, GfdDocumentoCreateOutputDto.class)).thenReturn(outputDto);

        var result = useCase.execute(inputDto);

        assertNotNull(result);
        assertEquals(1, result.getId());

        verify(gfdDocumentoRepository).save(gfdDocumentoEntity);
        verify(s3Service).upload(eq(StorageFolderEnum.GFD), eq("{json}"));
    }
}