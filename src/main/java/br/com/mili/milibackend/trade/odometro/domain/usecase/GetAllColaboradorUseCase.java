package br.com.mili.milibackend.trade.odometro.domain.usecase;

import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeColaboradorGetAllInputDto;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeColaboradorGetAllOutputDto;

public interface GetAllColaboradorUseCase {
    MyPage<TradeColaboradorGetAllOutputDto> execute(TradeColaboradorGetAllInputDto inputDto);
}
