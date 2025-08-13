package br.com.mili.milibackend.gfd.adapter.web.controller;


import br.com.mili.milibackend.gfd.application.dto.*;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoUpdateStatusObservacaoInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoUpdateStatusObservacaoOutputDto;
import br.com.mili.milibackend.gfd.application.policy.IGfdPolicy;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdManagerService;
import br.com.mili.milibackend.gfd.domain.usecases.UpdateStatusObservacaoDocumentoUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.UploadGfdDocumentoUseCase;
import br.com.mili.milibackend.shared.infra.security.model.CustomUserPrincipal;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static br.com.mili.milibackend.shared.roles.GfdRolesConstants.*;


@Slf4j
@RestController
@RequestMapping(GfdMDocumentoController.ENDPOINT)
@RequiredArgsConstructor
public class GfdMDocumentoController {
    protected static final String ENDPOINT = "/mili-backend/v1/gfd";

    private final IGfdManagerService gfdManagerService;
    private final UploadGfdDocumentoUseCase uploadGfdDocumentoUseCase;
    private final UpdateStatusObservacaoDocumentoUseCase updateStatusObservacaoDocumentoUseCase;
    private final IGfdPolicy gfdPolicy;


    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
                  "or hasAuthority('" + ROLE_FORNECEDOR + "')" +
                  "or hasAuthority('" + ROLE_VISUALIZACAO + "')" +
                  "or hasAuthority('" + ROLE_SESMT + "')"
    )
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

        if (gfdPolicy.isAnalista(user)) {
            inputDto.setId(fornecedorId);
        }

        return ResponseEntity.ok(gfdManagerService.verifyDocumentos(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @PostMapping("upload")
    @Transactional
    public ResponseEntity<GfdMUploadDocumentoOutputDto> uploadDocumentos(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestBody @Valid GfdMUploadDocumentoInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.POST, ENDPOINT, user.getUsername());

        if (gfdPolicy.isFornecedor(user)) {
            inputDto.setId(null);
        }

        inputDto.setUsuario(user.getUsername());
        inputDto.setCodUsuario(user.getIdUser());

        return ResponseEntity.ok(uploadGfdDocumentoUseCase.execute(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
                  "or hasAuthority('" + ROLE_FORNECEDOR + "')" +
                  "or hasAuthority('" + ROLE_VISUALIZACAO + "')" +
                  "or hasAuthority('" + ROLE_SESMT + "')"
    )
    @GetMapping("documentos")
    public ResponseEntity<GfdMDocumentosGetAllOutputDto> getAllDocumentos(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @ParameterObject @ModelAttribute @Valid GfdMDocumentosGetAllInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.POST, ENDPOINT, user.getUsername());
        inputDto.setUsuario(user.getUsername());
        inputDto.setCodUsuario(user.getIdUser());

        if (gfdPolicy.isFornecedor(user)) {
            inputDto.setId(null);
        }

        return ResponseEntity.ok(gfdManagerService.getAllDocumentos(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
                  "or hasAuthority('" + ROLE_SESMT + "')"
    )
    @PutMapping("documentos/status-observacao")
    public ResponseEntity<GfdDocumentoUpdateStatusObservacaoOutputDto> updateStatusObservacaoDocumento(
            @RequestBody @Valid GfdDocumentoUpdateStatusObservacaoInputDto inputDto,
            @AuthenticationPrincipal CustomUserPrincipal user
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        return ResponseEntity.ok(updateStatusObservacaoDocumentoUseCase.execute(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "')")
    @PutMapping("documentos")
    public ResponseEntity<GfdMDocumentoUpdateOutputDto> updateDocumento(
            @RequestBody @Valid GfdMDocumentoUpdateInputDto inputDto,
            @AuthenticationPrincipal CustomUserPrincipal user
    ) {
        log.info("{} {}/{}", RequestMethod.PUT, ENDPOINT, user.getUsername());

        inputDto.setCodUsuario(user.getIdUser());

        if (gfdPolicy.isAnalista(user)) {
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

        if (gfdPolicy.isAnalista(user)) {
            inputDto.setCodUsuario(null);
        }

        gfdManagerService.deleteDocumento(inputDto);

        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
                  "or hasAuthority('" + ROLE_FORNECEDOR + "')" +
                  "or hasAuthority('" + ROLE_SESMT + "')"
    )
    @GetMapping("documentos/{id}/download")
    public ResponseEntity<GfdMDocumentoDownloadOutputDto> download(
            @PathVariable Integer id,
            @ParameterObject @ModelAttribute @Valid GfdMDocumentoDownloadInputDto inputDto,
            @AuthenticationPrincipal CustomUserPrincipal user
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        inputDto.setId(id);
        inputDto.setCodUsuario(user.getIdUser());

        if (gfdPolicy.isAnalista(user)) {
            inputDto.setCodUsuario(null);
        }

        return ResponseEntity.ok(gfdManagerService.downloadDocumento(inputDto));
    }
}
