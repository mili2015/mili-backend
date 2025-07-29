package br.com.mili.milibackend.gfd.application.service;

import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.*;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumentoTipoEnum;
import br.com.mili.milibackend.gfd.infra.repository.GfdTipoDocumentoRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class GfdTipoDocumentoServiceTest {

    @Mock
    private GfdTipoDocumentoRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GfdTipoDocumentoService service;

    private GfdTipoDocumento entity;

    @BeforeEach
    void setup() {
        entity = new GfdTipoDocumento();
        entity.setId(1);
        entity.setNome("Documento");
        entity.setAtivo(true);
    }

    @Test
    void testGetById_found() {
        when(repository.findById(1)).thenReturn(Optional.of(entity));

        GfdTipoDocumentoGetByIdOutputDto dto = new GfdTipoDocumentoGetByIdOutputDto();
        dto.setId(1);
        dto.setNome("Documento");

        when(modelMapper.map(entity, GfdTipoDocumentoGetByIdOutputDto.class)).thenReturn(dto);

        var result = service.getById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Documento", result.getNome());
    }

    @Test
    void testGetById_notFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        var result = service.getById(99);

        assertNull(result);
    }

    @SuppressWarnings("unchecked")
    @Test
    void testGetAll() {
        when(repository.findAll((Specification<GfdTipoDocumento>) any(), any(Sort.class)))
                .thenReturn(List.of(entity));

        GfdTipoDocumentoGetAllOutputDto dto = new GfdTipoDocumentoGetAllOutputDto();
        dto.setId(1);
        dto.setNome("Documento");

        when(modelMapper.map(entity, GfdTipoDocumentoGetAllOutputDto.class)).thenReturn(dto);

        GfdTipoDocumentoGetAllInputDto input = new GfdTipoDocumentoGetAllInputDto();
        input.setNome("Doc");

        var result = service.getAll(input);

        assertEquals(1, result.size());
        assertEquals("Documento", result.get(0).getNome());
    }

    @Test
    void testCreate() {
        GfdTipoDocumentoCreateInputDto input = new GfdTipoDocumentoCreateInputDto();
        input.setNome("Novo");
        input.setDiasValidade(10);
        input.setTipo("FORNECEDOR");
        input.setObrigatoriedade(true);

        GfdTipoDocumento newEntity = new GfdTipoDocumento();
        newEntity.setNome("Novo");
        newEntity.setTipo(GfdTipoDocumentoTipoEnum.FORNECEDOR);
        newEntity.setDiasValidade(10);
        newEntity.setObrigatoriedade(true);

        GfdTipoDocumento savedEntity = new GfdTipoDocumento();
        savedEntity.setId(10);
        savedEntity.setNome("Novo");
        savedEntity.setAtivo(true);

        GfdTipoDocumentoCreateOutputDto outputDto = new GfdTipoDocumentoCreateOutputDto();
        outputDto.setId(10);
        outputDto.setNome("Novo");
        outputDto.setAtivo(true);

        when(modelMapper.map(input, GfdTipoDocumento.class)).thenReturn(newEntity);
        when(repository.save(newEntity)).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, GfdTipoDocumentoCreateOutputDto.class)).thenReturn(outputDto);

        var result = service.create(input);

        assertNotNull(result);
        assertEquals(10, result.getId());
        assertEquals("Novo", result.getNome());
        assertTrue(result.getAtivo());
    }

    @Test
    void testUpdate_found() {
        GfdTipoDocumentoUpdateInputDto input = new GfdTipoDocumentoUpdateInputDto();
        input.setId(1);
        input.setNome("Atualizado");
        input.setTipo("PDF");
        input.setObrigatoriedade(true);

        when(repository.findById(1)).thenReturn(Optional.of(entity));

        input.setTipo("FORNECEDOR");

        doAnswer(invocation -> {
            GfdTipoDocumentoUpdateInputDto src = invocation.getArgument(0);
            GfdTipoDocumento dest = invocation.getArgument(1);
            dest.setTipo(GfdTipoDocumentoTipoEnum.valueOf(src.getTipo()));
            return null;
        }).when(modelMapper).map(any(GfdTipoDocumentoUpdateInputDto.class), any(GfdTipoDocumento.class));


        when(repository.save(entity)).thenReturn(entity);

        GfdTipoDocumentoUpdateOutputDto outputDto = new GfdTipoDocumentoUpdateOutputDto();
        outputDto.setId(1);
        outputDto.setNome("Atualizado");

        when(modelMapper.map(entity, GfdTipoDocumentoUpdateOutputDto.class)).thenReturn(outputDto);

        var result = service.update(input);

        assertNotNull(result);
        assertEquals("Atualizado", result.getNome());
    }

    @Test
    void testUpdate_notFound() {
        GfdTipoDocumentoUpdateInputDto input = new GfdTipoDocumentoUpdateInputDto();
        input.setId(999);

        when(repository.findById(999)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.update(input));
        assertTrue(ex.getMessage().contains("não encontrado"));
    }

    @Test
    void testDelete() {
        service.delete(5);
        verify(repository, times(1)).inactive(5);
    }
}
