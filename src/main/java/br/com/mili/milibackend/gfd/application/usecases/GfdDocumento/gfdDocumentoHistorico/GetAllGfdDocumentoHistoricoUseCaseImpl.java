package br.com.mili.milibackend.gfd.application.usecases.GfdDocumento.gfdDocumentoHistorico;

import br.com.mili.milibackend.fornecedor.infra.repository.fornecedorRepository.FornecedorRepository;
import br.com.mili.milibackend.gfd.adapter.exception.GfdDocumentoHistoricoCodeException;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.gfdHistoricoDocumento.GfdDocumentoHistoricoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.gfdHistoricoDocumento.GfdDocumentoHistoricoGetAllOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoHistorico;
import br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento.gfdDocumentoHistorico.GetAllGfdDocumentoHistoricoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.gfdDocumento.GfdDocumentoHistoricoRepository;
import br.com.mili.milibackend.gfd.infra.specification.GfdDocumentoHistoricoSpecification;
import br.com.mili.milibackend.shared.exception.types.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllGfdDocumentoHistoricoUseCaseImpl implements GetAllGfdDocumentoHistoricoUseCase {
    private final GfdDocumentoHistoricoRepository repository;
    private final FornecedorRepository fornecedorRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<GfdDocumentoHistoricoGetAllOutputDto> execute(GfdDocumentoHistoricoGetAllInputDto inputDto) {
        var gfdDocumentoHistoricoInput = inputDto.getGfdDocumentoHistorico();

        if (inputDto.getUsuarioId() != null) {
            var fornecedor = fornecedorRepository.findByCodUsuario(inputDto.getUsuarioId()).orElseThrow(() -> new BadRequestException(GfdDocumentoHistoricoCodeException.GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), GfdDocumentoHistoricoCodeException.GFD_FORNECEDOR_NAO_ENCONTRADO.getCode()));
            gfdDocumentoHistoricoInput.setCtforCodigo(fornecedor.getCodigo());
        }

        Specification<GfdDocumentoHistorico> spec = Specification
                .where(GfdDocumentoHistoricoSpecification.filtroFuncionarioId(gfdDocumentoHistoricoInput.getFuncionarioId()))
                .and(GfdDocumentoHistoricoSpecification.filtroStatus(gfdDocumentoHistoricoInput.getStatus()))
                .and(GfdDocumentoHistoricoSpecification.filtroPeriodo(gfdDocumentoHistoricoInput.getPeriodoInicio(), gfdDocumentoHistoricoInput.getPeriodoFim()))
                .and(GfdDocumentoHistoricoSpecification.filtroDocumentoId(gfdDocumentoHistoricoInput.getDocumentoId()))
                .and(GfdDocumentoHistoricoSpecification.filtroFornecedorId(gfdDocumentoHistoricoInput.getCtforCodigo()));

        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "id"));

        var resultPage = repository.findAll(spec, pageRequest);

        return resultPage.getContent().stream()
                .map(this::toDto)
                .toList();
    }

    private GfdDocumentoHistoricoGetAllOutputDto toDto(GfdDocumentoHistorico entity) {
        return modelMapper.map(entity, GfdDocumentoHistoricoGetAllOutputDto.class);
    }


}
