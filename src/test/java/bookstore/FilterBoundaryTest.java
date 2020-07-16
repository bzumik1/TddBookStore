package bookstore;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public interface FilterBoundaryTest {
    BookFilter get();

    @Test @DisplayName("filter returns false when null is provided")
    default void nullTest(){
        assertThat(get().apply(null)).isFalse();
    }
}
