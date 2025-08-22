package br.com.mili.milibackend.gfd.infra.fileprocess;

import br.com.mili.milibackend.gfd.application.dto.fileprocess.DocumentoFileData;
import br.com.mili.milibackend.gfd.domain.interfaces.FileProcessingService;
import br.com.mili.milibackend.shared.enums.MimeTypeEnum;
import br.com.mili.milibackend.shared.exception.types.BadRequestException;
import org.apache.commons.codec.binary.Base64;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import java.text.Normalizer;
import java.util.UUID;

import static br.com.mili.milibackend.gfd.infra.fileprocess.exception.FileProcessingException.FILE_PROCESSING_TYPE_NOT_PERMITED;

@Service
public class FileProcessingServiceImpl implements FileProcessingService {
    private final Tika tika;

    public FileProcessingServiceImpl(Tika tika) {
        this.tika = tika;
    }

    public DocumentoFileData processFile(String base64File, String originalFileName, MimeTypeEnum allowedMimeType) {
        byte[] fileData = Base64.decodeBase64(base64File);

        String mimeTypeStr = tika.detect(fileData);
        MimeType mimeType = MimeType.valueOf(mimeTypeStr);

        if (!isValidMimeType(allowedMimeType, mimeType.toString())) {
            throw new BadRequestException(FILE_PROCESSING_TYPE_NOT_PERMITED.getMensagem() + allowedMimeType, FILE_PROCESSING_TYPE_NOT_PERMITED.getCode());
        }

        String nomeArquivo = UUID.randomUUID() + "-" + normalizeFileName(originalFileName);
        return new DocumentoFileData(fileData, mimeType.toString(), nomeArquivo, fileData.length);
    }

    private String normalizeFileName(String originalFileName) {
        if (originalFileName == null) {
            return null;
        }

        if (originalFileName.isEmpty()) {
            return "";
        }

        var normalized = Normalizer.normalize(originalFileName, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");

        normalized = normalized.replaceAll("[^a-zA-Z0-9\\.\\-_]", "");

        return normalized;
    }

    private boolean isValidMimeType(MimeTypeEnum allowedMimeType, String mimeType) {
        if (allowedMimeType == null) return true;

        if (allowedMimeType == MimeTypeEnum.ALL) return true;

        String allowedValue = allowedMimeType.getContentType().toLowerCase();
        String detected = mimeType.toLowerCase();

        // apenas uma extensão
        if (allowedValue.equals(detected)) {
            return true;
        }

        // wildcards image/*
        if (allowedValue.endsWith("/*")) {
            var prefix = allowedValue.substring(0, allowedValue.indexOf("/*"));
            return detected.startsWith(prefix + "/");
        }

        return false;
    }
}