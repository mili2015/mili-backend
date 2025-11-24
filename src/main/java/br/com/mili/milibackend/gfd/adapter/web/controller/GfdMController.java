package br.com.mili.milibackend.gfd.adapter.web.controller;


import br.com.mili.milibackend.gfd.application.dto.manager.fornecedor.GfdMFornecedorGetInputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.fornecedor.GfdMFornecedorGetOutputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.funcionario.GfdMVerificarFornecedorInputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.funcionario.GfdMVerificarFornecedorOutputDto;
import br.com.mili.milibackend.gfd.application.policy.IGfdPolicy;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdManagerService;
import br.com.mili.milibackend.gfd.domain.usecases.gfdManager.VerifyFornecedorUseCase;
import br.com.mili.milibackend.shared.infra.security.model.CustomUserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static br.com.mili.milibackend.shared.roles.GfdRolesConstants.*;

/**
 * Controller para gerenciar os endpoints de GFD
 */
@Slf4j
@RestController
@RequestMapping(GfdMController.ENDPOINT)
@PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
              "or hasAuthority('" + ROLE_FORNECEDOR + "')" +
              "or hasAuthority('" + ROLE_VISUALIZACAO + "')" +
              "or hasAuthority('" + ROLE_SESMT + "')"
)
public class GfdMController {
    protected static final String ENDPOINT = "/mili-backend/v1/gfd";

    private final IGfdManagerService gfdManagerService;
    private final IGfdPolicy gfdPolicy;
    private final VerifyFornecedorUseCase verifyFornecedorUseCase;

    public GfdMController(IGfdManagerService gfdManagerService, IGfdPolicy gfdPolicy, VerifyFornecedorUseCase verifyFornecedorUseCase) {
        this.gfdManagerService = gfdManagerService;
        this.gfdPolicy = gfdPolicy;
        this.verifyFornecedorUseCase = verifyFornecedorUseCase;
    }

    @GetMapping("verificar-fornecedor")
    public ResponseEntity<GfdMVerificarFornecedorOutputDto> verificarFornecedor(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestParam(value = "id", required = false) Integer fornecedorId
    ) {
        log.info("{} {} {}", RequestMethod.GET, ENDPOINT + "/verificar-fornecedor", user.getUsername());

        var inputDto = new GfdMVerificarFornecedorInputDto();
        inputDto.setCodUsuario(user.getIdUser());
        inputDto.setId(fornecedorId);

        if (gfdPolicy.isAnalista(user)) {
            inputDto.setAnalista(true);
        } else {
            inputDto.setAnalista(false);
        }

        return ResponseEntity.ok(verifyFornecedorUseCase.execute(inputDto));
    }

    @GetMapping("fornecedores")
    public ResponseEntity<GfdMFornecedorGetOutputDto> recuperarFornecedor(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestParam(value = "id", required = false) Integer fornecedorId
    ) {
        log.info("{} {} {}", RequestMethod.GET, ENDPOINT + "/fornecedores", user.getUsername());

        var inputDto = new GfdMFornecedorGetInputDto();

        if (gfdPolicy.isAnalista(user)) {
            inputDto.setId(fornecedorId);
            inputDto.setAnalista(true);
        } else {
            inputDto.setCodUsuario(user.getIdUser());
            inputDto.setAnalista(false);
        }

        var fornecedor = gfdManagerService.getFornecedor(inputDto);

        return ResponseEntity.ok(fornecedor);
    }

}
