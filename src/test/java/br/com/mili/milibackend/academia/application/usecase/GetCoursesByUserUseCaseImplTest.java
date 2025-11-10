package br.com.mili.milibackend.academia.application.usecase;

import br.com.mili.milibackend.academia.adapter.external.dto.AcademiaExternalGetCousesByIdResponse;
import br.com.mili.milibackend.academia.adapter.external.service.AcademiaExternalService;
import br.com.mili.milibackend.academia.application.dto.AcademiaGetCourseByUserOutputDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCoursesByUserUseCaseImplTest {

    @Mock
    private AcademiaExternalService academiaExternalService;

    @InjectMocks
    private GetCoursesByUserUseCaseImpl useCase;

    @Test
    void teste_deve_retornar_null_quando_externo_retorna_vazio() {
        when(academiaExternalService.getCoursesByUser(5, List.of(100)))
                .thenReturn(List.of());

        AcademiaGetCourseByUserOutputDto out = useCase.getCourseByUser(5, 100);
        assertThat(out).isNull();
    }

    @Test
    void teste_deve_mapear_campos_quando_externo_retorna_curso() {
        AcademiaExternalGetCousesByIdResponse.TitleDto title = AcademiaExternalGetCousesByIdResponse.TitleDto.builder()
                .rendered("Course Title")
                .build();
        AcademiaExternalGetCousesByIdResponse course = AcademiaExternalGetCousesByIdResponse.builder()
                .id(100)
                .title(title)
                .link("/course/100")
                .build();

        when(academiaExternalService.getCoursesByUser(7, List.of(100)))
                .thenReturn(List.of(course));

        AcademiaGetCourseByUserOutputDto out = useCase.getCourseByUser(7, 100);

        assertThat(out.getId()).isEqualTo(100);
        assertThat(out.getTitle()).isEqualTo("Course Title");
        assertThat(out.getLink()).isEqualTo("/course/100");
    }
}
