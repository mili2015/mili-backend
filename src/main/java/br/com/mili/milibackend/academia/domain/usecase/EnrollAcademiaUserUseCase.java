package br.com.mili.milibackend.academia.domain.usecase;

import br.com.mili.milibackend.academia.application.dto.AcademiaUserEnrollInputDto;
import br.com.mili.milibackend.academia.application.dto.AcademiaUserEnrollOutputDto;

public interface EnrollAcademiaUserUseCase {
    AcademiaUserEnrollOutputDto execute(AcademiaUserEnrollInputDto inputDto);
}
