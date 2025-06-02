package br.com.mili.milibackend.shared.enums;

import lombok.Getter;

@Getter
public enum MimeTypeEnum {
        JPEG("image/jpeg"),
        PNG("image/png"),
        PDF("application/pdf"),
        MP4("video/mp4");

        private final String contentType;

        MimeTypeEnum(String contentType) {
            this.contentType = contentType;
        }
}
