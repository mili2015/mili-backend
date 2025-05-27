package br.com.mili.milibackend.shared.infra.aws;

public enum StorageFolderEnum {
    ROTEIRO("roteiro"),
    GFD("gfd");


    private final String value;

    StorageFolderEnum(String value) {
        this.value = value;
    }

    public static boolean isValid(String value) {
        for (StorageFolderEnum param : values()) {
            if (param.value.equals(value)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String toString() {
        return value;
    }
}
