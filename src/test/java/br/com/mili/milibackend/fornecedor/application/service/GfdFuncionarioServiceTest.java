package br.com.mili.milibackend.fornecedor.application.service;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.*;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.gfd.infra.projections.GfdFuncionarioStatusProjection;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.gfd.application.service.GfdFuncionarioService;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GfdFuncionarioServiceTest {

    @InjectMocks
    private GfdFuncionarioService service;

    @Mock
    private GfdFuncionarioRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void deve_retornar_pagina_correta_em_getAll_usando_dto_como_projecao() {
        // Arrange
        var inputDto = new GfdFuncionarioGetAllInputDto();
        inputDto.setId(1);
        inputDto.setNome("João");
        inputDto.setFuncao("Dev");
        inputDto.setAtivo(1);
        inputDto.setFornecedor(new GfdFuncionarioGetAllInputDto.FornecedorDto(99));
        inputDto.getPageable().setPage(2);
        inputDto.getPageable().setSize(5);

        var proj1 = Mockito.mock(GfdFuncionarioStatusProjection.class);
        when(proj1.getCtforCodigo()).thenReturn(99);

        var proj2 = Mockito.mock(GfdFuncionarioStatusProjection.class);
        when(proj2.getCtforCodigo()).thenReturn(99);

        when(repository.getAll(
                eq(1), eq("%João%"), eq("%Dev%"),
                any(), any(), any(),
                eq(99), eq(1),
                eq(5), eq(1)
        )).thenReturn((List<GfdFuncionarioStatusProjection>) List.of(proj1, proj2));

        when(repository.getAllCount(
                eq(1), eq("João"), eq("Dev"),
                any(), any(), any(),
                eq(99), eq(1)
        )).thenReturn(2);

        var dto1 = GfdFuncionarioGetAllOutputDto.builder()
                .id(1)
                .nome("Ana")
                .funcao("Dev")
                .fornecedor(new GfdFuncionarioGetAllOutputDto.FornecedorDto(99))
                .build();

        var dto2 = GfdFuncionarioGetAllOutputDto.builder()
                .id(2)
                .nome("Bruno")
                .funcao("Dev")
                .fornecedor(new GfdFuncionarioGetAllOutputDto.FornecedorDto(99))
                .build();

        when(modelMapper.map(proj1, GfdFuncionarioGetAllOutputDto.class)).thenReturn(dto1);
        when(modelMapper.map(proj2, GfdFuncionarioGetAllOutputDto.class)).thenReturn(dto2);

        // Act
        var page = service.getAll(inputDto);

        // Assert
        assertEquals(2, page.getContent().size(), "Deve retornar 2 itens na página");
        assertEquals(2, page.getPage(), "Deve estar na página 2");
        assertEquals(5, page.getSize(), "O tamanho da página deve ser 5");
        assertEquals("Ana", page.getContent().get(0).getNome());
        assertEquals(99, page.getContent().get(0).getFornecedor().getCodigo());
    }

    @Test
    void deve_criar_e_mapear_novo_funcionario_em_create() {
        // Arrange
        var input = new GfdFuncionarioCreateInputDto();
        input.setNome("Carlos");

        var entityToSave = new GfdFuncionario();
        entityToSave.setNome("Carlos");
        when(modelMapper.map(input, GfdFuncionario.class)).thenReturn(entityToSave);

        var saved = new GfdFuncionario();
        saved.setId(10);
        saved.setNome("Carlos");
        when(repository.save(entityToSave)).thenReturn(saved);

        var outputDto = new GfdFuncionarioCreateOutputDto();
        outputDto.setId(10);
        outputDto.setNome("Carlos");
        when(modelMapper.map(saved, GfdFuncionarioCreateOutputDto.class)).thenReturn(outputDto);

        // Act
        var result = service.create(input);

        // Assert
        assertEquals(10, result.getId());
        assertEquals("Carlos", result.getNome());
    }

    @Test
    void deve_atualizar_quando_funcionario_existir_em_update() {
        // Arrange
        var input = new GfdFuncionarioUpdateInputDto();
        input.setId(7);
        input.setNome("Diana");

        var existing = new GfdFuncionario();
        existing.setId(7);
        existing.setNome("Old");
        when(repository.findById(7)).thenReturn(Optional.of(existing));

        doAnswer(i -> {
            existing.setNome("Diana");
            return null;
        })
                .when(modelMapper).map(input, existing);

        var outputDto = new GfdFuncionarioUpdateOutputDto();
        outputDto.setId(7);
        outputDto.setNome("Diana");
        when(modelMapper.map(existing, GfdFuncionarioUpdateOutputDto.class)).thenReturn(outputDto);

        // Act
        var result = service.update(input);

        // Assert
        assertEquals(7, result.getId());
        assertEquals("Diana", result.getNome());
    }

    @Test
    void deve_lancar_NotFoundException_em_update_quando_nao_existir() {
        // Arrange
        when(repository.findById(99)).thenReturn(Optional.empty());
        var input = new GfdFuncionarioUpdateInputDto();
        input.setId(99);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> service.update(input));
    }

    @Test
    void deve_retornar_dto_em_getById_quando_existir() {
        // Arrange
        var entity = new GfdFuncionario();
        entity.setId(8);
        entity.setNome("Elias");
        when(repository.findById(8)).thenReturn(Optional.of(entity));

        var dto = new GfdFuncionarioGetByIdOutputDto();
        dto.setId(8);
        dto.setNome("Elias");
        when(modelMapper.map(entity, GfdFuncionarioGetByIdOutputDto.class)).thenReturn(dto);

        // Act
        var result = service.getById(8);

        // Assert
        assertEquals(8, result.getId());
        assertEquals("Elias", result.getNome());
    }

    @Test
    void deve_retornar_null_em_getById_quando_nao_existir() {
        // Arrange
        when(repository.findById(42)).thenReturn(Optional.empty());

        // Act
        var result = service.getById(42);

        // Assert
        assertNull(result);
    }

    @Test
    void deve_desativar_funcionario_em_delete() {
        // Arrange
        var input = new GfdFuncionarioDeleteInputDto();
        input.setId(3);

        var existing = new GfdFuncionario();
        existing.setId(3);
        existing.setAtivo(1);
        when(repository.findById(3)).thenReturn(Optional.of(existing));

        // Act
        service.delete(input);

        // Assert
        assertEquals(0, existing.getAtivo());
        verify(repository).save(existing);
    }

    @Test
    void deve_lancar_NotFoundException_em_delete_quando_nao_existir() {
        when(repository.findById(55)).thenReturn(Optional.empty());
        var input = new GfdFuncionarioDeleteInputDto();
        input.setId(55);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> service.delete(input));
    }
}
