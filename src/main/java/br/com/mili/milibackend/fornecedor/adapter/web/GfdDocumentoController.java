package br.com.mili.milibackend.fornecedor.adapter.web;

import br.com.mili.milibackend.fornecedor.application.dto.FornecedorGetAllInputDto;
import br.com.mili.milibackend.fornecedor.application.dto.FornecedorGetAllOutputDto;
import br.com.mili.milibackend.fornecedor.application.dto.FornecedorMeusDadosUpdateInputDto;
import br.com.mili.milibackend.fornecedor.application.dto.FornecedorMeusDadosUpdateOutputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdDocumento.GfdDocumentoDownloadInputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdDocumento.GfdDocumentoDownloadOutputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdDocumento.GfdDocumentoUpdateInputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdDocumento.GfdDocumentoUpdateOutputDto;
import br.com.mili.milibackend.fornecedor.application.service.FornecedorService;
import br.com.mili.milibackend.fornecedor.domain.interfaces.service.IFornecedorService;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdDocumentoService;
import br.com.mili.milibackend.shared.infra.security.model.CustomUserPrincipal;
import br.com.mili.milibackend.shared.page.pagination.MyPage;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

import static br.com.mili.milibackend.shared.roles.GfdRolesConstants.ROLE_ANALISTA;

@Slf4j
@RestController
@RequestMapping(GfdDocumentoController.ENDPOINT)
public class GfdDocumentoController {
    protected static final String ENDPOINT = "/v1/fornecedor-documentos";

    private final IGfdDocumentoService gfdDocumentoService;

    public GfdDocumentoController(IGfdDocumentoService gfdDocumentoService) {
        this.gfdDocumentoService = gfdDocumentoService;
    }


    @PutMapping
    public ResponseEntity<GfdDocumentoUpdateOutputDto> update(
            @RequestBody @Valid GfdDocumentoUpdateInputDto inputDto,
            @AuthenticationPrincipal CustomUserPrincipal user
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());


        return ResponseEntity.ok(gfdDocumentoService.update(inputDto));
    }

    @GetMapping("{id}/download")
    public ResponseEntity<GfdDocumentoDownloadOutputDto> download(
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserPrincipal user
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        var inputDto = new GfdDocumentoDownloadInputDto(id);
        return ResponseEntity.ok(gfdDocumentoService.download(inputDto));
    }
}
