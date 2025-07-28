package br.com.mili.milibackend.gfd.adapter.web.controller;


import br.com.mili.milibackend.gfd.application.dto.GfdMDocumentosGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.*;
import br.com.mili.milibackend.gfd.application.policy.IGfdPolicy;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdManagerService;
import br.com.mili.milibackend.shared.infra.security.model.CustomUserPrincipal;
import jakarta.transaction.Transactional;
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

import static br.com.mili.milibackend.shared.roles.GfdRolesConstants.ROLE_ANALISTA;
import static br.com.mili.milibackend.shared.roles.GfdRolesConstants.ROLE_FORNECEDOR;


@Slf4j
@RestController
@RequestMapping(GfdMController.ENDPOINT)
public class GfdMController {
    protected static final String ENDPOINT = "/mili-backend/v1/gfd";

    private final IGfdManagerService gfdManagerService;
    private final IGfdPolicy gfdPolicy;

    public GfdMController(IGfdManagerService gfdManagerService, IGfdPolicy gfdPolicy) {
        this.gfdManagerService = gfdManagerService;
        this.gfdPolicy = gfdPolicy;
    }



    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @GetMapping("verificar-fornecedor")
    public ResponseEntity<GfdMVerificarFornecedorOutputDto> verificarFornecedor(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestParam(value = "id", required = false) Integer fornecedorId
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        var inputDto = new GfdMVerificarFornecedorInputDto();
        inputDto.setCodUsuario(user.getIdUser());

        if (gfdPolicy.isAnalista(user)) {
            inputDto.setId(fornecedorId);
        }

        return ResponseEntity.ok(gfdManagerService.verifyFornecedor(inputDto));
    }


    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @GetMapping("fornecedores")
    public ResponseEntity<GfdMFornecedorGetOutputDto> recuperarFornecedor(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestParam(value = "id", required = false) Integer fornecedorId
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        var inputDto = new GfdMFornecedorGetInputDto();
        inputDto.setCodUsuario(user.getIdUser());

        if (gfdPolicy.isAnalista(user)) {
            inputDto.setId(fornecedorId);
        }

        var fornecedor = gfdManagerService.getFornecedor(inputDto);

        return ResponseEntity.ok(fornecedor);
    }

}
