package br.com.mili.milibackend.gfd.application.usecases.GfdFuncionario;

import br.com.mili.milibackend.fornecedor.domain.usecases.GetFornecedorByCodOrIdUseCase;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioDesactivateInputDto;
import br.com.mili.milibackend.gfd.domain.usecases.GfdFuncionario.DesactivateFuncionarioUseCase;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.exception.types.ConflictException;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdFuncionarioCodeException.GFD_FUNCIONARIO_JA_DESLIGADO;
import static br.com.mili.milibackend.gfd.adapter.exception.GfdFuncionarioCodeException.GFD_FUNCIONARIO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class DesactivateFuncionarioUseCaseImpl implements DesactivateFuncionarioUseCase {
    private final GfdFuncionarioRepository gfdFuncionarioRepository;
    private final GetFornecedorByCodOrIdUseCase getFornecedorAndValidate;

    @Override
    public void execute(GfdFuncionarioDesactivateInputDto inputDto) {
        var codFornecedor = inputDto.getFuncionario() != null && inputDto.getFuncionario().getFornecedor() != null ? inputDto.getFuncionario().getFornecedor().getCodigo() : null;

        getFornecedorAndValidate.execute(inputDto.getCodUsuario(), codFornecedor);

        var id = inputDto.getFuncionario().getId();

        var gfdFuncionario = gfdFuncionarioRepository.findById(id).orElse(null);

        if (gfdFuncionario == null) {
            throw new NotFoundException(GFD_FUNCIONARIO_NAO_ENCONTRADO.getMensagem(), GFD_FUNCIONARIO_NAO_ENCONTRADO.getCode());
        }

        if(gfdFuncionario.getDesligado() == 1) {
            throw new ConflictException(GFD_FUNCIONARIO_JA_DESLIGADO.getMensagem(), GFD_FUNCIONARIO_JA_DESLIGADO.getCode());
        }

        gfdFuncionario.setDesligado(1);

        gfdFuncionarioRepository.save(gfdFuncionario);
    }
}
