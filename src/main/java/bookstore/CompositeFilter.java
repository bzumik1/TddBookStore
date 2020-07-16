package bookstore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompositeFilter implements BookFilter {
    private final List<BookFilter> bookFilters = new ArrayList<>();

    public CompositeFilter add(BookFilter bookFilter){
        bookFilters.add(bookFilter);
        return this;
    }


    @Override
    public boolean apply(Book book) {
        if(book ==null){
            return false;
        }
        return bookFilters.stream().allMatch(bookFilter -> bookFilter.apply(book));
    }
}
