package br.com.mili.milibackend.shared.page;

import br.com.mili.milibackend.shared.page.pagination.MyPageable;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Filtro {
    private String descricao;
    private MyPageable pageable = new MyPageable();
}
