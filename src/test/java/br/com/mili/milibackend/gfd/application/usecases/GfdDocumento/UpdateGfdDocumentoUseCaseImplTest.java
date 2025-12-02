package br.com.mili.milibackend.gfd.application.usecases.GfdDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoUpdateInputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumento;
import br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento.CreateDocumentoPeriodoUseCase;
import br.com.mili.milibackend.gfd.infra.projections.GfdDocumentCountProjection;
import br.com.mili.milibackend.gfd.infra.projections.GfdFuncionarioDocumentsProjection;
import br.com.mili.milibackend.gfd.infra.repository.gfdDocumento.GfdDocumentoHistoricoRepository;
import br.com.mili.milibackend.gfd.infra.repository.gfdDocumento.GfdDocumentoRepository;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateGfdDocumentoUseCaseImplTest {

    @InjectMocks
    private UpdateGfdDocumentoUseCaseImpl useCase;

    @Mock
    private GfdDocumentoRepository gfdDocumentoRepository;
    @Mock
    private CreateDocumentoPeriodoUseCase createDocumentoPeriodoUseCase;
    @Mock
    private GfdDocumentoHistoricoRepository gfdDocumentoHistoricoRepository;
    @Mock
    private GfdFuncionarioRepository gfdFuncionarioRepository;
    @Mock
    private ModelMapper modelMapper;

    private GfdDocumento buildDocumentoEntity(Integer docId, Integer ctforCodigo, Integer funcionarioId) {
        GfdDocumento doc = new GfdDocumento();
        doc.setId(docId);
        doc.setCtforCodigo(ctforCodigo);
        doc.setDataEmissao(LocalDate.now());
        doc.setDataValidade(LocalDate.now());
        doc.setStatus(GfdDocumentoStatusEnum.CONFORME);

        GfdTipoDocumento tipo = new GfdTipoDocumento();
        tipo.setId(5);
        tipo.setClassificacao("EMPRESA");
        tipo.setDiasValidade(30);
        doc.setGfdTipoDocumento(tipo);

        if (funcionarioId != null) {
            GfdFuncionario func = new GfdFuncionario();
            func.setId(funcionarioId);
            doc.setGfdFuncionario(func);
        }
        return doc;
    }

    private GfdDocumentoUpdateInputDto buildInput(Integer idDocumento, Integer codUsuario) {
        return GfdDocumentoUpdateInputDto.builder()
                .documento(idDocumento, LocalDate.now(), LocalDate.now().plusDays(10), GfdDocumentoStatusEnum.CONFORME.name(), "obs")
                .codUsuario(codUsuario)
                .periodo(LocalDate.now())
                .build();
    }

    @Test
    void deve_liberar_funcionario_quando_documentos_sem_pendencias() {
        // arrange
        final Integer documentoId = 100;
        final Integer ctforCodigo = 77;
        final Integer funcionarioId = 10;

        var input = buildInput(documentoId, 999);
        var entidade = buildDocumentoEntity(documentoId, ctforCodigo, funcionarioId);

        when(gfdDocumentoRepository.findById(documentoId)).thenReturn(Optional.of(entidade));
        when(gfdDocumentoRepository.save(any(GfdDocumento.class))).thenReturn(entidade);

        GfdDocumentCountProjection fornecedorProj = mock(GfdDocumentCountProjection.class);
        when(fornecedorProj.getNaoEnviado()).thenReturn(0);
        when(fornecedorProj.getTotalEnviado()).thenReturn(0);
        when(fornecedorProj.getTotalEmAnalise()).thenReturn(0);
        when(fornecedorProj.getTotalNaoConforme()).thenReturn(0);
        when(gfdDocumentoRepository.getAllCount(eq(ctforCodigo), any(LocalDate.class))).thenReturn(fornecedorProj);

        GfdFuncionarioDocumentsProjection funcProj = mock(GfdFuncionarioDocumentsProjection.class);
        when(funcProj.getNaoEnviado()).thenReturn(0);
        when(funcProj.getTotalEnviado()).thenReturn(0);
        when(funcProj.getTotalEmAnalise()).thenReturn(0);
        when(funcProj.getTotalNaoConforme()).thenReturn(0);
        when(gfdFuncionarioRepository.getAllDocuments(eq(funcionarioId), any(LocalDate.class))).thenReturn(funcProj);

        // act
        useCase.execute(input);

        // assert: sem pendencias => libera funcionario (1)
        verify(gfdFuncionarioRepository).updateLiberado(funcionarioId, 1);
        // e nao mexe no fornecedor
        verify(gfdFuncionarioRepository, never()).updateLiberadoFornecedor(anyInt(), anyInt());
        // historico salvo
        verify(gfdDocumentoHistoricoRepository).save(any());
    }

/*    @Test
    void deve_bloquear_fornecedor_quando_documentos_pendentes() {
        // arrange
        final Integer documentoId = 200;
        final Integer ctforCodigo = 99;

        var input = buildInput(documentoId, 1000);
        var entidade = buildDocumentoEntity(documentoId, ctforCodigo, null); // documento da empresa

        when(gfdDocumentoRepository.findById(documentoId)).thenReturn(Optional.of(entidade));
        when(gfdDocumentoRepository.save(any(GfdDocumento.class))).thenReturn(entidade);

        GfdDocumentCountProjection fornecedorProj = mock(GfdDocumentCountProjection.class);
        when(fornecedorProj.getNaoEnviado()).thenReturn(1); // pendente
        when(fornecedorProj.getTotalEnviado()).thenReturn(0);
        when(fornecedorProj.getTotalEmAnalise()).thenReturn(0);
        when(fornecedorProj.getTotalNaoConforme()).thenReturn(0);
        when(gfdDocumentoRepository.getAllCount(eq(ctforCodigo), any(LocalDate.class))).thenReturn(fornecedorProj);

        // act
        useCase.execute(input);

        // assert: pendencias no fornecedor => atualizar todos funcionarios para nao liberado (0)
        verify(gfdFuncionarioRepository).updateLiberadoFornecedor(ctforCodigo, 0);
        // e nao chamou updateLiberado individual
        verify(gfdFuncionarioRepository, never()).updateLiberado(anyInt(), anyInt());
        verify(gfdDocumentoHistoricoRepository).save(any());
    }*/
}
