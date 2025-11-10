package br.com.mili.milibackend.academia.domain.usecase;

import br.com.mili.milibackend.academia.application.dto.AcademiaGetUserByEmailOutputDto;

public interface GetByEmailAcademiaUserUseCase {
    AcademiaGetUserByEmailOutputDto getUserByEmail(String email);
}
