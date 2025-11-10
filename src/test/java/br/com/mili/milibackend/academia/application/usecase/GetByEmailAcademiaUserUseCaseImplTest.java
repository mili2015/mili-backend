package br.com.mili.milibackend.academia.application.usecase;

import br.com.mili.milibackend.academia.adapter.external.dto.AcademiaExternalGetUserByEmailResponse;
import br.com.mili.milibackend.academia.adapter.external.service.AcademiaExternalService;
import br.com.mili.milibackend.academia.application.dto.AcademiaGetUserByEmailOutputDto;
import br.com.mili.milibackend.shared.exception.types.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetByEmailAcademiaUserUseCaseImplTest {

    @Mock
    private AcademiaExternalService academiaExternalService;

    @InjectMocks
    private GetByEmailAcademiaUserUseCaseImpl useCase;

    @Test
    void teste_deve_lancar_bad_request_quando_email_invalido() {
        assertThatThrownBy(() -> useCase.getUserByEmail("invalid-email"))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void teste_deve_retornar_null_quando_externo_retorna_vazio() {
        String email = "none@example.com";
        when(academiaExternalService.getUserByEmailResponse(email))
                .thenReturn(Optional.empty());

        AcademiaGetUserByEmailOutputDto out = useCase.getUserByEmail(email);
        assertThat(out).isNull();
    }

    @Test
    void teste_deve_mapear_campos_quando_externo_retorna_usuario() {
        String email = "ok@example.com";

        AcademiaExternalGetUserByEmailResponse external = new AcademiaExternalGetUserByEmailResponse();
        external.setId(99);
        external.setName("Alice");
        external.setUrl("https://example.com/user/99");
        external.setDescription("desc");

        AcademiaExternalGetUserByEmailResponse.LinkItem linkItem = new AcademiaExternalGetUserByEmailResponse.LinkItem();
        linkItem.setHref("/courses/1");
        AcademiaExternalGetUserByEmailResponse.Links links = new AcademiaExternalGetUserByEmailResponse.Links();
        links.setCourses(List.of(linkItem));
        external.setLinks(links);

        when(academiaExternalService.getUserByEmailResponse(email))
                .thenReturn(Optional.of(external));

        AcademiaGetUserByEmailOutputDto out = useCase.getUserByEmail(email);

        assertThat(out.getId()).isEqualTo(99);
        assertThat(out.getNome()).isEqualTo("Alice");
        assertThat(out.getUrl()).isEqualTo("https://example.com/user/99");
        assertThat(out.getDescricao()).isEqualTo("desc");
        assertThat(out.getCourses()).hasSize(1);
        assertThat(out.getCourses().get(0).getHref()).isEqualTo("/courses/1");
    }
}
