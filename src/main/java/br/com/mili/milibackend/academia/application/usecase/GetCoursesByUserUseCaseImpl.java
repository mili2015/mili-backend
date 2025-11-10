package br.com.mili.milibackend.academia.application.usecase;

import br.com.mili.milibackend.academia.adapter.external.dto.AcademiaExternalGetCousesByIdResponse;
import br.com.mili.milibackend.academia.adapter.external.service.AcademiaExternalService;
import br.com.mili.milibackend.academia.application.dto.AcademiaGetCourseByUserOutputDto;
import br.com.mili.milibackend.academia.domain.usecase.GetCoursesByUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCoursesByUserUseCaseImpl implements GetCoursesByUserUseCase {

    private final AcademiaExternalService academiaExternalService;

    @Override
    public AcademiaGetCourseByUserOutputDto getCourseByUser(int userId, int courseId) {
        List<AcademiaExternalGetCousesByIdResponse> courses = academiaExternalService.getCoursesByUser(userId, List.of(courseId));

        if (courses == null || courses.isEmpty()) {
            return null;
        }

        AcademiaExternalGetCousesByIdResponse c = courses.get(0);
        return AcademiaGetCourseByUserOutputDto.builder()
                .id(c.getId() != null ? c.getId() : null)
                .title(c.getTitle() != null ? c.getTitle().getRendered() : null)
                .link(c.getLink())
                .build();
    }
}
