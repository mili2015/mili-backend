package br.com.mili.milibackend.fornecedor.application.usecases;

import br.com.mili.milibackend.fornecedor.domain.usecases.ValidatePermissionFornecedorUseCase;
import br.com.mili.milibackend.fornecedor.infra.repository.fornecedorRepository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidatePermissionFornecedorUseCaseImpl implements ValidatePermissionFornecedorUseCase {
    private final FornecedorRepository fornecedorRepository;

    @Override
    public boolean execute(Integer codUsuario, Integer fornecedorId) {
        // se não vier codUsuario, significa que o utilizador é um analista
        if (codUsuario == null) return true;

        return fornecedorRepository
                .existsByUsuarioIdAndFornecedorId(codUsuario, fornecedorId);
    }
}
