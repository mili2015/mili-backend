package br.com.mili.milibackend.trade.odometro.adapter.web.controller;

import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.trade.odometro.application.dto.*;
import br.com.mili.milibackend.trade.odometro.domain.usecase.DeleteOdometroUseCase;
import br.com.mili.milibackend.trade.odometro.domain.usecase.DownloadOdometroUseCase;
import br.com.mili.milibackend.trade.odometro.domain.usecase.GetAllOdometroUseCase;
import br.com.mili.milibackend.trade.odometro.domain.usecase.UpdateOdometroUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static br.com.mili.milibackend.shared.roles.TradeRolesConstants.ROLE_ODOMETRO;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(OdometroController.ENDPOINT)
@PreAuthorize("hasAuthority('" + ROLE_ODOMETRO + "')")
@Tag(name = "Trade - Odômetro", description = "Operações relacionadas ao controle de odômetro")
public class OdometroController {

    protected static final String ENDPOINT = "/mili-backend/v1/trade/odometro";

    private final GetAllOdometroUseCase getAllOdometroUseCase;
    private final UpdateOdometroUseCase updateOdometroUseCase;
    private final DeleteOdometroUseCase deleteOdometerReadingUseCase;
    private final DownloadOdometroUseCase downloadOdometroUseCase;

    @GetMapping
    @Operation(summary = "Lista todas as leituras de odômetro com filtros")
    public ResponseEntity<MyPage<TradeOdometroGetAllOutputDto>> getAll(
            @ParameterObject @ModelAttribute @Valid TradeOdometroGetAllInputDto inputDto) {

        log.info("GET {}/ - Filtros: {}", ENDPOINT, inputDto);

        MyPage<TradeOdometroGetAllOutputDto> page = getAllOdometroUseCase.execute(
                inputDto
        );

        return ResponseEntity.ok(page);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza a quilometragem final de uma leitura de odômetro")
    public ResponseEntity<TradeOdometroUpdateOutputDto> update(
            @PathVariable Integer id,
            @Valid @RequestBody TradeOdometroUpdateInputDto inputDto) {

        log.info("PATCH {}/{} - Dados: {}", ENDPOINT, id, inputDto);

        inputDto.setId(id);

        TradeOdometroUpdateOutputDto response = updateOdometroUseCase.execute(inputDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma leitura de odômetro")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("DELETE {}/{}", ENDPOINT, id);

        deleteOdometerReadingUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download")
    public ResponseEntity<TradeOdometroDownloadOutputDto> download(
            @RequestParam(required = true) String path
    ) {
        log.info("GET {}/{}", ENDPOINT, path);

        TradeOdometroDownloadOutputDto dto = downloadOdometroUseCase.execute(path);
        return ResponseEntity.ok(dto);
    }
}
