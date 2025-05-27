package br.com.mili.milibackend.shared.page.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    private List<T> items;
    private int getPage;
    private int getSize;
    private int getTotalPages;
    private long getTotalElements;
    private boolean isFirst;
    private boolean isLast;
}
