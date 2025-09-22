package br.com.mili.milibackend.trade.odometro.application.usecase;

import br.com.mili.milibackend.shared.infra.aws.IS3Service;
import br.com.mili.milibackend.shared.infra.aws.StorageFolderEnum;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeOdometroDownloadOutputDto;
import br.com.mili.milibackend.trade.odometro.domain.usecase.DownloadOdometroUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DownloadOdometroUseCaseImpl implements DownloadOdometroUseCase {
    private final IS3Service s3Service;

    @Override
    public TradeOdometroDownloadOutputDto execute(String path) {

        // recupera no s3
        var url = s3Service.getPresignedUrl(StorageFolderEnum.ODOMETRO, path);

        return new TradeOdometroDownloadOutputDto(url);
    }
}
