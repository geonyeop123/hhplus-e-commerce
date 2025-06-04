package kr.hhplus.be.domain.common;

import java.util.List;

public record PageResult<T>(
        List<T> content,
        int page,
        int size,
        long totalCount,
        int totalPages
) {

    public static <T> PageResult<T> create(List<T> content, int page, int size, long totalCount){
        return new PageResult<>(content, page, size, totalCount, (int) Math.ceil((double) totalCount / size));
    }
}