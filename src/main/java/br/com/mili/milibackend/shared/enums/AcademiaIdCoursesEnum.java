package br.com.mili.milibackend.shared.enums;

import lombok.Getter;

@Getter
public enum AcademiaIdCoursesEnum {
    TRES_BARRAS(2519, 2);

    private int idCourse;
    private int ctempCodigo;

    AcademiaIdCoursesEnum(int idCouse, int ctempCodigo) {
        this.idCourse = idCouse;
        this.ctempCodigo = ctempCodigo;
    }

    public static AcademiaIdCoursesEnum fromCtempCodigo(int ctempCodigo) {
        for (AcademiaIdCoursesEnum e : values()) {
            if (e.getCtempCodigo() == ctempCodigo) {
                return e;
            }
        }
        return null;
    }
}
