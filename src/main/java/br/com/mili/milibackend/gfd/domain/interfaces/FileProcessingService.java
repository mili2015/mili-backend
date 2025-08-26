package br.com.mili.milibackend.gfd.domain.interfaces;

import br.com.mili.milibackend.gfd.application.dto.fileprocess.DocumentoFileData;
import br.com.mili.milibackend.shared.enums.MimeTypeEnum;

public interface FileProcessingService {
    DocumentoFileData processFile(String base64File, String originalFileName, MimeTypeEnum allowedMimeTypes);
}
