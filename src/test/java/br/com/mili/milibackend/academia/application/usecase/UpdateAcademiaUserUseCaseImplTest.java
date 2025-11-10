package br.com.mili.milibackend.academia.application.usecase;

import br.com.mili.milibackend.academia.adapter.external.dto.AcademiaExternalUpdateUserRequest;
import br.com.mili.milibackend.academia.adapter.external.dto.AcademiaExternalSaveUserResponse;
import br.com.mili.milibackend.academia.adapter.external.service.AcademiaExternalService;
import br.com.mili.milibackend.academia.application.dto.AcademiaUserSaveOutputDto;
import br.com.mili.milibackend.academia.application.dto.AcademiaUserUpdateInputDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateAcademiaUserUseCaseImplTest {

    @Mock
    private AcademiaExternalService academiaExternalService;

    @InjectMocks
    private UpdateAcademiaUserUseCaseImpl useCase;

    @Test
    void teste_deve_mapear_entrada_chamar_servico_e_retornar_saida_mapeada_quando_executar() {
        AcademiaUserUpdateInputDto input = AcademiaUserUpdateInputDto.builder()
                .userId(55)
                .email("mary@example.com")
                .name("Mary Jane")
                .firstName("Mary")
                .lastName("Jane")
                .build();

        AcademiaExternalSaveUserResponse externalResp = new AcademiaExternalSaveUserResponse();
        externalResp.setId(55);
        externalResp.setUsername("maryj");
        externalResp.setName("Mary Jane");
        externalResp.setEmail("mary@example.com");

        when(academiaExternalService.updateUser(any(AcademiaExternalUpdateUserRequest.class)))
                .thenReturn(externalResp);

        AcademiaUserSaveOutputDto out = useCase.execute(input);

        ArgumentCaptor<AcademiaExternalUpdateUserRequest> reqCap = ArgumentCaptor.forClass(AcademiaExternalUpdateUserRequest.class);
        verify(academiaExternalService).updateUser(reqCap.capture());
        AcademiaExternalUpdateUserRequest sent = reqCap.getValue();
        assertThat(sent.getUserId()).isEqualTo(55);
        assertThat(sent.getEmail()).isEqualTo("mary@example.com");
        assertThat(sent.getName()).isEqualTo("Mary Jane");
        assertThat(sent.getFirstName()).isEqualTo("Mary");
        assertThat(sent.getLastName()).isEqualTo("Jane");

        assertThat(out.getId()).isEqualTo(55);
        assertThat(out.getUsername()).isEqualTo("maryj");
        assertThat(out.getNome()).isEqualTo("Mary Jane");
        assertThat(out.getEmail()).isEqualTo("mary@example.com");
    }
}
