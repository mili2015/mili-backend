package br.com.mili.milibackend.gfd.application.usecases;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioGetAllOutputDto;
import br.com.mili.milibackend.gfd.application.usecases.GfdFuncionario.GetAllGfdFuncionarioUseCaseImpl;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.gfd.domain.entity.GfdLocalTrabalho;
import br.com.mili.milibackend.gfd.infra.projections.GfdFuncionarioStatusProjection;
import br.com.mili.milibackend.gfd.infra.repository.GfdLocalTrabalhoRepository;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.page.pagination.MyPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@SpringBootTest
class GetAllGfdFuncionarioUseCaseImplTest {

    @InjectMocks
    private GetAllGfdFuncionarioUseCaseImpl service;

    @Mock
    private GfdFuncionarioRepository repository;

    @Mock
    private GfdLocalTrabalhoRepository gfdLocalTrabalhoRepository;

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
        inputDto.setLocaisTrabalho(List.of(1));

        var proj1 = Mockito.mock(GfdFuncionarioStatusProjection.class);
        when(proj1.getCtforCodigo()).thenReturn(99);

        var proj2 = Mockito.mock(GfdFuncionarioStatusProjection.class);
        when(proj2.getCtforCodigo()).thenReturn(99);


        when(repository.getAll(
                1, "%João%", "%Dev%", null, null, null, 99, 1, List.of(1), 5, 5
        )).thenReturn(List.of(proj1, proj2));

        when(repository.getAllCount(
                1, "%João%", "%Dev%", null, null, null, 99, 1, List.of(1)
        )).thenReturn(2);

        var dto1 = GfdFuncionarioGetAllOutputDto.builder()
                .id(1)
                .nome("João Rodrigues")
                .funcao("Dev")
                .fornecedor(new GfdFuncionarioGetAllOutputDto.FornecedorDto(99))
                .build();

        var dto2 = GfdFuncionarioGetAllOutputDto.builder()
                .id(2)
                .nome("João Fernnando")
                .funcao("Dev")
                .fornecedor(new GfdFuncionarioGetAllOutputDto.FornecedorDto(99))
                .build();

        when(modelMapper.map(proj1, GfdFuncionarioGetAllOutputDto.class)).thenReturn(dto1);
        when(modelMapper.map(proj2, GfdFuncionarioGetAllOutputDto.class)).thenReturn(dto2);
        when(gfdLocalTrabalhoRepository.findByInIdFuncionario(any())).thenReturn(List.of());

        // Act
        var page = service.execute(inputDto);

        // Assert
        assertEquals(2, page.getContent().size(), "Deve retornar 2 itens na página");
        assertEquals(2, page.getPage(), "Deve estar na página 2");
        assertEquals(5, page.getSize(), "O tamanho da página deve ser 5");
        assertEquals("João Rodrigues", page.getContent().get(0).getNome());
        assertEquals(99, page.getContent().get(0).getFornecedor().getCodigo());
    }

    @Test
    void deve_retornar_pagina_correta_com_locais_trabalho() {
        // Arrange
        var inputDto = new GfdFuncionarioGetAllInputDto();
        inputDto.setId(1);
        inputDto.setNome("João");
        inputDto.setFuncao("Dev");
        inputDto.setAtivo(1);
        inputDto.setFornecedor(new GfdFuncionarioGetAllInputDto.FornecedorDto(99));
        inputDto.getPageable().setSize(5);
        inputDto.getPageable().setPage(2);
        inputDto.setLocaisTrabalho(List.of(1));

        var proj1 = Mockito.mock(GfdFuncionarioStatusProjection.class);
        when(proj1.getId()).thenReturn(1);
        when(proj1.getCtforCodigo()).thenReturn(99);

        var proj2 = Mockito.mock(GfdFuncionarioStatusProjection.class);
        when(proj2.getId()).thenReturn(2);
        when(proj2.getCtforCodigo()).thenReturn(99);

        when(repository.getAll(
                1, "%João%", "%Dev%", null, null, null, 99, 1, List.of(1), 5, 5
        )).thenReturn(List.of(proj1, proj2));

        when(repository.getAllCount(
                1, "%João%", "%Dev%", null, null, null, 99, 1, List.of(1)
        )).thenReturn(2);

        var local1 = new GfdLocalTrabalho();
        local1.setIdFuncionario(1);
        local1.setCtempCodigo(101);
        local1.setFuncionario(new GfdFuncionario());

        var local2 = new GfdLocalTrabalho();
        local2.setIdFuncionario(1);
        local2.setCtempCodigo(101);
        local2.setFuncionario(new GfdFuncionario());

        when(gfdLocalTrabalhoRepository.findByInIdFuncionario(List.of(1, 2)))
                .thenReturn(List.of(
                        local1,
                        local2
                ));

        var dto1 = GfdFuncionarioGetAllOutputDto.builder()
                .id(1)
                .nome("João Rodrigues")
                .funcao("Dev")
                .fornecedor(new GfdFuncionarioGetAllOutputDto.FornecedorDto(99))
                .build();

        var dto2 = GfdFuncionarioGetAllOutputDto.builder()
                .id(2)
                .nome("João Fernnando")
                .funcao("Dev")
                .fornecedor(new GfdFuncionarioGetAllOutputDto.FornecedorDto(99))
                .build();

        when(modelMapper.map(proj1, GfdFuncionarioGetAllOutputDto.class)).thenReturn(dto1);
        when(modelMapper.map(proj2, GfdFuncionarioGetAllOutputDto.class)).thenReturn(dto2);

        // Act
        MyPage<GfdFuncionarioGetAllOutputDto> page = service.execute(inputDto);

        // Assert
        assertEquals(2, page.getContent().size());
        assertEquals(2, page.getPage());
        assertEquals(5, page.getSize());
        assertEquals(99, page.getContent().get(0).getFornecedor().getCodigo());
        assertNotNull(page.getContent().get(0).getLocaisTrabalho());
        assertNotNull(page.getContent().get(1).getLocaisTrabalho());
    }
}