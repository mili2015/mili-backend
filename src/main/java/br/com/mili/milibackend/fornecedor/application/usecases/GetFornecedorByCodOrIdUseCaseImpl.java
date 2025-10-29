package br.com.mili.milibackend.fornecedor.application.usecases;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.usecases.GetFornecedorByCodOrIdUseCase;
import br.com.mili.milibackend.fornecedor.infra.repository.fornecedorRepository.FornecedorRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_FORNECEDOR_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class GetFornecedorByCodOrIdUseCaseImpl implements GetFornecedorByCodOrIdUseCase {
    private final FornecedorRepository fornecedorRepository;

    public Fornecedor execute(Integer codUsuario, Integer id, boolean isAnalista) {
        Fornecedor fornecedor = null;

        if (isAnalista) {
            fornecedor = fornecedorRepository.findById(id).orElse(null);
        } else if (codUsuario != null) {
            fornecedor = fornecedorRepository.findByCodUsuario(codUsuario).orElse(null);
        }

        if (fornecedor == null) {
            throw new NotFoundException(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), GFD_FORNECEDOR_NAO_ENCONTRADO.getCode());
        }

        return fornecedor;
    }
}
