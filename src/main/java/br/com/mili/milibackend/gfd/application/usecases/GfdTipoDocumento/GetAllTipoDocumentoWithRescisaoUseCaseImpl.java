package br.com.mili.milibackend.gfd.application.usecases.GfdTipoDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumento;
import br.com.mili.milibackend.gfd.domain.usecases.GetAllTipoDocumentoUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.GfdTipoDocumento.GetAllTipoDocumentoWithRescisaoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdTipoDocumentoRepository;
import br.com.mili.milibackend.gfd.infra.specification.GfdTipoDocumentoSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class GetAllTipoDocumentoWithRescisaoUseCaseImpl implements GetAllTipoDocumentoWithRescisaoUseCase {

    private final GetAllTipoDocumentoUseCase getAllTipoDocumentoUseCase;
    private final GfdTipoDocumentoRepository gfdTipoDocumentoRepository;
    private final ModelMapper modelMapper;

    @Override
    public GfdTipoDocumentoWithRescisaoGetAllOutputDto execute(GfdTipoDocumentoWithRescisaoGetAllInputDto inputDto) {
        var result = getAllTipoDocumentoUseCase.execute(
                modelMapper.map(inputDto, GfdTipoDocumentoGetAllInputDto.class)
        );

        var listGfdTipoDocumentoDto = result.stream()
                .map(tipoDoc -> modelMapper.map(
                        tipoDoc,
                        GfdTipoDocumentoWithRescisaoGetAllOutputDto.GfdTipoDocumentoDto.class
                ))
                .toList();

        var listCategoriasPossuem = gfdTipoDocumentoRepository.listCategoriaPossuemRescisao().stream()
                .map(categoria -> new GfdTipoDocumentoWithRescisaoGetAllOutputDto.TiposRescisaoDto(
                        categoria.getNome(),
                        categoria.getPossuiRescisao()
                ))
                .toList();

        return new GfdTipoDocumentoWithRescisaoGetAllOutputDto(listGfdTipoDocumentoDto, listCategoriasPossuem);
    }
}
