package kr.hhplus.be.domain.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageResultTest {

    @DisplayName("PageResult를 생성할 때 totalCount와 pageSize에 의해 totalPages가 계산된다.")
    @Test
    void calculateTotalPages() {
        // given
        List<String> content = List.of("hi", "hello", "bye");
        int pageNo = 1;
        int pageSize = 10;
        long totalCount = 14;

        // when
        PageResult<String> result =
                PageResult.create(content, pageNo, pageSize, totalCount);

        // then
        assertThat(result.totalPages()).isEqualTo(2);

    }

}