package br.com.mili.milibackend.gfd.adapter.web.controller;


import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.*;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao.GfdFuncionarioLiberacaoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao.GfdFuncionarioLiberacaoGetAllOutputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao.GfdFuncionarioLiberarInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao.GfdFuncionarioLiberarOutputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.funcionario.*;
import br.com.mili.milibackend.gfd.application.policy.IGfdPolicy;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdManagerService;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.GetAllGfdFuncionarioLiberacaoUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.LiberarFuncionarioUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.UpdateObservacaoFuncionarioUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.DesactivateFuncionarioUseCase;
import br.com.mili.milibackend.shared.infra.security.model.CustomUserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping(GfdMFuncionarioController.ENDPOINT)
@Tag(name = "GFD - Funcionário", description = "Rotas para manipulação de Funcionários")
@RequiredArgsConstructor
public class GfdMFuncionarioController {
    protected static final String ENDPOINT = "/mili-backend/v1/gfd";

    private final IGfdManagerService gfdManagerService;
    private final LiberarFuncionarioUseCase updateLiberarFuncionarioUseCase;
    private final UpdateObservacaoFuncionarioUseCase updateObservacaoFuncionarioUseCase;
    private final IGfdPolicy gfdPolicy;
    private final DesactivateFuncionarioUseCase desactivateFuncionarioUseCase;
    private final GetAllGfdFuncionarioLiberacaoUseCase getAllGfdFuncionarioLiberacaoUseCase;


    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
            "or hasAuthority('" + ROLE_FORNECEDOR + "')" +
            "or hasAuthority('" + ROLE_SESMT + "')"
    )
    @PostMapping("funcionarios")
    @Transactional
    @Operation(
            summary = "Cria um novo funcionário",
            description = "Retorna o funcionário criado, sendo que se o usuário for fornecedor, o funcionário criado será de seu usuário(empresa)"
    )
    public ResponseEntity<GfdMFuncionarioCreateOutputDto> createFuncionario(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @RequestBody @Valid GfdMFuncionarioCreateInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.POST, ENDPOINT, user.getUsername());

        inputDto.setCodUsuario(user.getIdUser());
        inputDto.setAnalista(!gfdPolicy.isFornecedor(user));

        return ResponseEntity.ok(gfdManagerService.createFuncionario(inputDto));
    }


    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
            "or hasAuthority('" + ROLE_FORNECEDOR + "')" +
            "or hasAuthority('" + ROLE_SESMT + "')"
    )
    @PutMapping("funcionarios/{id}")
    @Transactional
    @Operation(
            summary = "Atualiza um funcionário",
            description = "Retorna o funcionário, sendo que se o usuário for fornecedor, o funcionário atualizado será de seu usuário(empresa)"
    )
    public ResponseEntity<GfdMFuncionarioUpdateOutputDto> updateFuncionario(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @PathVariable Integer id,
            @RequestBody @Valid GfdMFuncionarioUpdateInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.PUT, ENDPOINT, user.getUsername());

        inputDto.setCodUsuario(user.getIdUser());
        inputDto.getFuncionario().setId(id);
        inputDto.setAnalista(!gfdPolicy.isFornecedor(user));

        return ResponseEntity.ok(gfdManagerService.updateFuncionario(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
            "or hasAuthority('" + ROLE_FORNECEDOR + "')" +
            "or hasAuthority('" + ROLE_VISUALIZACAO + "')" +
            "or hasAuthority('" + ROLE_SESMT + "')"
    )
    @GetMapping("funcionarios/{id}")
    @Operation(
            summary = "Retorna um funcionário",
            description = "Retorna o funcionário, sendo que se o usuário for fornecedor, sera retornado o funcionário de seu usuário(empresa)"
    )
    public ResponseEntity<GfdMFuncionarioGetOutputDto> getFuncionario(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @PathVariable Integer id,
            @ParameterObject @ModelAttribute @Valid GfdMFuncionarioGetInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        inputDto.setCodUsuario(user.getIdUser());
        inputDto.setAnalista(gfdPolicy.isAnalista(user));
        inputDto.getFuncionario().setId(id);

        return ResponseEntity.ok(gfdManagerService.getFuncionario(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @DeleteMapping("funcionarios/{id}")
    @Transactional
    @Operation(
            summary = "Deleta um funcionário",
            description = "Não retorna nada, sendo que se o usuário for fornecedor, o funcionário deletado será de seu usuário(empresa)"
    )
    public ResponseEntity<Void> deleteFuncionario(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @PathVariable Integer id,
            @ParameterObject @ModelAttribute @Valid GfdMFuncionarioDeleteInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.DELETE, ENDPOINT, user.getUsername());

        inputDto.setCodUsuario(user.getIdUser());
        inputDto.setAnalista(gfdPolicy.isAnalista(user));

        if (inputDto.getFuncionario() == null) {
            inputDto.setFuncionario(new GfdMFuncionarioDeleteInputDto.GfdFuncionarioDto());
            inputDto.getFuncionario().setFornecedor(new GfdMFuncionarioDeleteInputDto.GfdFuncionarioDto.FornecedorDto());
        }

        inputDto.getFuncionario().setId(id);

        gfdManagerService.deleteFuncionario(inputDto);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') or hasAuthority('" + ROLE_FORNECEDOR + "')")
    @DeleteMapping("funcionarios/{id}/desligar")
    @Transactional
    @Operation(
            summary = "desativa um funcionário",
            description = "Não retorna nada, sendo que se o usuário for fornecedor, o funcionário desativado será de seu usuário(empresa)"
    )
    public ResponseEntity<Void> desativateFuncionario(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @PathVariable Integer id,
            @ParameterObject @ModelAttribute @Valid GfdFuncionarioDesactivateInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.DELETE, ENDPOINT, user.getUsername());

        inputDto.setCodUsuario(user.getIdUser());
        inputDto.setAnalista(gfdPolicy.isAnalista(user));

        inputDto.getFuncionario().setId(id);

        desactivateFuncionarioUseCase.execute(inputDto);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
            "or hasAuthority('" + ROLE_SESMT + "')"
    )
    @PutMapping("funcionarios/{id}/observacao")
    @Transactional
    @Operation(
            summary = "Atualiza a observação de um funcionário",
            description = "Retorna o funcionário. Faz a liberação do funcionário, caso o usuario tenha " +
                    "algum documento pendente a justificativa se torna obrigatório"
    )
    public ResponseEntity<GfdFuncionarioUpdateObservacaoOutputDto> updateObservacao(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @PathVariable Integer id,
            @RequestBody @Valid GfdFuncionarioUpdateObservacaoInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.PUT, ENDPOINT, user.getUsername());
        inputDto.setId(id);

        return ResponseEntity.ok(updateObservacaoFuncionarioUseCase.execute(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
            "or hasAuthority('" + ROLE_SESMT + "')"
    )
    @PutMapping("funcionarios/{id}/liberar")
    @Transactional
    @Operation(
            summary = "Atualiza o status de liberação de um funcionário",
            description = "Retorna o funcionário. Faz a liberação do funcionário, caso o usuario tenha " +
                    "algum documento pendente a justificativa se torna obrigatório"
    )
    public ResponseEntity<GfdFuncionarioLiberarOutputDto> liberar(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @PathVariable Integer id,
            @RequestBody @Valid GfdFuncionarioLiberarInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.PUT, ENDPOINT, user.getUsername());
        inputDto.setId(id);
        inputDto.setUsuario(user.getIdUser());

        return ResponseEntity.ok(updateLiberarFuncionarioUseCase.execute(inputDto));
    }

    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "') " +
            "or hasAuthority('" + ROLE_FORNECEDOR + "')" +
            "or hasAuthority('" + ROLE_VISUALIZACAO + "')" +
            "or hasAuthority('" + ROLE_SESMT + "')"
    )
    @GetMapping("funcionarios")
    @Operation(
            summary = "Retorna todos os funcionários",
            description = "Retorna todos os funcionários, sendo que se o usuário for fornecedor, sera retornado os funcionários de seu usuário(empresa)"
    )
    public ResponseEntity<GfdMFuncionarioGetAllOutputDto> getAllFuncionario(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @ParameterObject @ModelAttribute @Valid GfdMFuncionarioGetAllInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        inputDto.setCodUsuario(user.getIdUser());

        if (gfdPolicy.isFornecedor(user)) {
            inputDto.setAnalista(false);
        } else {
            inputDto.setAnalista(true);
        }

        return ResponseEntity.ok(gfdManagerService.getAllFuncionarios(inputDto));
    }


    @PreAuthorize("hasAuthority('" + ROLE_ANALISTA + "')")
    @GetMapping("funcionarios/{id}/liberacoes")
    @Operation(
            summary = "Retorna o histótico de liberações do funcionário",
            description = "Retornar o historico de liberações do funcionário, sendo so acessivel por analistas"
    )
    public ResponseEntity<List<GfdFuncionarioLiberacaoGetAllOutputDto>> getAllLiberacoesFuncionario(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @PathVariable Integer id,
            @ParameterObject @ModelAttribute @Valid GfdFuncionarioLiberacaoGetAllInputDto inputDto
    ) {
        log.info("{} {}/{}", RequestMethod.GET, ENDPOINT, user.getUsername());

        inputDto.setFuncionarioId(id);
        return ResponseEntity.ok(getAllGfdFuncionarioLiberacaoUseCase.execute(inputDto));
    }
}
