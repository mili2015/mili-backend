package br.com.mili.milibackend.gfd.adapter.web.controller;


import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.*;
import br.com.mili.milibackend.gfd.application.usecases.GfdTipoDocumento.GfdTipoDocumentoWithRescisaoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.usecases.GfdTipoDocumento.GfdTipoDocumentoWithRescisaoGetAllOutputDto;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdTipoDocumentoService;
import br.com.mili.milibackend.gfd.domain.usecases.GetAllTipoDocumentoUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.GfdTipoDocumento.GetAllTipoDocumentoWithRescisaoUseCase;
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
@RequestMapping(GfdTipoDocumentoController.ENDPOINT)
@RequiredArgsConstructor
public class GfdTipoDocumentoController {
    protected static final String ENDPOINT = "/mili-backend/v1/gfd/tipo-documentos";

    private final IGfdTipoDocumentoService gfdTipoDocumentoService;
    private final GetAllTipoDocumentoUseCase getAllTipoDocumentoUseCase ;
    private final GetAllTipoDocumentoWithRescisaoUseCase getAllTipoDocumentoWithRescisaoUseCase;

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
                  "or hasAuthority('" + ROLE_FORNECEDOR + "')" +
                  "or hasAuthority('" + ROLE_VISUALIZACAO + "')" +
                  "or hasAuthority('" + ROLE_SESMT + "')"
    )
    @GetMapping()
    public ResponseEntity<List<GfdTipoDocumentoGetAllOutputDto>> getAll(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @ParameterObject @ModelAttribute @Valid GfdTipoDocumentoGetAllInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        return ResponseEntity.ok(getAllTipoDocumentoUseCase.execute(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
                  "or hasAuthority('" + ROLE_FORNECEDOR + "')" +
                  "or hasAuthority('" + ROLE_VISUALIZACAO + "')" +
                  "or hasAuthority('" + ROLE_SESMT + "')"
    )
    @GetMapping("/rescisao")
    public ResponseEntity<GfdTipoDocumentoWithRescisaoGetAllOutputDto> getAllWithRescisao(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @ParameterObject @ModelAttribute @Valid GfdTipoDocumentoWithRescisaoGetAllInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        return ResponseEntity.ok(getAllTipoDocumentoWithRescisaoUseCase.execute(inputDto));
    }

       @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
                  "or hasAuthority('" + ROLE_FORNECEDOR + "')" +
                  "or hasAuthority('" + ROLE_VISUALIZACAO + "')" +
                  "or hasAuthority('" + ROLE_SESMT + "')"
    )
    @GetMapping("{id}")
    public ResponseEntity<GfdTipoDocumentoGetByIdOutputDto> getById(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @PathVariable Integer id
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        return ResponseEntity.ok(gfdTipoDocumentoService.getById(id));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> inactive(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @PathVariable Integer id
    ) {
        log.info("{} {}/{}", RequestMethod.DELETE, ENDPOINT, user.getUsername());

        gfdTipoDocumentoService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "')")
    @PutMapping("{id}")
    public ResponseEntity<GfdTipoDocumentoUpdateOutputDto> update(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestBody @Valid GfdTipoDocumentoUpdateInputDto inputDto,
            @PathVariable Integer id
    ) {
        log.info("{} {}/{}", RequestMethod.PUT, ENDPOINT, user.getUsername());

        inputDto.setId(id);

        return ResponseEntity.ok(gfdTipoDocumentoService.update(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "')")
    @PostMapping()
    public ResponseEntity<GfdTipoDocumentoCreateOutputDto> create(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestBody @Valid GfdTipoDocumentoCreateInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.POST, ENDPOINT, user.getUsername());

        return ResponseEntity.ok(gfdTipoDocumentoService.create(inputDto));
    }

}
