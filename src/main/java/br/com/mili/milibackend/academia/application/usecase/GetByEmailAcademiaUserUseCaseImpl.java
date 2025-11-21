package br.com.mili.milibackend.academia.application.usecase;

import br.com.mili.milibackend.academia.adapter.external.service.AcademiaExternalService;
import br.com.mili.milibackend.academia.application.dto.AcademiaGetUserByEmailOutputDto;
import br.com.mili.milibackend.academia.domain.usecase.GetByEmailAcademiaUserUseCase;
import br.com.mili.milibackend.shared.exception.types.BadRequestException;
import br.com.mili.milibackend.shared.util.EmailValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.com.mili.milibackend.academia.adapter.web.exceptions.AcademiaCodeException.ACADEMIA_EMAIL_INVALIDO;

@Service
@RequiredArgsConstructor
public class GetByEmailAcademiaUserUseCaseImpl implements GetByEmailAcademiaUserUseCase {

    private final AcademiaExternalService academiaExternalService;

    @Override
    public AcademiaGetUserByEmailOutputDto getUserByEmail(String email) {

        if (!EmailValidation.validate(email)) {
            throw new BadRequestException(ACADEMIA_EMAIL_INVALIDO.getMensagem(), ACADEMIA_EMAIL_INVALIDO.getCode());
        }

        return academiaExternalService.getUserByEmailResponse(email)
                .map(user -> AcademiaGetUserByEmailOutputDto.builder()
                        .id(user.getId())
                        .nome(user.getName())
                        .url(user.getUrl())
                        .descricao(user.getDescription())
                        .courses(user.getLinks().getCourses().stream().map(link -> AcademiaGetUserByEmailOutputDto.LinkItem.builder()
                                .href(link.getHref())
                                .build()).toList())
                        .build())
                .orElse(null);
    }
}
