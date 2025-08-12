package br.com.mili.milibackend.gfd.application.usecases;

import br.com.mili.milibackend.gfd.adapter.exception.GfdFuncionarioCodeException;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioLiberarInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioLiberarOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionarioLiberacao;
import br.com.mili.milibackend.gfd.infra.projections.GfdFuncionarioDocumentsProjection;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioLiberacaoRepository;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LiberarFuncionarioUseCaseImplTest {

    @Mock
    private GfdFuncionarioRepository funcionarioRepository;

    @Mock
    private GfdFuncionarioLiberacaoRepository liberacaoRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private LiberarFuncionarioUseCaseImpl useCase;

    @Captor
    private ArgumentCaptor<GfdFuncionarioLiberacao> liberacaoCaptor;

    private GfdFuncionario funcionario;

    @BeforeEach
    void setup() {
        funcionario = new GfdFuncionario();
        funcionario.setId(1);
        funcionario.setLiberado(0);
    }

    @Test
    void teste_deve_lancar_excecao_quando_funcionario_nao_for_encontrado() {
        when(funcionarioRepository.findById(1)).thenReturn(Optional.empty());

        var input = new GfdFuncionarioLiberarInputDto();
        input.setId(1);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> useCase.execute(input));
        assertEquals(GfdFuncionarioCodeException.GFD_FUNCIONARIO_NAO_ENCONTRADO.getMensagem(), ex.getMessage());

        verify(funcionarioRepository).findById(1);
        verifyNoMoreInteractions(funcionarioRepository, liberacaoRepository);
    }

    @Test
    void teste_deve_lancar_excecao_quando_documentos_pendentes_e_justificativa_vazia() {
        when(funcionarioRepository.findById(1)).thenReturn(Optional.of(funcionario));

        GfdFuncionarioDocumentsProjection documentosMock = mock(GfdFuncionarioDocumentsProjection.class);
        when(documentosMock.getTotalEnviado()).thenReturn(1);


        when(funcionarioRepository.getAllDocuments(1)).thenReturn(documentosMock);

        var input = new GfdFuncionarioLiberarInputDto();
        input.setId(1);
        input.setJustificativa("");
        input.setLiberado(1);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> useCase.execute(input));
        assertEquals(GfdFuncionarioCodeException.GFD_FUNCIONARIO_LIBERACAO_COM_DOCUMENTO_PENDENTE.getMensagem(), ex.getMessage());

        verify(funcionarioRepository).findById(1);
        verify(funcionarioRepository).getAllDocuments(1);
        verifyNoMoreInteractions(liberacaoRepository);
    }

    @Test
    void teste_deve_liberar_funcionario_quando_documentos_pendentes_e_justificativa_preenchida() {
        when(funcionarioRepository.findById(1)).thenReturn(Optional.of(funcionario));

        GfdFuncionarioDocumentsProjection documentosMock = mock(GfdFuncionarioDocumentsProjection.class);
        when(documentosMock.getTotalEnviado()).thenReturn(1);

        when(funcionarioRepository.getAllDocuments(1)).thenReturn(documentosMock);


        var input = new GfdFuncionarioLiberarInputDto();
        input.setId(1);
        input.setJustificativa("Justificativa válida");
        input.setLiberado(1);
        input.setUsuario(12345);

        when(funcionarioRepository.save(any())).thenReturn(funcionario);
        when(modelMapper.map(any(), eq(GfdFuncionarioLiberarOutputDto.class))).thenReturn(new GfdFuncionarioLiberarOutputDto());

        var output = useCase.execute(input);

        assertNotNull(output);
        assertEquals(1, funcionario.getLiberado());

        verify(funcionarioRepository).findById(1);
        verify(funcionarioRepository).getAllDocuments(1);
        verify(funcionarioRepository).save(funcionario);
        verify(liberacaoRepository).save(liberacaoCaptor.capture());

        var liberacaoSalva = liberacaoCaptor.getValue();
        assertEquals(funcionario, liberacaoSalva.getFuncionario());
        assertEquals(input.getLiberado(), liberacaoSalva.getStatusLiberado());
        assertEquals(input.getJustificativa(), liberacaoSalva.getJustificativa());
        assertEquals(input.getUsuario(), liberacaoSalva.getUsuario());
    }

    @Test
    void teste_deve_liberar_funcionario_quando_nao_existirem_documentos_pendentes() {
        when(funcionarioRepository.findById(1)).thenReturn(Optional.of(funcionario));

        GfdFuncionarioDocumentsProjection documentosMock = mock(GfdFuncionarioDocumentsProjection.class);
        when(documentosMock.getTotalEnviado()).thenReturn(0);

        when(funcionarioRepository.getAllDocuments(1)).thenReturn(documentosMock);

        var input = new GfdFuncionarioLiberarInputDto();
        input.setId(1);
        input.setLiberado(1);
        input.setJustificativa(null);
        input.setUsuario(12345);

        when(funcionarioRepository.save(any())).thenReturn(funcionario);
        when(modelMapper.map(any(), eq(GfdFuncionarioLiberarOutputDto.class))).thenReturn(new GfdFuncionarioLiberarOutputDto());

        var output = useCase.execute(input);

        assertNotNull(output);
        assertEquals(1, funcionario.getLiberado());

        verify(funcionarioRepository).findById(1);
        verify(funcionarioRepository).getAllDocuments(1);
        verify(funcionarioRepository).save(funcionario);
        verify(liberacaoRepository).save(any());
    }
}