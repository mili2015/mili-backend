package br.com.mili.milibackend.trade.odometro.application.usecase;

import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeOdometroUpdateInputDto;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeOdometroUpdateOutputDto;
import br.com.mili.milibackend.trade.odometro.domain.entity.Odometro;
import br.com.mili.milibackend.trade.odometro.infra.repository.OdometroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateOdometroUseCaseImplTest {

    @InjectMocks
    private UpdateOdometroUseCaseImpl useCase;

    @Mock
    private OdometroRepository odometroRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void deve_atualizar_quando_existir() {
        TradeOdometroUpdateInputDto input = new TradeOdometroUpdateInputDto();
        input.setId(1);
        input.setKmFim(new BigDecimal("123.45"));
        input.setDataFim(LocalDateTime.now());

        Odometro entity = new Odometro();
        entity.setId(1);
        when(odometroRepository.findById(eq(1))).thenReturn(Optional.of(entity));

        Odometro saved = new Odometro();
        saved.setId(1);
        saved.setKmFim(input.getKmFim());
        saved.setDataFim(input.getDataFim());
        when(odometroRepository.save(any(Odometro.class))).thenReturn(saved);

        TradeOdometroUpdateOutputDto mapped = new TradeOdometroUpdateOutputDto();
        mapped.setId(1);
        mapped.setKmFim(input.getKmFim());
        mapped.setDataFim(input.getDataFim());
        when(modelMapper.map(eq(saved), eq(TradeOdometroUpdateOutputDto.class))).thenReturn(mapped);

        TradeOdometroUpdateOutputDto out = useCase.execute(input);

        assertNotNull(out);
        assertEquals(1, out.getId());
        assertEquals(input.getKmFim(), out.getKmFim());
        assertEquals(input.getDataFim(), out.getDataFim());

        verify(odometroRepository).save(any(Odometro.class));
        verify(modelMapper).map(eq(saved), eq(TradeOdometroUpdateOutputDto.class));
    }

    @Test
    void deve_lancar_notfound_quando_nao_existir() {
        when(odometroRepository.findById(eq(999))).thenReturn(Optional.empty());

        TradeOdometroUpdateInputDto input = new TradeOdometroUpdateInputDto();
        input.setId(999);

        assertThrows(NotFoundException.class, () -> useCase.execute(input));
        verify(odometroRepository, never()).save(any());
        verifyNoInteractions(modelMapper);
    }
}
