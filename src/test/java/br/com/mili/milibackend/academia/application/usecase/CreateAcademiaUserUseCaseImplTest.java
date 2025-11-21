package br.com.mili.milibackend.academia.application.usecase;

import br.com.mili.milibackend.academia.adapter.external.dto.AcademiaExternalCreateUserRequest;
import br.com.mili.milibackend.academia.adapter.external.dto.AcademiaExternalSaveUserResponse;
import br.com.mili.milibackend.academia.adapter.external.service.AcademiaExternalService;
import br.com.mili.milibackend.academia.application.dto.AcademiaUserCreateInputDto;
import br.com.mili.milibackend.academia.application.dto.AcademiaUserSaveOutputDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAcademiaUserUseCaseImplTest {

    @Mock
    private AcademiaExternalService academiaExternalService;

    @InjectMocks
    private CreateAcademiaUserUseCaseImpl useCase;

    @Test
    void teste_deve_mapear_entrada_chamar_servico_e_retornar_saida_mapeada_quando_executar() {
        AcademiaUserCreateInputDto input = AcademiaUserCreateInputDto.builder()
                .username("john")
                .email("john@example.com")
                .password("secret")
                .name("John Doe")
                .firstName("John")
                .lastName("Doe")
                .build();

        AcademiaExternalSaveUserResponse externalResp = new AcademiaExternalSaveUserResponse();
        externalResp.setId(123);
        externalResp.setUsername("john");
        externalResp.setName("John Doe");
        externalResp.setEmail("john@example.com");

        when(academiaExternalService.createUser(any(AcademiaExternalCreateUserRequest.class)))
                .thenReturn(externalResp);

        AcademiaUserSaveOutputDto out = useCase.execute(input);

        ArgumentCaptor<AcademiaExternalCreateUserRequest> reqCap = ArgumentCaptor.forClass(AcademiaExternalCreateUserRequest.class);
        verify(academiaExternalService).createUser(reqCap.capture());
        AcademiaExternalCreateUserRequest sent = reqCap.getValue();
        assertThat(sent.getUsername()).isEqualTo("john");
        assertThat(sent.getEmail()).isEqualTo("john@example.com");
        assertThat(sent.getPassword()).isEqualTo("secret");
        assertThat(sent.getName()).isEqualTo("John Doe");
        assertThat(sent.getFirstName()).isEqualTo("John");
        assertThat(sent.getLastName()).isEqualTo("Doe");
        assertThat(sent.getRoles()).containsExactly("subscriber");

        assertThat(out.getId()).isEqualTo(123);
        assertThat(out.getUsername()).isEqualTo("john");
        assertThat(out.getNome()).isEqualTo("John Doe");
        assertThat(out.getEmail()).isEqualTo("john@example.com");
    }
}
