package br.com.mili.milibackend.gfd.application.usecases.GfdFuncionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioCreateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioCreateOutputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.MatricularAcademiaFuncionarioInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdResponsavelIntegracao.ResponsavelIntegracaoSendEmailInputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.gfd.domain.entity.GfdLocalTrabalho;
import br.com.mili.milibackend.gfd.domain.usecases.GfdResponsavelIntegracao.SendEmailResponsavelIntegracaoUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.CreateFuncionarioUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.MatricularAcademiaFuncionarioUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdLocalTrabalhoRepository;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.exception.types.ConflictException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdFuncionarioCodeException.GFD_FUNCIONARIO_JA_SENDO_USADO;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateFuncionarioUseCaseImpl implements CreateFuncionarioUseCase {
    private final ModelMapper modelMapper;
    private final GfdFuncionarioRepository gfdFuncionarioRepository;
    private final GfdLocalTrabalhoRepository gfdLocalTrabalhoRepository;
    private final SendEmailResponsavelIntegracaoUseCase sendEmailResponsavelIntegracaoUseCase;
    private final MatricularAcademiaFuncionarioUseCase matricularAcademiaFuncionarioUseCase;

    @Override
    @Transactional
    public GfdFuncionarioCreateOutputDto execute(GfdFuncionarioCreateInputDto inputDto) {
        var gfdFuncionario = modelMapper.map(inputDto, GfdFuncionario.class);

        gfdFuncionario.setAtivo(1);
        gfdFuncionario.setLiberado(0);
        gfdFuncionario.setDesligado(0);

        validarEmail(gfdFuncionario);

        var gfdFuncionarioCreated = gfdFuncionarioRepository.save(gfdFuncionario);

        // cria as relações do local de trabalho
        adicionarLocaisTrabalho(inputDto, gfdFuncionario);

        try {
            matricular(gfdFuncionarioCreated);
        } catch (Exception ex) {
            log.error("Erro ao matricular o funcionário: " + gfdFuncionarioCreated.getId(), ex);
        }

        return modelMapper.map(gfdFuncionarioCreated, GfdFuncionarioCreateOutputDto.class);
    }

    private void matricular(GfdFuncionario gfdFuncionarioCreated) {
        if (gfdFuncionarioCreated.getEmail() != null && !gfdFuncionarioCreated.getEmail().isBlank()) {
            var matricularAcademiaFuncionarioInputDto = MatricularAcademiaFuncionarioInputDto.builder()
                    .id(gfdFuncionarioCreated.getId())
                    .nome(gfdFuncionarioCreated.getNome())
                    .cpf(gfdFuncionarioCreated.getCpf())
                    .email(gfdFuncionarioCreated.getEmail())
                    .locaisTrabalho(gfdFuncionarioCreated.getLocaisTrabalho().stream().map(GfdLocalTrabalho::getCtempCodigo).toList())
                    .build();

            matricularAcademiaFuncionarioUseCase.execute(matricularAcademiaFuncionarioInputDto);
        }
    }

    private void validarEmail(GfdFuncionario gfdFuncionario) {
        if (gfdFuncionario.getEmail() == null || gfdFuncionario.getEmail().isBlank()) return;

        boolean emailAlreadyUse = gfdFuncionarioRepository.existsByEmail(gfdFuncionario.getEmail());

        if (emailAlreadyUse) {
            throw new ConflictException(GFD_FUNCIONARIO_JA_SENDO_USADO.getMensagem(), GFD_FUNCIONARIO_JA_SENDO_USADO.getCode());
        }
    }

    private void adicionarLocaisTrabalho(GfdFuncionarioCreateInputDto inputDto, GfdFuncionario gfdFuncionario) {
        var novosLocais = inputDto.getLocaisTrabalho().stream()
                .map(dto -> {
                    var localTrabalho = new GfdLocalTrabalho();
                    localTrabalho.setFuncionario(gfdFuncionario);
                    localTrabalho.setCtempCodigo(dto.getCtempCodigo());
                    return localTrabalho;
                })
                .toList();

        gfdLocalTrabalhoRepository.saveAll(novosLocais);
        gfdFuncionario.setLocaisTrabalho(novosLocais);

        // faz o envio de email das integraçoes
        gfdFuncionario.getLocaisTrabalho().stream()
                .map(local -> toInputDtoSendEmail(gfdFuncionario, local))
                .forEach(sendEmailResponsavelIntegracaoUseCase::execute);
    }


    private ResponsavelIntegracaoSendEmailInputDto toInputDtoSendEmail(GfdFuncionario funcionario, GfdLocalTrabalho local) {
        return ResponsavelIntegracaoSendEmailInputDto.builder()
                .funcionario(new ResponsavelIntegracaoSendEmailInputDto.FuncionarioDto(
                        funcionario.getId(), funcionario.getNome()))

                .fornecedor(new ResponsavelIntegracaoSendEmailInputDto.FornecedorDto(
                        funcionario.getFornecedor().getCodigo(),
                        funcionario.getFornecedor().getRazaoSocial()))

                .ctempCodigo(local.getCtempCodigo())
                .build();
    }
}
