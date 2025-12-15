package br.com.mili.milibackend.academia.application.usecase;

import br.com.mili.milibackend.academia.adapter.external.dto.AcademiaExternalMatricularUserResponse;
import br.com.mili.milibackend.academia.adapter.external.service.AcademiaExternalService;
import br.com.mili.milibackend.academia.application.dto.AcademiaUserEnrollInputDto;
import br.com.mili.milibackend.academia.application.dto.AcademiaUserEnrollOutputDto;
import br.com.mili.milibackend.academia.domain.usecase.EnrollAcademiaUserUseCase;
import br.com.mili.milibackend.shared.exception.types.ConflictException;
import br.com.mili.milibackend.shared.exception.types.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.mili.milibackend.academia.adapter.web.exceptions.AcademiaCodeException.ACADEMIA_FALHA_AO_MATRICULAR;
import static br.com.mili.milibackend.academia.adapter.web.exceptions.AcademiaCodeException.ACADEMIA_USUARIO_MATRICULADO;

@Service
@RequiredArgsConstructor
public class EnrollAcademiaUserUseCaseImpl implements EnrollAcademiaUserUseCase {

    private final AcademiaExternalService academiaExternalService;

    @Override
    public AcademiaUserEnrollOutputDto execute(AcademiaUserEnrollInputDto inputDto) {
        //  Matriculo
        var response = academiaExternalService.matricularUser(inputDto.getUserId(), inputDto.getIdCursos());

        // Em caso de falha joga um erro
        validateResponte(response);

        return AcademiaUserEnrollOutputDto.builder()
                .userId(inputDto.getUserId())
                .idCursos(response.stream().map(AcademiaExternalMatricularUserResponse::getCourseId).toList())
                .build();
    }

    private void validateResponte(List<AcademiaExternalMatricularUserResponse> response) {
        response.forEach(res -> {
            if(res.getStatus().equals("failed") && res.getMessage().equals("User already enrolled in Curso."))
            {
                throw new ConflictException(ACADEMIA_USUARIO_MATRICULADO.getMensagem(), ACADEMIA_FALHA_AO_MATRICULAR.getCode());
            }


            if (res.getStatus().equals("failed")) {
                throw new InternalServerException(ACADEMIA_FALHA_AO_MATRICULAR.getMensagem() + res.getMessage(), ACADEMIA_FALHA_AO_MATRICULAR.getCode());
            }
        });
    }
}
