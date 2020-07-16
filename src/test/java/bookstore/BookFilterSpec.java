package bookstore;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(BooksParameterResolver.class)
@DisplayName("<= Book Filter Specification =>")
public class BookFilterSpec {
    private Book cleanCode;
    private Book codeComplete;

    @BeforeEach
    void initializeBookFilterSpec(Map<String,Book> books){
        cleanCode = books.get("Clean Code");
        codeComplete = books.get("Code Complete");
    }

    @Nested @DisplayName("book published date")
    class BookPublishedFilterSpec{
        @Test @DisplayName("is after specified year")
        void validateBookPublishedDatePostAskedYear(){
            BookPublishedYearFilter filter = BookPublishedYearFilter.after(2007);
            assertThat(filter.apply(cleanCode))
                    .as("Should be true.")
                    .isTrue();

        }

        @Test @DisplayName("is before specific year")
        void validateBookPublishedDateBeforeYear(){
            BookFilter filter = BookPublishedYearFilter.before(2007);
            assertThat(filter.apply(codeComplete))
                    .as("Should be true.")
                    .isTrue();
        }
    }

    @Nested @DisplayName("book title")
    class bookTitle implements FilterBoundaryTest{
        BookFilter filter;

        @BeforeEach
        void initializeBookFilterForPublishedDate(){
            filter = BookTitleFilter.searchTerm("code");
        }
        @Test @DisplayName("title contains search-term")
        void titleContainsSearchTerm(){
            BookFilter filter = BookTitleFilter.searchTerm("code");
            assertThat(filter.apply(codeComplete)).isTrue();
        }

        @Override
        public BookFilter get() {
            return filter;
        }
    }

    @Nested @DisplayName("composite filter")
    class compositeFilter implements FilterBoundaryTest{
        CompositeFilter compositeFilter;

        @BeforeEach
        void initializeCompositeFilter(){
            compositeFilter = new CompositeFilter();
        }

        @Test @DisplayName("title contains search-term and also published-on is before specific year")
        void titleContainsPublishedBefore(){
            compositeFilter
                    .add(BookPublishedYearFilter.before(2007))
                    .add(BookTitleFilter.searchTerm("code"));
            assertThat(compositeFilter.apply(codeComplete)).isTrue();
        }

        @Test @DisplayName("composite criteria does not invoke after first failure")
        void shouldNotInvokeAfterFirstFailure(){
            compositeFilter
                    .add(book -> false)
                    .add(book -> true);
            assertThat(compositeFilter.apply(codeComplete)).isFalse();
        }

        @Tag("slow")
        @Test @DisplayName("composite criteria invokes all filters")
        void compositeCriteriaAllFilters(){
            BookFilter firstInvokedMockedFilter = Mockito.mock(BookFilter.class);
            Mockito.when(firstInvokedMockedFilter.apply(cleanCode)).thenReturn(true);
            compositeFilter.add(firstInvokedMockedFilter);

            BookFilter secondInvokedMockedFilter = Mockito.mock(BookFilter.class);
            Mockito.when(secondInvokedMockedFilter.apply(cleanCode)).thenReturn(true);
            compositeFilter.add(secondInvokedMockedFilter);

            assertThat(compositeFilter.apply(cleanCode))
                    .isTrue();

            Mockito.verify(firstInvokedMockedFilter).apply(cleanCode);
            Mockito.verify(secondInvokedMockedFilter).apply(cleanCode);
        }

        @Override
        public BookFilter get() {
            return compositeFilter;
        }
    }

}
