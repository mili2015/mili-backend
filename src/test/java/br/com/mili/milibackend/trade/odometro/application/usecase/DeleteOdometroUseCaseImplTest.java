package br.com.mili.milibackend.trade.odometro.application.usecase;

import br.com.mili.milibackend.shared.exception.types.BadRequestException;
import br.com.mili.milibackend.trade.odometro.infra.repository.OdometroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteOdometroUseCaseImplTest {

    @InjectMocks
    private DeleteOdometroUseCaseImpl useCase;

    @Mock
    private OdometroRepository odometroRepository;

    @Test
    void deve_deletar_quando_existir() {
        when(odometroRepository.existsById(eq(10))).thenReturn(true);

        useCase.execute(10);

        verify(odometroRepository).deleteById(eq(10));
    }

    @Test
    void deve_lancar_erro_quando_nao_existir() {
        when(odometroRepository.existsById(eq(99))).thenReturn(false);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(99));
        assertNotNull(ex.getMessage());

        verify(odometroRepository, never()).deleteById(anyInt());
    }
}
