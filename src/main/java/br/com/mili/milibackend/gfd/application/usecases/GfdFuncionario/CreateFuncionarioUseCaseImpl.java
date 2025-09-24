package br.com.mili.milibackend.gfd.application.usecases.GfdFuncionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioCreateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioCreateOutputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdResponsavelIntegracao.ResponsavelIntegracaoSendEmailInputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.gfd.domain.entity.GfdLocalTrabalho;
import br.com.mili.milibackend.gfd.domain.usecases.GfdFuncionario.CreateFuncionarioUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.GfdResponsavelIntegracao.SendEmailResponsavelIntegracaoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdLocalTrabalhoRepository;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateFuncionarioUseCaseImpl implements CreateFuncionarioUseCase {
    private final ModelMapper modelMapper;
    private final GfdFuncionarioRepository gfdFuncionarioRepository;
    private final GfdLocalTrabalhoRepository gfdLocalTrabalhoRepository;
    private final SendEmailResponsavelIntegracaoUseCase sendEmailResponsavelIntegracaoUseCase;

    @Override
    @Transactional
    public GfdFuncionarioCreateOutputDto execute(GfdFuncionarioCreateInputDto inputDto) {
        var gfdFuncionario = modelMapper.map(inputDto, GfdFuncionario.class);

        gfdFuncionario.setAtivo(1);
        gfdFuncionario.setLiberado(0);
        gfdFuncionario.setDesligado(0);

        var gfdFuncionarioCreated = gfdFuncionarioRepository.save(gfdFuncionario);

        // cria as relações do local de trabalho
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

        return modelMapper.map(gfdFuncionarioCreated, GfdFuncionarioCreateOutputDto.class);
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
