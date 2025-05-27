package br.com.mili.milibackend.shared.page.pagination;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyPageable {
    private int page;
    private int size;

    public int getOffset() {
        if (page <= 0)
            return 0;
        return (page - 1)  * size;
    }
}
