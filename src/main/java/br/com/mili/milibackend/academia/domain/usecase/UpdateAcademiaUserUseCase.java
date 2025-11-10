package br.com.mili.milibackend.academia.domain.usecase;

import br.com.mili.milibackend.academia.application.dto.AcademiaUserSaveOutputDto;
import br.com.mili.milibackend.academia.application.dto.AcademiaUserUpdateInputDto;

public interface UpdateAcademiaUserUseCase {
    AcademiaUserSaveOutputDto execute(AcademiaUserUpdateInputDto inputDto);
}
