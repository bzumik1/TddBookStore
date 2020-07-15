package bookstore;

import java.time.LocalDate;
import java.time.Month;
import java.util.function.Predicate;

public class BookPublishedYearFilter implements BookFilter {
    private LocalDate startDate;
    private Predicate<Book> filterCondition;

    static BookPublishedYearFilter after(int year){
        BookPublishedYearFilter filter = new BookPublishedYearFilter();
        filter.startDate = LocalDate.of(year, Month.DECEMBER, 31);
        filter.filterCondition = book -> book.getPublishedOn().isAfter(filter.startDate);
        return filter;
    }

    static BookPublishedYearFilter before(int year){
        BookPublishedYearFilter filter = new BookPublishedYearFilter();
        filter.startDate = LocalDate.of(year, Month.JANUARY, 1);
        filter.filterCondition = book -> book.getPublishedOn().isBefore(filter.startDate);
        return filter;
    }




    @Override
    public boolean apply(Book book) {
        return filterCondition.test(book);
    }
}
