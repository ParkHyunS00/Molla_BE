package com.example.molla.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private int totalPage;
    private long totalElement;
    private int currentPage;
    private boolean isLast;
}
