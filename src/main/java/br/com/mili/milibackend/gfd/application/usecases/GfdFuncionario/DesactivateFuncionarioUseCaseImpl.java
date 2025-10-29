package br.com.mili.milibackend.gfd.application.usecases.GfdFuncionario;

import br.com.mili.milibackend.fornecedor.domain.usecases.GetFornecedorByCodOrIdUseCase;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioDesactivateInputDto;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.DesactivateFuncionarioUseCase;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdFuncionarioCodeException.GFD_FUNCIONARIO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class DesactivateFuncionarioUseCaseImpl implements DesactivateFuncionarioUseCase {
    private final GfdFuncionarioRepository gfdFuncionarioRepository;
    private final GetFornecedorByCodOrIdUseCase getFornecedorAndValidate;

    @Override
    public void execute(GfdFuncionarioDesactivateInputDto inputDto) {
        var codFornecedor = inputDto.getFuncionario() != null && inputDto.getFuncionario().getFornecedor() != null ? inputDto.getFuncionario().getFornecedor().getCodigo() : null;

        getFornecedorAndValidate.execute(inputDto.getCodUsuario(), codFornecedor, inputDto.isAnalista());

        var id = inputDto.getFuncionario().getId();

        var gfdFuncionario = gfdFuncionarioRepository.findById(id).orElse(null);

        if (gfdFuncionario == null) {
            throw new NotFoundException(GFD_FUNCIONARIO_NAO_ENCONTRADO.getMensagem(), GFD_FUNCIONARIO_NAO_ENCONTRADO.getCode());
        }

        if (gfdFuncionario.getDesligado() == 1) {
            gfdFuncionario.setDesligado(0);
        } else {
            gfdFuncionario.setDesligado(1);
        }

        gfdFuncionarioRepository.save(gfdFuncionario);
    }
}
