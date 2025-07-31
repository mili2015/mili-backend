package br.com.mili.milibackend.gfd.application.dto.fileprocess;

public class DocumentoFileData {
    private final byte[] fileBytes;
    private final String mimeType;
    private final String nomeArquivo;
    private final Integer tamanho;

    public DocumentoFileData(byte[] fileBytes, String mimeType, String nomeArquivo, Integer tamanho) {
        this.fileBytes = fileBytes;
        this.mimeType = mimeType;
        this.nomeArquivo = nomeArquivo;
        this.tamanho = tamanho;
    }

    public byte[] getFileBytes() { return fileBytes; }
    public String getMimeType() { return mimeType; }
    public String getNomeArquivo() { return nomeArquivo; }
    public Integer getTamanho() { return tamanho; }
}