package br.com.mili.milibackend.gfd.application.usecases.GfdFuncionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioUpdateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioUpdateOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.gfd.domain.usecases.GfdResponsavelIntegracao.SendEmailResponsavelIntegracaoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdLocalTrabalhoRepository;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateGfdFuncionarioUseCaseImplTest {

    @Mock
    private GfdFuncionarioRepository funcionarioRepository;

    @Mock
    private GfdLocalTrabalhoRepository localTrabalhoRepository;

    @Mock
    private SendEmailResponsavelIntegracaoUseCase sendEmailResponsavelIntegracaoUseCase;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UpdateGfdFuncionarioUseCaseImpl updateUseCase;

    private GfdFuncionario funcionario;
    private GfdFuncionarioUpdateInputDto inputDto;

    @BeforeEach
    void setUp() {
        funcionario = new GfdFuncionario();
        funcionario.setId(1);
        funcionario.setNome("João");

        inputDto = new GfdFuncionarioUpdateInputDto();
        inputDto.setId(1);
        inputDto.setNome("João Atualizado");
        inputDto.setCpf("12345678901");
        inputDto.setDataNascimento(LocalDate.of(1990,1,1));
        inputDto.setFuncao("Analista");
        inputDto.setFornecedor(new GfdFuncionarioUpdateInputDto.FornecedorDto(10));
        inputDto.setLocaisTrabalho(List.of(
                new GfdFuncionarioUpdateInputDto.LocalTrabalhoDto(1),
                new GfdFuncionarioUpdateInputDto.LocalTrabalhoDto(2)
        ));
    }

    @Test
    void execute_shouldUpdateFuncionarioAndLocais() {
        when(funcionarioRepository.findById(1)).thenReturn(Optional.of(funcionario));
        when(modelMapper.map(funcionario, GfdFuncionarioUpdateOutputDto.class))
                .thenReturn(new GfdFuncionarioUpdateOutputDto());

        GfdFuncionarioUpdateOutputDto output = updateUseCase.execute(inputDto);

        assertNotNull(output);
        verify(funcionarioRepository, times(1)).save(funcionario);

        verify(localTrabalhoRepository, times(1)).saveAll(anyList());
    }

    @Test
    void execute_shouldThrowNotFoundException_whenFuncionarioNotExists() {
        when(funcionarioRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            updateUseCase.execute(inputDto);
        });

        assertEquals("GFD_FUNCIONARIO_NAO_ENCONTRADO", exception.getCode());
    }
}
