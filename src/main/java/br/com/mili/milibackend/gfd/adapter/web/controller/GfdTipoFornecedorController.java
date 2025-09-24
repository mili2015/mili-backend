package br.com.mili.milibackend.gfd.adapter.web.controller;

import br.com.mili.milibackend.gfd.application.dto.gfdTipoFornecedor.GfdTipoFornecedorGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoFornecedor.GfdTipoFornecedorGetAllOutputDto;
import br.com.mili.milibackend.gfd.domain.usecases.gfdTipoFornecedor.GetAllUseGfdTipoFornecedorCase;
import br.com.mili.milibackend.shared.infra.security.model.CustomUserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static br.com.mili.milibackend.shared.roles.GfdRolesConstants.*;

@Slf4j
@RestController
@RequestMapping(GfdTipoFornecedorController.ENDPOINT) // corrigido aqui
@RequiredArgsConstructor
public class GfdTipoFornecedorController {

    protected static final String ENDPOINT = "/mili-backend/v1/gfd/tipos-fornecedor";

    private final GetAllUseGfdTipoFornecedorCase gfdTipoFornecedorGetAllUseCase;

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
                  "or hasAuthority('" + ROLE_FORNECEDOR + "')" +
                  "or hasAuthority('" + ROLE_VISUALIZACAO + "')" +
                  "or hasAuthority('" + ROLE_SESMT + "')"
    )
    @GetMapping
    public ResponseEntity<List<GfdTipoFornecedorGetAllOutputDto>> getAll(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @ParameterObject @ModelAttribute @Valid GfdTipoFornecedorGetAllInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());
        return ResponseEntity.ok(gfdTipoFornecedorGetAllUseCase.execute(inputDto));
    }
}

