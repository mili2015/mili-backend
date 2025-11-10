package br.com.mili.milibackend.gfd.application.usecases.GfdFuncionario;

import br.com.mili.milibackend.academia.application.dto.*;
import br.com.mili.milibackend.academia.domain.usecase.CreateAcademiaUserUseCase;
import br.com.mili.milibackend.academia.domain.usecase.EnrollAcademiaUserUseCase;
import br.com.mili.milibackend.academia.domain.usecase.GetByEmailAcademiaUserUseCase;
import br.com.mili.milibackend.academia.domain.usecase.UpdateAcademiaUserUseCase;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.MatricularAcademiaFuncionarioInputDto;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.MatricularAcademiaFuncionarioUseCase;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.enums.AcademiaIdCoursesEnum;
import br.com.mili.milibackend.shared.exception.types.ConflictException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.mili.milibackend.shared.util.GeneratorPassword.senhaAlphaNumerica;

@Service
@AllArgsConstructor
@Slf4j
public class MatricularAcademiaFuncionarioUseCaseImpl implements MatricularAcademiaFuncionarioUseCase {
    private final GetByEmailAcademiaUserUseCase getByEmailAcademiaUserUseCase;
    private final CreateAcademiaUserUseCase createAcademiaUserUseCase;
    private final EnrollAcademiaUserUseCase enrollAcademiaUserUseCase;
    private final GfdFuncionarioRepository gfdFuncionarioRepository;
    private final UpdateAcademiaUserUseCase updateAcademiaUserUseCase;

    @Override
    public Integer execute(MatricularAcademiaFuncionarioInputDto inputDto) {
        Integer academiaId = recuperarAcademiaId(inputDto);
        gfdFuncionarioRepository.alterarIdAcademia(inputDto.getId(), academiaId);

        var locaisTrabalhoPermitidos = inputDto.getLocaisTrabalho().stream()
                .filter(local -> local.equals(AcademiaIdCoursesEnum.TRES_BARRAS.getCtempCodigo()))
                .toList();

        //futuramente remover apos validar as integrações
        if (locaisTrabalhoPermitidos.isEmpty()) {
            return null;
        }

        matricular(locaisTrabalhoPermitidos, academiaId);

        return academiaId;
    }

    private int recuperarAcademiaId(MatricularAcademiaFuncionarioInputDto funcionario) {
        var email = funcionario.getOldEmail() != null ? funcionario.getOldEmail() : funcionario.getEmail();

        var academiaGetUserByEmailOutputDto = getByEmailAcademiaUserUseCase.getUserByEmail(email);

        // cria caso não exista o usuário
        if (academiaGetUserByEmailOutputDto == null) {
            var academiaUserCreateOutputDto = createAcademiaUser(funcionario);

            return academiaUserCreateOutputDto.getId();
        }

        updateUser(funcionario, academiaGetUserByEmailOutputDto);
        return academiaGetUserByEmailOutputDto.getId();
    }

    private void updateUser(MatricularAcademiaFuncionarioInputDto funcionario, AcademiaGetUserByEmailOutputDto academiaGetUserByEmailOutputDto) {
        var nomeCompleto = funcionario.getNome();
        var primeiroNome = funcionario.getNome().split(" ")[0];
        var sobrenome = nomeCompleto.contains(" ") ? nomeCompleto.substring(nomeCompleto.indexOf(' ') + 1).trim() : "";

        var academiaUserUpdateInputDto = AcademiaUserUpdateInputDto.builder()
                .userId(academiaGetUserByEmailOutputDto.getId())
                .email(funcionario.getEmail())
                .name(nomeCompleto)
                .firstName(primeiroNome)
                .lastName(sobrenome)
                .build();

        updateAcademiaUserUseCase.execute(academiaUserUpdateInputDto);
    }

    private void matricular(List<Integer> locaisTrabalhoPermitidos, Integer idAcademia) {
        var idCursos = locaisTrabalhoPermitidos.stream().map(localTrabalho -> {
            if (localTrabalho.equals(AcademiaIdCoursesEnum.TRES_BARRAS.getCtempCodigo())) {
                return AcademiaIdCoursesEnum.TRES_BARRAS.getIdCourse();
            }

            return null;
        }).toList();

        var academiaUserEnrollOutputDto = AcademiaUserEnrollInputDto.builder()
                .userId(idAcademia)
                .idCursos(idCursos)
                .build();

        try {
            enrollAcademiaUserUseCase.execute(academiaUserEnrollOutputDto);
        } catch (ConflictException ex) {
            log.info("Usuario: {}, já possuí matricula", idAcademia);
        }

    }

    private AcademiaUserSaveOutputDto createAcademiaUser(MatricularAcademiaFuncionarioInputDto funcionario) {
        var nomeCompleto = funcionario.getNome();
        var primeiroNome = funcionario.getNome().split(" ")[0];
        var sobrenome = nomeCompleto.contains(" ") ? nomeCompleto.substring(nomeCompleto.indexOf(' ') + 1).trim() : "";

        var academiaUserCreateInputDto = AcademiaUserCreateInputDto.builder()
                .username(funcionario.getCpf())
                .email(funcionario.getEmail())
                .password(senhaAlphaNumerica(7))
                .name(nomeCompleto)
                .firstName(primeiroNome)
                .lastName(sobrenome)
                .build();

        return createAcademiaUserUseCase.execute(academiaUserCreateInputDto);
    }

}
