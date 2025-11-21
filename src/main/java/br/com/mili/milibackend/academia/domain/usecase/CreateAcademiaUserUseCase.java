package br.com.mili.milibackend.academia.domain.usecase;

import br.com.mili.milibackend.academia.application.dto.AcademiaUserCreateInputDto;
import br.com.mili.milibackend.academia.application.dto.AcademiaUserSaveOutputDto;

public interface CreateAcademiaUserUseCase {
    AcademiaUserSaveOutputDto execute(AcademiaUserCreateInputDto inputDto);
}
