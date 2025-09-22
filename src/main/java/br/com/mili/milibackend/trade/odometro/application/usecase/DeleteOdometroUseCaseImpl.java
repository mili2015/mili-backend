package br.com.mili.milibackend.trade.odometro.application.usecase;

import br.com.mili.milibackend.shared.exception.types.BadRequestException;
import br.com.mili.milibackend.trade.odometro.domain.usecase.DeleteOdometroUseCase;
import br.com.mili.milibackend.trade.odometro.infra.repository.OdometroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.com.mili.milibackend.trade.odometro.adapter.exception.OdometroCodeException.TRADE_MCD_ODOMETRO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class DeleteOdometroUseCaseImpl implements DeleteOdometroUseCase {

    private final OdometroRepository odometroRepository;

    @Override
    public void execute(Integer id) {
        if (!odometroRepository.existsById(id)) {
            throw new BadRequestException(TRADE_MCD_ODOMETRO_NAO_ENCONTRADO.getMensagem(), TRADE_MCD_ODOMETRO_NAO_ENCONTRADO.getCode());
        }

        odometroRepository.deleteById(id);
    }
}
