package br.com.mili.milibackend.trade.odometro.application.usecase;

import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeColaboradorGetAllInputDto;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeColaboradorGetAllOutputDto;
import br.com.mili.milibackend.trade.odometro.domain.entity.Colaborador;
import br.com.mili.milibackend.trade.odometro.infra.repository.ColaboradorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllColaboradorUseCaseImplTest {

    @InjectMocks
    private GetAllColaboradorUseCaseImpl useCase;

    @Mock
    private ColaboradorRepository colaboradorRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void deve_listar_paginar_e_mapear_colaboradores() {
        // Arrange
        TradeColaboradorGetAllInputDto input = new TradeColaboradorGetAllInputDto();
        input.getPageable().setPage(2); // page external starts at 1
        input.getPageable().setSize(5);

        Colaborador c1 = new Colaborador();
        c1.setId(10);
        c1.setNome("Joao");
        c1.setSobrenome("Silva");

        Page<Colaborador> page = new PageImpl<>(List.of(c1), PageRequest.of(1, 5), 6);
        when(colaboradorRepository.findAll(any(Specification.class), eq(PageRequest.of(1, 5)))).thenReturn(page);

        TradeColaboradorGetAllOutputDto mapped = new TradeColaboradorGetAllOutputDto();
        mapped.setId(10);
        mapped.setNome("Joao");
        mapped.setSobrenome("Silva");
        when(modelMapper.map(eq(c1), eq(TradeColaboradorGetAllOutputDto.class))).thenReturn(mapped);

        // Act
        MyPage<TradeColaboradorGetAllOutputDto> out = useCase.execute(input);

        // Assert
        assertNotNull(out);
        assertEquals(1, out.getContent().size());
        assertEquals(2, out.getPage());
        assertEquals(5, out.getSize());
        assertEquals(6, out.getTotalElements());
        assertEquals(10, out.getContent().get(0).getId());

        verify(colaboradorRepository).findAll(any(Specification.class), eq(PageRequest.of(1, 5)));
        verify(modelMapper).map(eq(c1), eq(TradeColaboradorGetAllOutputDto.class));
    }
}
