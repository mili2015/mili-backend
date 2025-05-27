package br.com.mili.milibackend.gfd.adapter.web.controller;


import br.com.mili.milibackend.gfd.application.dto.GfdDocumentosGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.*;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdManagerService;
import br.com.mili.milibackend.shared.infra.security.model.CustomUserPrincipal;
import br.com.mili.milibackend.shared.roles.GfdRolesConstants;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static br.com.mili.milibackend.shared.roles.GfdRolesConstants.ROLE_ANALISTA;
import static br.com.mili.milibackend.shared.roles.GfdRolesConstants.ROLE_FORNECEDOR;


@Slf4j
@RestController
@RequestMapping(GfdController.ENDPOINT)
public class GfdController {
    protected static final String ENDPOINT = "/v1/gfd";

    private final IGfdManagerService gfdManagerService;

    public GfdController(IGfdManagerService gfdManagerService) {
        this.gfdManagerService = gfdManagerService;
    }

    private boolean isAnalista(CustomUserPrincipal user) {
        Set<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        return roles.contains(ROLE_ANALISTA);
    }


   @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @GetMapping("verificar-fornecedor")
    public ResponseEntity<GfdVerificarFornecedorOutputDto> verificarFornecedor(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestParam(value = "id", required = false) Integer fornecedorId
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        var inputDto = new GfdVerificarFornecedorInputDto();
        inputDto.setCodUsuario(user.getIdUser());

        if (isAnalista(user)) {
            inputDto.setId(fornecedorId);
        }

        return ResponseEntity.ok(gfdManagerService.verifyFornecedor(inputDto));
    }
   @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @GetMapping("verificar-docs")
    public ResponseEntity<List<GfdVerificarDocumentosOutputDto>> verificarDocumentos(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestParam(value = "id", required = false) Integer fornecedorId
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        var inputDto = new GfdVerificarDocumentosInputDto();
        inputDto.setCodUsuario(user.getIdUser());

        if (isAnalista(user)) {
            inputDto.setId(fornecedorId);
        }

        return ResponseEntity.ok(gfdManagerService.verifyDocumentos(inputDto));
    }

   @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @GetMapping("fornecedores")
    public ResponseEntity<GfdFornecedorGetOutputDto> recuperarFornecedor(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestParam(value = "id", required = false) Integer fornecedorId
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        var inputDto = new GfdFornecedorGetInputDto();
        inputDto.setCodUsuario(user.getIdUser());

        if (isAnalista(user)) {
            inputDto.setId(fornecedorId);
        }

        var fornecedor = gfdManagerService.getFornecedor(inputDto);

        return ResponseEntity.ok(fornecedor);
    }

   @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @PostMapping("upload")
    public ResponseEntity<GfdUploadDocumentoOutputDto> uploadDocumentos(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestBody @Valid GfdUploadDocumentoInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.POST, ENDPOINT, user.getUsername());

        if (!isAnalista(user)) {
            inputDto.setId(null);
        }

        inputDto.setUsuario(user.getUsername());
        inputDto.setCodUsuario(user.getIdUser());

        return ResponseEntity.ok(gfdManagerService.uploadDocumento(inputDto));
    }

   @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @GetMapping("documentos")
    public ResponseEntity<GfdDocumentosGetAllOutputDto> getAllDocumentos(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @ParameterObject @ModelAttribute @Valid GfdDocumentosGetAllInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.POST, ENDPOINT, user.getUsername());
        inputDto.setUsuario(user.getUsername());
        inputDto.setCodUsuario(user.getIdUser());

        if (!isAnalista(user)) {
            inputDto.setId(null);
        }

        return ResponseEntity.ok(gfdManagerService.getAllDocumentos(inputDto));
    }
}
