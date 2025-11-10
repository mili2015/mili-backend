package br.com.mili.milibackend.gfd.application.usecases.GfdFuncionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioUpdateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioUpdateOutputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.MatricularAcademiaFuncionarioInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdResponsavelIntegracao.ResponsavelIntegracaoSendEmailInputDto;
import br.com.mili.milibackend.gfd.application.mappers.GfdFuncionarioUpdateMapper;
import br.com.mili.milibackend.gfd.application.usecases.GfdFuncionario.utils.UpdateGfdFuncionarioChangeDetector;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.gfd.domain.entity.GfdLocalTrabalho;
import br.com.mili.milibackend.gfd.domain.entity.GfdLocalTrabalhoPk;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.MatricularAcademiaFuncionarioUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.UpdateGfdFuncionarioUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.GfdResponsavelIntegracao.SendEmailResponsavelIntegracaoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdLocalTrabalhoRepository;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdFuncionarioCodeException.GFD_FUNCIONARIO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class UpdateGfdFuncionarioUseCaseImpl implements UpdateGfdFuncionarioUseCase {
    private final GfdFuncionarioRepository gfdFuncionarioRepository;
    private final GfdLocalTrabalhoRepository gfdLocalTrabalhoRepository;
    private final SendEmailResponsavelIntegracaoUseCase sendEmailResponsavelIntegracaoUseCase;
    private final MatricularAcademiaFuncionarioUseCase matricularAcademiaFuncionarioUseCase;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public GfdFuncionarioUpdateOutputDto execute(GfdFuncionarioUpdateInputDto inputDto) {
        var id = inputDto.getId();

        var gfdFuncionario = gfdFuncionarioRepository.findById(id).orElse(null);

        if (gfdFuncionario == null) {
            throw new NotFoundException(GFD_FUNCIONARIO_NAO_ENCONTRADO.getMensagem(), GFD_FUNCIONARIO_NAO_ENCONTRADO.getCode());
        }

        boolean funcionarioAlterado = UpdateGfdFuncionarioChangeDetector.hasFuncionarioChanges(inputDto, gfdFuncionario);
        Set<Integer> novosLocais = UpdateGfdFuncionarioChangeDetector.hasLocaisChanges(inputDto, gfdFuncionario);

        matricularAcademia(inputDto, gfdFuncionario, novosLocais);

        if (funcionarioAlterado) {
            GfdFuncionarioUpdateMapper.map(inputDto, gfdFuncionario);

            gfdFuncionario = gfdFuncionarioRepository.save(gfdFuncionario);
        }

        salvarLocaisTrabalho(gfdFuncionario, novosLocais);

        return modelMapper.map(gfdFuncionario, GfdFuncionarioUpdateOutputDto.class);
    }

    private void matricularAcademia(GfdFuncionarioUpdateInputDto inputDto, GfdFuncionario gfdFuncionario, Set<Integer> novosLocais) {
        var matricularAcademiaFuncionarioInputDto = MatricularAcademiaFuncionarioInputDto.builder()
                .id(gfdFuncionario.getId())
                .nome(gfdFuncionario.getNome())
                .cpf(gfdFuncionario.getCpf())
                .email(inputDto.getEmail())
                .locaisTrabalho(novosLocais.stream().toList())
                .oldEmail(gfdFuncionario.getEmail())
                .build();

        matricularAcademiaFuncionarioUseCase.execute(matricularAcademiaFuncionarioInputDto);
    }

    private void salvarLocaisTrabalho(GfdFuncionario gfdFuncionario, Set<Integer> novosLocais) {
        var gfdLocalTrabalhos = novosLocais.stream().map(novoLocal -> new GfdLocalTrabalho(new GfdLocalTrabalhoPk(gfdFuncionario.getId(), novoLocal), gfdFuncionario)).toList();
        var locaisTrabalhoCreates = gfdLocalTrabalhoRepository.saveAll(gfdLocalTrabalhos);
        gfdFuncionario.getLocaisTrabalho().addAll(locaisTrabalhoCreates);

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
