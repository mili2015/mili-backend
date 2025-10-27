package br.com.mili.milibackend.shared.page;

import br.com.mili.milibackend.shared.page.pagination.MyPageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Sort;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Filtro {
    private String descricao;
    private String decricao;
    private Boolean allRows = false;
    private MyPageable pageable = new MyPageable();

    @Schema(description = "Campo de ordenação example = \"nome,asc\"")
    private String sort;

    public Sort toSort() {
        if (sort == null || sort.isBlank()) {
            return null;
        }

        String[] parts = sort.split(",");
        String campo = parts[0].trim();

        Sort.Direction direction = (parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim()))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return Sort.by(direction, campo);
    }
}
