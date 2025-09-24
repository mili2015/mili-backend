package br.com.mili.milibackend.trade.odometro.application.usecase;

import br.com.mili.milibackend.shared.infra.aws.IS3Service;
import br.com.mili.milibackend.shared.infra.aws.StorageFolderEnum;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeOdometroDownloadOutputDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DownloadOdometroUseCaseImplTest {

    @InjectMocks
    private DownloadOdometroUseCaseImpl useCase;

    @Mock
    private IS3Service s3Service;

    @Test
    void deve_retornar_url_assinada_do_s3() {
        String path = "folder/file.png";
        String expectedUrl = "https://signed.example.com";
        when(s3Service.getPresignedUrl(eq(StorageFolderEnum.ODOMETRO), eq(path))).thenReturn(expectedUrl);

        TradeOdometroDownloadOutputDto out = useCase.execute(path);

        assertNotNull(out);
        assertEquals(expectedUrl, out.getLink());
    }
}
