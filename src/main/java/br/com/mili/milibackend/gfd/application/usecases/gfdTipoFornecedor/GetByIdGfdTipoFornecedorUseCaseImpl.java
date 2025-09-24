package br.com.mili.milibackend.gfd.application.usecases.gfdTipoFornecedor;

import br.com.mili.milibackend.gfd.application.dto.gfdTipoFornecedor.GfdTipoFornecedorGetByIdOutputDto;
import br.com.mili.milibackend.gfd.domain.usecases.gfdTipoFornecedor.GetByIdGfdTipoFornecedorUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdTipoFornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetByIdGfdTipoFornecedorUseCaseImpl implements GetByIdGfdTipoFornecedorUseCase {
    private final GfdTipoFornecedorRepository repository;
    private final ModelMapper modelMapper;


    @Override
    public GfdTipoFornecedorGetByIdOutputDto execute(Integer id) {
        return repository.findById(id)
                .map(tipo -> modelMapper.map(tipo, GfdTipoFornecedorGetByIdOutputDto.class))
                .orElse(null);
    }
}
