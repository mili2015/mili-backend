package br.com.mili.milibackend.shared.page.pagination;

import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@ToString
public class PageBaseImpl<T> implements MyPage<T> {

    private Page pageSpring;

    public PageBaseImpl(List<T> content, int currentPage, int pageSize, long total) {

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        this.pageSpring = new PageImpl(content, pageable, total);
    }

    @Override
    public List<T> getContent() {
        return pageSpring.getContent();
    }

    @Override
    public int getPage() {
        return pageSpring.getNumber() + 1;
    }

    @Override
    public int getSize() {
        return pageSpring.getSize();
    }

    @Override
    public int getTotalPages() {
        return pageSpring.getTotalPages();
    }

    @Override
    public long getTotalElements() {
        return pageSpring.getTotalElements();
    }

    @Override
    public boolean isFirst() {
        return pageSpring.isFirst();
    }

    @Override
    public boolean isLast() {
        return pageSpring.isLast();
    }
}
