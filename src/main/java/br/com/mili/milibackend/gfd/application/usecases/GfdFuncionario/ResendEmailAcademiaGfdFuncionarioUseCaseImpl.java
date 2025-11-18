package br.com.mili.milibackend.gfd.application.usecases.GfdFuncionario;

import br.com.mili.milibackend.academia.application.dto.AcademiaGetCourseByUserOutputDto;
import br.com.mili.milibackend.academia.application.dto.AcademiaGetUserByEmailOutputDto;
import br.com.mili.milibackend.academia.domain.usecase.GetByEmailAcademiaUserUseCase;
import br.com.mili.milibackend.academia.domain.usecase.GetCoursesByUserUseCase;
import br.com.mili.milibackend.envioEmail.domain.entity.EnvioEmail;
import br.com.mili.milibackend.envioEmail.domain.interfaces.IEnvioEmailService;
import br.com.mili.milibackend.envioEmail.shared.RemetenteEnum;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioResendEmailAcademiaInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.MatricularAcademiaFuncionarioInputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.gfd.domain.entity.GfdLocalTrabalho;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.MatricularAcademiaFuncionarioUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.ResendEmailAcademiaGfdFuncionarioUseCase;
import br.com.mili.milibackend.gfd.infra.email.AcademiaEmailTemplate;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.enums.AcademiaIdCoursesEnum;
import br.com.mili.milibackend.shared.exception.types.BadRequestException;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.com.mili.milibackend.academia.adapter.web.exceptions.AcademiaCodeException.ACADEMIA_INTEGRACAO_INVALIDA;
import static br.com.mili.milibackend.gfd.adapter.exception.GfdFuncionarioCodeException.GFD_FUNCIONARIO_NAO_ENCONTRADO;
import static br.com.mili.milibackend.gfd.adapter.exception.GfdFuncionarioCodeException.GFD_FUNCIONARIO_SEM_EMAIL;

@Service
@RequiredArgsConstructor
public class ResendEmailAcademiaGfdFuncionarioUseCaseImpl implements ResendEmailAcademiaGfdFuncionarioUseCase {
    private final GfdFuncionarioRepository gfdFuncionarioRepository;
    private final GetByEmailAcademiaUserUseCase getByEmailAcademiaUserUseCase;
    private final MatricularAcademiaFuncionarioUseCase matricularAcademiaFuncionarioUseCase;
    private final IEnvioEmailService envioEmailService;
    private final GetCoursesByUserUseCase getCoursesByUserUseCase;

    @Override
    public void execute(GfdFuncionarioResendEmailAcademiaInputDto inputDto) {
        var funcionario = getGfdFuncionario(inputDto);

        var userAcademia = getAcademiaGetUserByEmailOutputDto(funcionario);

        AcademiaIdCoursesEnum courseEnum = getAcademiaIdCoursesEnum(inputDto);

        var courseFound = getAcademiaGetCourseByUserOutputDto(userAcademia, courseEnum, funcionario);
        if (courseFound == null) return;

        enviarEmail(funcionario, courseFound);

    }

    private AcademiaGetCourseByUserOutputDto getAcademiaGetCourseByUserOutputDto(AcademiaGetUserByEmailOutputDto userAcademia, AcademiaIdCoursesEnum courseEnum, GfdFuncionario funcionario) {
        AcademiaGetCourseByUserOutputDto courseFound = null;

        if (userAcademia != null) {
            courseFound = getCoursesByUserUseCase.getCourseByUser(userAcademia.getId(), courseEnum.getIdCourse());
        }

        // ja retorna porque ja é feito o envio da matricula do usuário
        if (courseFound == null) {
            matricular(funcionario);
            return null;
        }

        return courseFound;
    }

    private AcademiaGetUserByEmailOutputDto getAcademiaGetUserByEmailOutputDto(GfdFuncionario funcionario) {
        return getByEmailAcademiaUserUseCase.getUserByEmail(funcionario.getEmail());
    }

    private AcademiaIdCoursesEnum getAcademiaIdCoursesEnum(GfdFuncionarioResendEmailAcademiaInputDto inputDto) {
        AcademiaIdCoursesEnum courseEnum = AcademiaIdCoursesEnum.fromCtempCodigo(inputDto.getCtempCodigo());

        if (courseEnum == null) {
            throw new BadRequestException(
                    ACADEMIA_INTEGRACAO_INVALIDA.getMensagem(),
                    ACADEMIA_INTEGRACAO_INVALIDA.getCode()
            );
        }
        return courseEnum;
    }

    private GfdFuncionario getGfdFuncionario(GfdFuncionarioResendEmailAcademiaInputDto inputDto) {
        var funcionario = gfdFuncionarioRepository.findById(inputDto.getIdFuncionario())
                .orElseThrow(() -> new NotFoundException(
                        GFD_FUNCIONARIO_NAO_ENCONTRADO.getMensagem(),
                        GFD_FUNCIONARIO_NAO_ENCONTRADO.getCode())
                );

        if (funcionario.getEmail() == null || funcionario.getEmail().isEmpty()) {
            throw new BadRequestException(
                    GFD_FUNCIONARIO_SEM_EMAIL.getMensagem(),
                    GFD_FUNCIONARIO_SEM_EMAIL.getCode())
                    ;
        }
        return funcionario;
    }

    private void matricular(GfdFuncionario funcionario) {
        var matricularAcademiaFuncionarioInputDto = MatricularAcademiaFuncionarioInputDto.builder()
                .id(funcionario.getId())
                .nome(funcionario.getNome())
                .cpf(funcionario.getCpf())
                .email(funcionario.getEmail())
                .locaisTrabalho(funcionario.getLocaisTrabalho().stream().map(GfdLocalTrabalho::getCtempCodigo).toList())
                .oldEmail(funcionario.getEmail())
                .build();

        matricularAcademiaFuncionarioUseCase.execute(matricularAcademiaFuncionarioInputDto);
    }

    private void enviarEmail(GfdFuncionario funcionario, AcademiaGetCourseByUserOutputDto courseFound) {
        var template = AcademiaEmailTemplate.template(
                funcionario.getNome(),
                courseFound.getLink(),
                funcionario.getEmail()
        );

        var titulo = "Mili - Academia Integração";

        var assunto = "Academia Integração";

        var envioEmail = EnvioEmail.builder()
                .remetente(RemetenteEnum.MILI.getEndereco())
                .destinatario(funcionario.getEmail())
                .assunto(assunto)
                .titulo(titulo)
                .texto(template)
                .build();

        envioEmailService.enviarFila(envioEmail);
    }


}
