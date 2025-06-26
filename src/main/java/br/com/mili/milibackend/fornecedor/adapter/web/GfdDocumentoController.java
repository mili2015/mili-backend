package br.com.mili.milibackend.fornecedor.adapter.web;

import br.com.mili.milibackend.fornecedor.application.dto.gfdDocumento.*;
import br.com.mili.milibackend.fornecedor.application.dto.gfdFuncionario.GfdFuncionarioGetAllOutputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdFuncionario.GfdFuncionarioGetAllInputDto;
import br.com.mili.milibackend.fornecedor.domain.interfaces.service.IGfdFuncionarioService;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdDocumentoService;
import br.com.mili.milibackend.shared.infra.security.model.CustomUserPrincipal;
import br.com.mili.milibackend.shared.page.pagination.MyPage;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static br.com.mili.milibackend.shared.roles.GfdRolesConstants.ROLE_ANALISTA;
import static br.com.mili.milibackend.shared.roles.GfdRolesConstants.ROLE_FORNECEDOR;

@Slf4j
@RestController
@RequestMapping(GfdDocumentoController.ENDPOINT)
public class GfdDocumentoController {
    protected static final String ENDPOINT = "/mili-backend/v1/fornecedores/documentos";

    private final IGfdDocumentoService gfdDocumentoService;
    private final IGfdFuncionarioService  gfdFuncionarioService;

    public GfdDocumentoController(IGfdDocumentoService gfdDocumentoService, IGfdFuncionarioService gfdFuncionarioService) {
        this.gfdDocumentoService = gfdDocumentoService;
        this.gfdFuncionarioService = gfdFuncionarioService;
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @PutMapping
    public ResponseEntity<GfdDocumentoUpdateOutputDto> update(
            @RequestBody @Valid GfdDocumentoUpdateInputDto inputDto,
            @AuthenticationPrincipal CustomUserPrincipal user
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());


        return ResponseEntity.ok(gfdDocumentoService.update(inputDto));
    }


    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @GetMapping("{id}/download")
    public ResponseEntity<GfdDocumentoDownloadOutputDto> download(
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserPrincipal user
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        var inputDto = new GfdDocumentoDownloadInputDto(id);
        return ResponseEntity.ok(gfdDocumentoService.download(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserPrincipal user
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        var inputDto = new GfdDocumentoDeleteInputDto(id);
        gfdDocumentoService.delete(inputDto);

        return ResponseEntity.noContent().build();
    }
}
