package br.com.mili.milibackend.academia.domain.usecase;

import br.com.mili.milibackend.academia.application.dto.AcademiaGetCourseByUserOutputDto;

public interface GetCoursesByUserUseCase {
    AcademiaGetCourseByUserOutputDto getCourseByUser(int userId, int courseId);
}
