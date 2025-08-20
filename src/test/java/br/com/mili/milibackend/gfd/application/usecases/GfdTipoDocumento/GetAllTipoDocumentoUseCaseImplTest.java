package br.com.mili.milibackend.gfd.application.usecases.GfdTipoDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumento;
import br.com.mili.milibackend.gfd.infra.repository.GfdTipoDocumentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllTipoDocumentoUseCaseImplTest {
    @Mock
    private GfdTipoDocumentoRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GetAllTipoDocumentoUseCaseImpl service;

    private GfdTipoDocumento entity;

    @BeforeEach
    void setup() {
        entity = new GfdTipoDocumento();
        entity.setId(1);
        entity.setNome("Documento");
        entity.setAtivo(true);
    }

    @Test
    void teste_GetAll() {
        when(repository.findAll((Specification<GfdTipoDocumento>) any(), any(Sort.class)))
                .thenReturn(List.of(entity));

        GfdTipoDocumentoGetAllOutputDto dto = new GfdTipoDocumentoGetAllOutputDto();
        dto.setId(1);
        dto.setNome("Documento");

        when(modelMapper.map(entity, GfdTipoDocumentoGetAllOutputDto.class)).thenReturn(dto);

        GfdTipoDocumentoGetAllInputDto input = new GfdTipoDocumentoGetAllInputDto();
        input.setNome("Doc");

        var result = service.execute(input);

        assertEquals(1, result.size());
        assertEquals("Documento", result.get(0).getNome());
    }
}