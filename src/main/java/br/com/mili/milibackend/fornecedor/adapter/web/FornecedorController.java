package br.com.mili.milibackend.fornecedor.adapter.web;

import br.com.mili.milibackend.fornecedor.application.dto.FornecedorGetAllInputDto;
import br.com.mili.milibackend.fornecedor.application.dto.FornecedorGetAllOutputDto;
import br.com.mili.milibackend.fornecedor.application.dto.FornecedorMeusDadosUpdateInputDto;
import br.com.mili.milibackend.fornecedor.application.dto.FornecedorMeusDadosUpdateOutputDto;
import br.com.mili.milibackend.fornecedor.application.service.FornecedorService;
import br.com.mili.milibackend.fornecedor.domain.interfaces.service.IFornecedorService;
import br.com.mili.milibackend.gfd.application.policy.GfdPolicy;
import br.com.mili.milibackend.shared.infra.security.model.CustomUserPrincipal;
import br.com.mili.milibackend.shared.page.pagination.MyPage;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

import static br.com.mili.milibackend.shared.roles.GfdRolesConstants.*;

@Slf4j
@RestController
@RequestMapping(FornecedorController.ENDPOINT)
public class FornecedorController {
    protected static final String ENDPOINT = "/mili-backend/v1/fornecedores";

    private final IFornecedorService fornecedorService;
    private final GfdPolicy gfdPolicy;

    public FornecedorController(FornecedorService fornecedorService, GfdPolicy gfdPolicy) {
        this.fornecedorService = fornecedorService;
        this.gfdPolicy = gfdPolicy;
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
                  "or hasAuthority('" + ROLE_FORNECEDOR + "') " +
                  "or hasAuthority('" + ROLE_SESMT + "')")
    @PutMapping
    public ResponseEntity<FornecedorMeusDadosUpdateOutputDto> updateMeusDados(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestBody @Valid FornecedorMeusDadosUpdateInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.PUT, ENDPOINT, user.getUsername());

        inputDto.setCodUsuario(user.getIdUser());

        if (!gfdPolicy.isFornecedor(user)) {
            inputDto.setCodUsuario(null);
        } else {
            inputDto.setId(null);
        }

        var fornecedor = fornecedorService.updateMeusDados(inputDto);

        return ResponseEntity.ok(fornecedor);
    }


    /// @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @GetMapping
    public ResponseEntity<MyPage<FornecedorGetAllOutputDto>> getAll(
            @ParameterObject @ModelAttribute FornecedorGetAllInputDto inputDto,
            @AuthenticationPrincipal CustomUserPrincipal user
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        var fornecedor = fornecedorService.getAll(inputDto);

        return ResponseEntity.ok(fornecedor);
    }
}
