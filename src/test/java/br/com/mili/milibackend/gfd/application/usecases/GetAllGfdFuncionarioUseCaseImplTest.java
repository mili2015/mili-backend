package br.com.mili.milibackend.gfd.application.usecases;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioGetAllOutputDto;
import br.com.mili.milibackend.gfd.application.usecases.GfdFuncionario.GetAllGfdFuncionarioUseCaseImpl;
import br.com.mili.milibackend.gfd.infra.projections.GfdFuncionarioStatusProjection;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)

class GetAllGfdFuncionarioUseCaseImplTest {

    @InjectMocks
    private GetAllGfdFuncionarioUseCaseImpl service;

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
        inputDto.getPageable().setSize(5);
        inputDto.getPageable().setPage(2);

        var proj1 = Mockito.mock(GfdFuncionarioStatusProjection.class);
        when(proj1.getCtforCodigo()).thenReturn(99);

        var proj2 = Mockito.mock(GfdFuncionarioStatusProjection.class);
        when(proj2.getCtforCodigo()).thenReturn(99);


        when(repository.getAll(
                eq(1), eq("%João%"), eq("%Dev%"),
                any(), any(), any(),
                eq(99), eq(5),
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
        var page = service.execute(inputDto);

        // Assert
        assertEquals(2, page.getContent().size(), "Deve retornar 2 itens na página");
        assertEquals(2, page.getPage(), "Deve estar na página 2");
        assertEquals(5, page.getSize(), "O tamanho da página deve ser 5");
        assertEquals("Ana", page.getContent().get(0).getNome());
        assertEquals(99, page.getContent().get(0).getFornecedor().getCodigo());
    }
}