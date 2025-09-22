package br.com.mili.milibackend.trade.odometro.application.usecase;

import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeOdometroGetAllInputDto;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeOdometroGetAllOutputDto;
import br.com.mili.milibackend.trade.odometro.infra.repository.OdometroRepository;
import br.com.mili.milibackend.trade.odometro.infra.repository.dto.OdometroResumoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllOdometroUseCaseImplTest {

    @InjectMocks
    private GetAllOdometroUseCaseImpl useCase;

    @Mock
    private OdometroRepository odometroRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void deve_listar_paginar_e_mapear_registros() {
        // Arrange
        TradeOdometroGetAllInputDto input = new TradeOdometroGetAllInputDto();
        input.getPageable().setPage(1);
        input.getPageable().setSize(10);

        OdometroResumoDto resumo = new OdometroResumoDto();
        resumo.setId(1);
        resumo.setIdColaborador(123);
        resumo.setDataInicio(LocalDateTime.now().minusHours(1));
        resumo.setDataFim(LocalDateTime.now());
        resumo.setKmInicio(new BigDecimal("10"));
        resumo.setKmFim(new BigDecimal("20"));
        resumo.setTipoVeiculo("CARRO");
        resumo.setDiferencaKm(new BigDecimal("10"));
        resumo.setTotalDiferencaKm(new BigDecimal("100"));

        Page<OdometroResumoDto> page = new PageImpl<>(List.of(resumo), PageRequest.of(0, 10), 1);

        when(odometroRepository.getAll(any(), any(PageRequest.class))).thenReturn(page);

        TradeOdometroGetAllOutputDto mapped = new TradeOdometroGetAllOutputDto();
        mapped.setId(1);
        mapped.setIdColaborador(123);
        when(modelMapper.map(eq(resumo), eq(TradeOdometroGetAllOutputDto.class))).thenReturn(mapped);

        // Act
        MyPage<TradeOdometroGetAllOutputDto> out = useCase.execute(input);

        // Assert
        assertNotNull(out);
        assertEquals(1, out.getContent().size());
        assertEquals(1, out.getPage());
        assertEquals(10, out.getSize());
        assertEquals(1, out.getTotalElements());
        assertEquals(123, out.getContent().get(0).getIdColaborador());

        verify(odometroRepository).getAll(any(), eq(PageRequest.of(0, 10)));
        verify(modelMapper).map(eq(resumo), eq(TradeOdometroGetAllOutputDto.class));
    }
}
