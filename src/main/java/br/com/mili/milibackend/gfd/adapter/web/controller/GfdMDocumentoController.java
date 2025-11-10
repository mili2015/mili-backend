package br.com.mili.milibackend.gfd.adapter.web.controller;


import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoUpdateStatusObservacaoInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoUpdateStatusObservacaoOutputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.gfdHistoricoDocumento.GfdDocumentoHistoricoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.gfdHistoricoDocumento.GfdDocumentoHistoricoGetAllOutputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.documentos.*;
import br.com.mili.milibackend.gfd.application.policy.IGfdPolicy;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdManagerService;
import br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento.GetAllStatusGfdDocumentsUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento.GetAllSupplierDocumentsUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento.UpdateStatusObservacaoDocumentoUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento.UploadGfdDocumentoUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento.gfdDocumentoHistorico.GetAllGfdDocumentoHistoricoUseCase;
import br.com.mili.milibackend.shared.infra.security.model.CustomUserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.transaction.Transactional;
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
@RequestMapping(GfdMDocumentoController.ENDPOINT)
@RequiredArgsConstructor
public class GfdMDocumentoController {
    protected static final String ENDPOINT = "/mili-backend/v1/gfd";

    private final IGfdManagerService gfdManagerService;
    private final UploadGfdDocumentoUseCase uploadGfdDocumentoUseCase;
    private final UpdateStatusObservacaoDocumentoUseCase updateStatusObservacaoDocumentoUseCase;
    private final GetAllSupplierDocumentsUseCase getAllSupplierDocumentsUseCase;
    private final GetAllStatusGfdDocumentsUseCase getAllStatusGfdDocumentsUseCase;
    private final GetAllGfdDocumentoHistoricoUseCase getAllGfdDocumentoHistoricoUseCase;
    private final IGfdPolicy gfdPolicy;


    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
            "or hasAuthority('" + ROLE_FORNECEDOR + "')" +
            "or hasAuthority('" + ROLE_VISUALIZACAO + "')" +
            "or hasAuthority('" + ROLE_SESMT + "')"
    )
    @GetMapping("verificar-docs")
    public ResponseEntity<GfdMVerificarDocumentosOutputDto> verificarDocumentos(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @ParameterObject @ModelAttribute GfdMVerificarDocumentosInputDto inputDto
    ) {
        log.info("{} {} {}", RequestMethod.GET, ENDPOINT + "/verificar-docs", user.getUsername());

        inputDto.setCodUsuario(user.getIdUser());

        if (gfdPolicy.isFornecedor(user)) {
            inputDto.setAnalista(false);
            inputDto.setId(null);
        } else {
            inputDto.setAnalista(true);
        }

        return ResponseEntity.ok(getAllStatusGfdDocumentsUseCase.execute(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
            "or hasAuthority('" + ROLE_FORNECEDOR + "') " +
            "or hasAuthority('" + ROLE_SESMT + "')")
    @PostMapping("upload")
    @Transactional
    public ResponseEntity<GfdMUploadDocumentoOutputDto> uploadDocumentos(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestBody @Valid GfdMUploadDocumentoInputDto inputDto
    ) {
        log.info("{} {} {}", RequestMethod.POST, ENDPOINT + "/upload", user.getUsername());

        if (gfdPolicy.isFornecedor(user)) {
            inputDto.setId(null);
            inputDto.setAnalista(false);
        } else {
            inputDto.setAnalista(true);
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
        log.info("{} {} {}", RequestMethod.GET, ENDPOINT + "/documentos", user.getUsername());
        inputDto.setUsuario(user.getUsername());
        inputDto.setCodUsuario(user.getIdUser());

        if (gfdPolicy.isFornecedor(user)) {
            inputDto.setAnalista(false);
            inputDto.setId(null);
        } else {
            inputDto.setAnalista(true);
        }

        return ResponseEntity.ok(getAllSupplierDocumentsUseCase.execute(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
            "or hasAuthority('" + ROLE_SESMT + "')"
    )
    @PutMapping("documentos/status-observacao")
    public ResponseEntity<GfdDocumentoUpdateStatusObservacaoOutputDto> updateStatusObservacaoDocumento(
            @RequestBody @Valid GfdDocumentoUpdateStatusObservacaoInputDto inputDto,
            @AuthenticationPrincipal CustomUserPrincipal user
    ) {
        log.info("{} {} {}", RequestMethod.PUT, ENDPOINT + "/documentos/status-observacao", user.getUsername());

        return ResponseEntity.ok(updateStatusObservacaoDocumentoUseCase.execute(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "')" +
            "or hasAuthority('" + ROLE_SESMT + "')")
    @PutMapping("documentos")
    public ResponseEntity<GfdMDocumentoUpdateOutputDto> updateDocumento(
            @RequestBody @Valid GfdMDocumentoUpdateInputDto inputDto,
            @AuthenticationPrincipal CustomUserPrincipal user
    ) {
        log.info("{} {} {}", RequestMethod.PUT, ENDPOINT + "/documentos", user.getUsername());

        inputDto.setCodUsuario(user.getIdUser());

        if (gfdPolicy.isAnalista(user)) {
           inputDto.setAnalista(true);
        } else {
            inputDto.setAnalista(false);
        }

        return ResponseEntity.ok(gfdManagerService.updateDocumento(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
            "or hasAuthority('" + ROLE_FORNECEDOR + "')" +
            "or hasAuthority('" + ROLE_SESMT + "')"
    )
    @DeleteMapping("documentos/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id,
            @ParameterObject @ModelAttribute @Valid GfdMDocumentoDeleteInputDto inputDto,
            @AuthenticationPrincipal CustomUserPrincipal user
    ) {
        log.info("{} {} {}", RequestMethod.DELETE, ENDPOINT + "/documentos/" + id, user.getUsername());

        inputDto.setId(id);
        inputDto.setCodUsuario(user.getIdUser());

        if (gfdPolicy.isFornecedor(user)) {
            inputDto.setAnalista(false);
            inputDto.setFornecedorId(null);
        } else {
            inputDto.setAnalista(true);
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
        log.info("{} {} {}", RequestMethod.GET, ENDPOINT + "/documentos/" + id + "/download", user.getUsername());

        inputDto.setId(id);
        inputDto.setCodUsuario(user.getIdUser());

        if (gfdPolicy.isFornecedor(user)) {
            inputDto.setAnalista(false);
            inputDto.setFornecedorId(null);
        } else {
            inputDto.setAnalista(true);
        }

        return ResponseEntity.ok(gfdManagerService.downloadDocumento(inputDto));
    }


    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
            "or hasAuthority('" + ROLE_FORNECEDOR + "')" +
            "or hasAuthority('" + ROLE_VISUALIZACAO + "')" +
            "or hasAuthority('" + ROLE_SESMT + "')"
    )
    @GetMapping("documentos/{id}/historico")
    @Operation(
            summary = "Retorna o histórico de mudanças do documento",
            description = "Retorna uma lista com o histórico das mudanças de status do documento, " +
                    "sendo possível visualizar tanto o fornecedor quanto o analista"
    )
    public ResponseEntity<List<GfdDocumentoHistoricoGetAllOutputDto>> getHistorico(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @PathVariable("id") Integer idDocumento,
            @ParameterObject @ModelAttribute @Valid GfdDocumentoHistoricoGetAllInputDto inputDto
    ) {
        log.info("{} {} {}", RequestMethod.GET, ENDPOINT + "/documentos/" + idDocumento + "/historico", user.getUsername());

        if (gfdPolicy.isAnalista(user)) {
            inputDto.setUsuarioId(null);
        } else {
            inputDto.setUsuarioId(user.getIdUser());
        }

        inputDto.getGfdDocumentoHistorico().setDocumentoId(idDocumento);

        return ResponseEntity.ok(getAllGfdDocumentoHistoricoUseCase.execute(inputDto));
    }
}
