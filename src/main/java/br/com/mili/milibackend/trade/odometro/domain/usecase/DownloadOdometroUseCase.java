package br.com.mili.milibackend.trade.odometro.domain.usecase;

import br.com.mili.milibackend.trade.odometro.application.dto.TradeOdometroDownloadOutputDto;

public interface DownloadOdometroUseCase {
   TradeOdometroDownloadOutputDto execute(String path);
}
