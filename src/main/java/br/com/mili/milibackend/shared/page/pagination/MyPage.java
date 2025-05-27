package br.com.mili.milibackend.shared.page.pagination;

import java.util.List;

public interface MyPage<T> {

    List<T> getContent();

    int getPage();
    int getSize();
    int getTotalPages();
    long getTotalElements();
    boolean isFirst();
    boolean isLast();

}
