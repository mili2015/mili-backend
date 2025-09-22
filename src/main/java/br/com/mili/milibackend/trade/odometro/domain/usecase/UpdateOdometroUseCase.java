package br.com.mili.milibackend.trade.odometro.domain.usecase;

import br.com.mili.milibackend.trade.odometro.application.dto.TradeOdometroUpdateInputDto;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeOdometroUpdateOutputDto;

public interface UpdateOdometroUseCase {
    TradeOdometroUpdateOutputDto execute(TradeOdometroUpdateInputDto inputDto);
}
