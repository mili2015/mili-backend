package br.com.mili.milibackend.gfd.adapter.web.controller;


import br.com.mili.milibackend.fornecedor.application.dto.gfdDocumento.*;
import br.com.mili.milibackend.fornecedor.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.gfd.application.dto.GfdMDocumentosGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.*;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdManagerService;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.shared.infra.security.model.CustomUserPrincipal;
import br.com.mili.milibackend.shared.page.pagination.MyPage;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static br.com.mili.milibackend.fornecedor.adapter.exception.GfdDocumentoCodeException.GFD_DOCUMENTO_NAO_ENCONTRADO;
import static br.com.mili.milibackend.shared.roles.GfdRolesConstants.ROLE_ANALISTA;
import static br.com.mili.milibackend.shared.roles.GfdRolesConstants.ROLE_FORNECEDOR;


@Slf4j
@RestController
@RequestMapping(GfdMController.ENDPOINT)
public class GfdMController {
    protected static final String ENDPOINT = "/mili-backend/v1/gfd";

    private final IGfdManagerService gfdManagerService;

    public GfdMController(IGfdManagerService gfdManagerService) {
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
    public ResponseEntity<GfdMVerificarFornecedorOutputDto> verificarFornecedor(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestParam(value = "id", required = false) Integer fornecedorId
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        var inputDto = new GfdMVerificarFornecedorInputDto();
        inputDto.setCodUsuario(user.getIdUser());

        if (isAnalista(user)) {
            inputDto.setId(fornecedorId);
        }

        return ResponseEntity.ok(gfdManagerService.verifyFornecedor(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @GetMapping("verificar-docs")
    public ResponseEntity<GfdMVerificarDocumentosOutputDto> verificarDocumentos(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestParam(value = "id", required = false) Integer fornecedorId,
            @RequestParam(value = "idFuncionario", required = false) Integer idFuncionario
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        var inputDto = new GfdMVerificarDocumentosInputDto();
        inputDto.setCodUsuario(user.getIdUser());
        inputDto.setIdFuncionario(idFuncionario);

        if (isAnalista(user)) {
            inputDto.setId(fornecedorId);
        }

        return ResponseEntity.ok(gfdManagerService.verifyDocumentos(inputDto));
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

        if (isAnalista(user)) {
            inputDto.setId(fornecedorId);
        }

        var fornecedor = gfdManagerService.getFornecedor(inputDto);

        return ResponseEntity.ok(fornecedor);
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @PostMapping("upload")
    @Transactional
    public ResponseEntity<GfdMUploadDocumentoOutputDto> uploadDocumentos(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestBody @Valid GfdMUploadDocumentoInputDto inputDto
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
    @PostMapping("funcionarios")
    @Transactional
    public ResponseEntity<GfdMFuncionarioCreateOutputDto> createFuncionario(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestBody @Valid GfdMFuncionarioCreateInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.POST, ENDPOINT, user.getUsername());

        if (!isAnalista(user)) {
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

        if (!isAnalista(user)) {
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

        if (!isAnalista(user)) {
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

        if (!isAnalista(user)) {
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

        if (!isAnalista(user)) {
            inputDto.setCodUsuario(user.getIdUser());
        }

        return ResponseEntity.ok(gfdManagerService.getAllFuncionarios(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @GetMapping("documentos")
    public ResponseEntity<GfdMDocumentosGetAllOutputDto> getAllDocumentos(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @ParameterObject @ModelAttribute @Valid GfdMDocumentosGetAllInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.POST, ENDPOINT, user.getUsername());
        inputDto.setUsuario(user.getUsername());
        inputDto.setCodUsuario(user.getIdUser());

        if (!isAnalista(user)) {
            inputDto.setId(null);
        }

        return ResponseEntity.ok(gfdManagerService.getAllDocumentos(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @PutMapping("documentos")
    public ResponseEntity<GfdMDocumentoUpdateOutputDto> updateDocumento(
            @RequestBody @Valid GfdMDocumentoUpdateInputDto inputDto,
            @AuthenticationPrincipal CustomUserPrincipal user
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        inputDto.setCodUsuario(user.getIdUser());

        if (isAnalista(user)) {
            inputDto.setCodUsuario(null);
        }

        return ResponseEntity.ok(gfdManagerService.updateDocumento(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @DeleteMapping("documentos/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id,
            @ParameterObject @ModelAttribute @Valid GfdMDocumentoDeleteInputDto inputDto,
            @AuthenticationPrincipal CustomUserPrincipal user
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        inputDto.setId(id);
        inputDto.setCodUsuario(user.getIdUser());

        if (isAnalista(user)) {
            inputDto.setCodUsuario(null);
        }

        gfdManagerService.deleteDocumento(inputDto);

        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @GetMapping("documentos/{id}/download")
    public ResponseEntity<GfdMDocumentoDownloadOutputDto> download(
            @PathVariable Integer id,
            @ParameterObject @ModelAttribute @Valid GfdMDocumentoDownloadInputDto inputDto,
            @AuthenticationPrincipal CustomUserPrincipal user
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        inputDto.setId(id);
        inputDto.setCodUsuario(user.getIdUser());

        if (isAnalista(user)) {
            inputDto.setCodUsuario(null);
        }

        return ResponseEntity.ok(gfdManagerService.downloadDocumento(inputDto));
    }
}
