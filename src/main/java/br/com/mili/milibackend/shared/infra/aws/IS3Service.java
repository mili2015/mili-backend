package br.com.mili.milibackend.shared.infra.aws;

public interface IS3Service {
    void upload(StorageFolderEnum folder, String json) ;
    String getPresignedUrl(StorageFolderEnum folder, String filename);
}
