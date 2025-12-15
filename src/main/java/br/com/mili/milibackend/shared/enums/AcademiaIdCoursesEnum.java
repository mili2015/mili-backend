package br.com.mili.milibackend.shared.enums;

import lombok.Getter;

import java.util.Optional;

@Getter
public enum AcademiaIdCoursesEnum {
    TRES_BARRAS(2519, 2),
    CURITIBA_FAB(2614, 1);

    private int idCourse;
    private int ctempCodigo;

    AcademiaIdCoursesEnum(int idCouse, int ctempCodigo) {
        this.idCourse = idCouse;
        this.ctempCodigo = ctempCodigo;
    }

    public static Optional<AcademiaIdCoursesEnum> fromCtempCodigo(int ctempCodigo) {
        for (AcademiaIdCoursesEnum e : values()) {
            if (e.getCtempCodigo() == ctempCodigo) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }
}
