package br.com.mili.milibackend.academia.application.usecase;

import br.com.mili.milibackend.academia.adapter.external.dto.AcademiaExternalGetCousesByIdResponse;
import br.com.mili.milibackend.academia.adapter.external.dto.AcademiaExternalMatricularUserResponse;
import br.com.mili.milibackend.academia.adapter.external.service.AcademiaExternalService;
import br.com.mili.milibackend.academia.application.dto.AcademiaUserEnrollInputDto;
import br.com.mili.milibackend.academia.application.dto.AcademiaUserEnrollOutputDto;
import br.com.mili.milibackend.shared.exception.types.ConflictException;
import br.com.mili.milibackend.shared.exception.types.InternalServerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollAcademiaUserUseCaseImplTest {

    @Mock
    private AcademiaExternalService academiaExternalService;

    @InjectMocks
    private EnrollAcademiaUserUseCaseImpl useCase;

    @Test
    void teste_deve_matricular_e_retornar_quando_sem_matriculas_previas_e_tudo_sucesso() {
        AcademiaUserEnrollInputDto input = AcademiaUserEnrollInputDto.builder()
                .userId(10)
                .idCursos(List.of(3, 4))
                .build();


        List<AcademiaExternalMatricularUserResponse> resp = List.of(
                AcademiaExternalMatricularUserResponse.builder().courseId(3).status("success").build(),
                AcademiaExternalMatricularUserResponse.builder().courseId(4).status("success").build()
        );
        when(academiaExternalService.matricularUser(10, List.of(3, 4)))
                .thenReturn(resp);

        AcademiaUserEnrollOutputDto out = useCase.execute(input);

        assertThat(out.getUserId()).isEqualTo(10);
        assertThat(out.getIdCursos()).containsExactlyInAnyOrder(3, 4);
    }

    @Test
    void teste_deve_lancar_erro_interno_quando_alguma_matricula_falhar() {
        AcademiaUserEnrollInputDto input = AcademiaUserEnrollInputDto.builder()
                .userId(11)
                .idCursos(List.of(7))
                .build();

        List<AcademiaExternalMatricularUserResponse> resp = List.of(
                AcademiaExternalMatricularUserResponse.builder().courseId(7).status("failed").message("Boom").build()
        );
        when(academiaExternalService.matricularUser(11, List.of(7)))
                .thenReturn(resp);

        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(InternalServerException.class);
    }
}
