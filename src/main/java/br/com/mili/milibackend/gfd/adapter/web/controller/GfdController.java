package br.com.mili.milibackend.gfd.adapter.web.controller;


import br.com.mili.milibackend.gfd.application.dto.GfdFornecedorGetInputDto;
import br.com.mili.milibackend.gfd.application.dto.GfdFornecedorGetOutputDto;
import br.com.mili.milibackend.gfd.application.dto.GfdVerificarDocumentosInputDto;
import br.com.mili.milibackend.gfd.application.dto.GfdVerificarDocumentosOutputDto;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdManagerService;
import br.com.mili.milibackend.infra.security.model.CustomUserPrincipal;
import br.com.mili.milibackend.shared.roles.SisavRolesConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping(GfdController.ENDPOINT)
public class GfdController {
    protected static final String ENDPOINT = "/gfd/v1";

    @Autowired
    private IGfdManagerService gfdManagerService;

    @PreAuthorize("hasAuthority('" + SisavRolesConstants.ROLE_AGENDA + "')")
    @GetMapping("verificar-docs")
    public ResponseEntity<GfdVerificarDocumentosOutputDto> verificarDocumentos(@AuthenticationPrincipal CustomUserPrincipal user
    ) {

        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        var inputDto = new GfdVerificarDocumentosInputDto();
        return ResponseEntity.ok(gfdManagerService.verifyDocumentos(inputDto));
    }

    @GetMapping("fornecedores")
    public ResponseEntity<GfdFornecedorGetOutputDto> recuperarFornecedor(@AuthenticationPrincipal CustomUserPrincipal user
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        GfdFornecedorGetInputDto inputDto = new GfdFornecedorGetInputDto(11545);
        return ResponseEntity.ok(gfdManagerService.getFornecedor(inputDto));
    }
}
