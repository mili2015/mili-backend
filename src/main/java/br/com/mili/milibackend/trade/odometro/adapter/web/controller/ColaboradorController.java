package br.com.mili.milibackend.trade.odometro.adapter.web.controller;

import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeColaboradorGetAllInputDto;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeColaboradorGetAllOutputDto;
import br.com.mili.milibackend.trade.odometro.domain.usecase.GetAllColaboradorUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static br.com.mili.milibackend.shared.roles.GfdRolesConstants.*;
import static br.com.mili.milibackend.shared.roles.GfdRolesConstants.ROLE_SESMT;
import static br.com.mili.milibackend.shared.roles.TradeRolesConstants.ROLE_ODOMETRO;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ColaboradorController.ENDPOINT)
@PreAuthorize("hasAuthority('" + ROLE_ODOMETRO + "')")
@Tag(name = "Trade - Colaborador", description = "Operações relacionadas a colaboradores")
public class ColaboradorController {

    protected static final String ENDPOINT = "/mili-backend/v1/trade/colaborador";

    private final GetAllColaboradorUseCase getAllColaboradorUseCase;

    @GetMapping
    @Operation(summary = "Lista colaboradores com filtros")
    public ResponseEntity<MyPage<TradeColaboradorGetAllOutputDto>> getAll(
            @ParameterObject @ModelAttribute @Valid TradeColaboradorGetAllInputDto inputDto) {

        log.info("GET {}/ - Filtros: {}", ENDPOINT, inputDto);

        MyPage<TradeColaboradorGetAllOutputDto> page = getAllColaboradorUseCase.execute(
                inputDto
        );

        return ResponseEntity.ok(page);
    }
}
