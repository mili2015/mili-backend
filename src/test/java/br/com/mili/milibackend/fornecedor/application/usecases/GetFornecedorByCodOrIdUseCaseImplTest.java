package br.com.mili.milibackend.fornecedor.application.usecases;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.infra.repository.fornecedorRepository.FornecedorRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_FORNECEDOR_NAO_ENCONTRADO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class GetFornecedorByCodOrIdUseCaseImplTest {

    @Mock
    private FornecedorRepository fornecedorRepository;

    @InjectMocks
    private GetFornecedorByCodOrIdUseCaseImpl useCase;

    private Fornecedor fornecedor;

    @BeforeEach
    public void setUp() {
        fornecedor = new Fornecedor();
        fornecedor.setCodigo(1);
        fornecedor.setRazaoSocial("Fornecedor 1");
    }


    @Test
    public void test_deve_buscar_fornecedor_quando_enviar_id() {
        when(fornecedorRepository.findById(1)).thenReturn(Optional.of(fornecedor));

        // Act
        var fornecedorOut = useCase.execute(null, 1, true);

        // Assert
        assertEquals(fornecedor.getCodigo(), fornecedorOut.getCodigo());
    }

    @Test
    public void test_deve_lancar_excecao_quando_nao_encontrar_por_id_fornecedor() {
        when(fornecedorRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        NotFoundException ex = assertThrows( NotFoundException.class, () -> useCase.execute(null, 1, true));

        // Assert
        assertEquals(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), ex.getMessage());
        assertEquals(GFD_FORNECEDOR_NAO_ENCONTRADO.getCode(), ex.getCode());
    }

    @Test
    public void test_deve_buscar_fornecedor_quando_enviar_cod() {
        when(fornecedorRepository.findByCodUsuario(1)).thenReturn(Optional.of(fornecedor));

        // Act
        var fornecedorOut = useCase.execute(1, null, false);

        // Assert
        assertEquals(fornecedor.getCodigo(), fornecedorOut.getCodigo());
    }

    @Test
    public void test_deve_lancar_excecao_quando_nao_encontrar_cod_fornecedor() {
        when(fornecedorRepository.findByCodUsuario(1)).thenReturn(Optional.empty());

        // Act
        NotFoundException ex = assertThrows( NotFoundException.class, () -> useCase.execute(1, null, false));

        // Assert
        assertEquals(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), ex.getMessage());
        assertEquals(GFD_FORNECEDOR_NAO_ENCONTRADO.getCode(), ex.getCode());
    }
}