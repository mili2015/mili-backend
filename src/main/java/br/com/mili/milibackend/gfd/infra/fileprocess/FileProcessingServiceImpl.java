package br.com.mili.milibackend.gfd.infra.fileprocess;

import br.com.mili.milibackend.gfd.application.dto.fileprocess.DocumentoFileData;
import br.com.mili.milibackend.gfd.domain.interfaces.FileProcessingService;
import org.apache.commons.codec.binary.Base64;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import java.text.Normalizer;
import java.util.UUID;

@Service
public class FileProcessingServiceImpl implements FileProcessingService {
    private final Tika tika;

    public FileProcessingServiceImpl(Tika tika) {
        this.tika = tika;
    }

    public DocumentoFileData processFile(String base64File, String originalFileName) {
        byte[] fileData = Base64.decodeBase64(base64File);
        String mimeTypeStr = tika.detect(fileData);
        MimeType mimeType = MimeType.valueOf(mimeTypeStr);
        String nomeArquivo = UUID.randomUUID() + "-" + normalizeFileName(originalFileName);
        return new DocumentoFileData(fileData, mimeType.toString(), nomeArquivo, fileData.length);
    }

    private String normalizeFileName(String originalFileName) {
        if(originalFileName == null) {
            return null;
        }

        if(originalFileName.isEmpty()) {
            return "";
        }

        var normalized = Normalizer.normalize(originalFileName, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");

        normalized = normalized.replaceAll("[^a-zA-Z0-9\\.\\-_]", "");

        return normalized;
    }
}