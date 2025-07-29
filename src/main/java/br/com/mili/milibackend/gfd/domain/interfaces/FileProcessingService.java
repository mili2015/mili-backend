package br.com.mili.milibackend.gfd.domain.interfaces;

import br.com.mili.milibackend.gfd.application.dto.fileprocess.DocumentoFileData;

public interface FileProcessingService {
    DocumentoFileData processFile(String base64File, String originalFileName);
}
