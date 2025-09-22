package br.com.mili.milibackend.trade.odometro.application.usecase;

import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeOdometroUpdateInputDto;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeOdometroUpdateOutputDto;
import br.com.mili.milibackend.trade.odometro.domain.entity.Odometro;
import br.com.mili.milibackend.trade.odometro.domain.usecase.UpdateOdometroUseCase;
import br.com.mili.milibackend.trade.odometro.infra.repository.OdometroRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static br.com.mili.milibackend.trade.odometro.adapter.exception.OdometroCodeException.TRADE_MCD_ODOMETRO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class UpdateOdometroUseCaseImpl implements UpdateOdometroUseCase {
    private final OdometroRepository odometerRepository;
    private final ModelMapper modelMapper;

    @Override
    public TradeOdometroUpdateOutputDto execute(TradeOdometroUpdateInputDto inputDto) {
        Odometro odometer = odometerRepository.findById(inputDto.getId())
                .orElseThrow(() -> new NotFoundException(TRADE_MCD_ODOMETRO_NAO_ENCONTRADO.getMensagem(), TRADE_MCD_ODOMETRO_NAO_ENCONTRADO.getCode()));

        odometer.setKmFim(inputDto.getKmFim());
        odometer.setDataFim(inputDto.getDataFim());

        Odometro updatedOdometer = odometerRepository.save(odometer);

        return modelMapper.map(updatedOdometer, TradeOdometroUpdateOutputDto.class);
    }
}
