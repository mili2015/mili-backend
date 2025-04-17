package br.com.mili.milibackend.shared.enums;

//@Getter
public enum SystemEnum {
    SISAV(38);

    private int Id;
    SystemEnum(int id) {
        Id = id;
    }

    public int getId() {
        return Id;
    }
}
