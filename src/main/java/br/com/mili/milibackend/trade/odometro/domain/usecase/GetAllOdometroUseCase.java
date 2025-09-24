package br.com.mili.milibackend.trade.odometro.domain.usecase;

import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeOdometroGetAllInputDto;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeOdometroGetAllOutputDto;

public interface GetAllOdometroUseCase {
    MyPage<TradeOdometroGetAllOutputDto> execute(TradeOdometroGetAllInputDto inputDto);
}
