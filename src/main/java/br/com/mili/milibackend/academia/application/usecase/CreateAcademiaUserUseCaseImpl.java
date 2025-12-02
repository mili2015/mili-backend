package br.com.mili.milibackend.academia.application.usecase;

import br.com.mili.milibackend.academia.adapter.external.dto.AcademiaExternalCreateUserRequest;
import br.com.mili.milibackend.academia.adapter.external.service.AcademiaExternalService;
import br.com.mili.milibackend.academia.application.dto.AcademiaUserCreateInputDto;
import br.com.mili.milibackend.academia.application.dto.AcademiaUserSaveOutputDto;
import br.com.mili.milibackend.academia.domain.usecase.CreateAcademiaUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateAcademiaUserUseCaseImpl implements CreateAcademiaUserUseCase {
    private final AcademiaExternalService academiaExternalService;

    @Override
    public AcademiaUserSaveOutputDto execute(AcademiaUserCreateInputDto inputDto) {
        var request = AcademiaExternalCreateUserRequest.builder()
                .username(inputDto.getUsername())
                .email(inputDto.getEmail())
                .password(inputDto.getPassword())
                .name(inputDto.getName())
                .firstName(inputDto.getFirstName())
                .lastName(inputDto.getLastName())
                .roles(List.of("subscriber"))
                .build();

        var response = academiaExternalService.createUser(request);
        academiaExternalService.updateMetaPlainPass(response.getId(), inputDto.getPassword());

        return AcademiaUserSaveOutputDto.builder()
                .id(response.getId())
                .nome(response.getName())
                .username(response.getUsername())
                .email(response.getEmail())
                .build();
    }
}
