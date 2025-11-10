package br.com.mili.milibackend.academia.application.usecase;

import br.com.mili.milibackend.academia.adapter.external.dto.AcademiaExternalUpdateUserRequest;
import br.com.mili.milibackend.academia.adapter.external.service.AcademiaExternalService;
import br.com.mili.milibackend.academia.application.dto.AcademiaUserSaveOutputDto;
import br.com.mili.milibackend.academia.application.dto.AcademiaUserUpdateInputDto;
import br.com.mili.milibackend.academia.domain.usecase.UpdateAcademiaUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateAcademiaUserUseCaseImpl implements UpdateAcademiaUserUseCase {
    private final AcademiaExternalService academiaExternalService;

    @Override
    public AcademiaUserSaveOutputDto execute(AcademiaUserUpdateInputDto inputDto) {
        var request = AcademiaExternalUpdateUserRequest.builder()
                .userId(inputDto.getUserId())
                .email(inputDto.getEmail())
                .name(inputDto.getName())
                .firstName(inputDto.getFirstName())
                .lastName(inputDto.getLastName())
                .build();

        var response = academiaExternalService.updateUser(request);

        return AcademiaUserSaveOutputDto.builder()
                .id(response.getId())
                .nome(response.getName())
                .username(response.getUsername())
                .email(response.getEmail())
                .build();
    }
}
