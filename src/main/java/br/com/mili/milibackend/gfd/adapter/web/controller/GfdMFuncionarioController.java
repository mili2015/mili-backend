package br.com.mili.milibackend.gfd.adapter.web.controller;


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
@RequestMapping(GfdMFuncionarioController.ENDPOINT)
public class GfdMFuncionarioController {
    protected static final String ENDPOINT = "/mili-backend/v1/gfd";

    private final IGfdManagerService gfdManagerService;
    private final IGfdPolicy gfdPolicy;


    public GfdMFuncionarioController(IGfdManagerService gfdManagerService, IGfdPolicy gfdPolicy) {
        this.gfdManagerService = gfdManagerService;
        this.gfdPolicy = gfdPolicy;
    }


    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @PostMapping("funcionarios")
    @Transactional
    public ResponseEntity<GfdMFuncionarioCreateOutputDto> createFuncionario(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestBody @Valid GfdMFuncionarioCreateInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.POST, ENDPOINT, user.getUsername());

        if (gfdPolicy.isFornecedor(user)) {
            inputDto.setCodUsuario(user.getIdUser());
        }

        return ResponseEntity.ok(gfdManagerService.createFuncionario(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @PutMapping("funcionarios/{id}")
    @Transactional
    public ResponseEntity<GfdMFuncionarioUpdateOutputDto> updateFuncionario(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @PathVariable Integer id,
            @RequestBody @Valid GfdMFuncionarioUpdateInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.PUT, ENDPOINT, user.getUsername());

        if (gfdPolicy.isFornecedor(user)) {
            inputDto.setCodUsuario(user.getIdUser());
        }

        inputDto.getFuncionario().setId(id);

        return ResponseEntity.ok(gfdManagerService.updateFuncionario(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @GetMapping("funcionarios/{id}")
    @Transactional
    public ResponseEntity<GfdMFuncionarioGetOutputDto> getFuncionario(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @PathVariable Integer id,
            @ParameterObject @ModelAttribute @Valid GfdMFuncionarioGetInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        if (gfdPolicy.isFornecedor(user)) {
            inputDto.setCodUsuario(user.getIdUser());
        }

        inputDto.getFuncionario().setId(id);

        return ResponseEntity.ok(gfdManagerService.getFuncionario(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @DeleteMapping("funcionarios/{id}")
    @Transactional
    public ResponseEntity<Void> deleteFuncionario(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @PathVariable Integer id,
            @RequestBody @Valid GfdMFuncionarioDeleteInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.DELETE, ENDPOINT, user.getUsername());

        if (gfdPolicy.isFornecedor(user)) {
            inputDto.setCodUsuario(user.getIdUser());
        }

        inputDto.getFuncionario().setId(id);

        gfdManagerService.deleteFuncionario(inputDto);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @GetMapping("funcionarios")
    public ResponseEntity<GfdMFuncionarioGetAllOutputDto> getAllFuncionario(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @ParameterObject @ModelAttribute @Valid GfdMFuncionarioGetAllInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        if (gfdPolicy.isFornecedor(user)) {
            inputDto.setCodUsuario(user.getIdUser());
        }

        return ResponseEntity.ok(gfdManagerService.getAllFuncionarios(inputDto));
    }
}
