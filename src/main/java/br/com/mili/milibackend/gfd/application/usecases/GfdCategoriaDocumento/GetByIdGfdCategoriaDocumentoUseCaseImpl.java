package br.com.mili.milibackend.gfd.application.usecases.GfdCategoriaDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdCategoriaDocumento.GfdCategoriaDocumentoGetByIdOutputDto;
import br.com.mili.milibackend.gfd.domain.usecases.gfdCategoriaDocumento.GetByIdGfdCategoriaDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdCategoriaDocumentoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetByIdGfdCategoriaDocumentoUseCaseImpl implements GetByIdGfdCategoriaDocumentoUseCase {
    private final GfdCategoriaDocumentoRepository gfdCategoriaDocumentoRepository;
    private final ModelMapper modelMapper;

    @Override
    public GfdCategoriaDocumentoGetByIdOutputDto execute(Integer id){

        var result = gfdCategoriaDocumentoRepository.findById(id).orElse(null);

        if(result == null) {
            return null;
        }

        return modelMapper.map(result, GfdCategoriaDocumentoGetByIdOutputDto.class);
    }
}
