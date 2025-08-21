package br.com.mili.milibackend.gfd.application.dto.gfdDocumentoPeriodo;

import br.com.mili.milibackend.gfd.domain.entity.GfdDocumento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GfdDocumentoPeriodoCreateOutputDto {
    private Integer id;

    private GfdDocumento gfdDocumento;

    private LocalDate periodoInicial;

    private LocalDate periodoFinal;
}
