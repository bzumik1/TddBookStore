package bookstore;




import lombok.Getter;

import java.time.Year;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class BookShelf {
    private final List<Book> books = new ArrayList<>();



    public void add(Book... books){
        this.books.addAll(Arrays.asList(books));
    }

    public List<Book> getBooks(){
        return Collections.unmodifiableList(books);
    }

    public List<Book> arrange(){
        return arrange(Comparator.comparing(Book::getTitle));
    }

    public List<Book> arrange(Comparator<Book> comparator){
        return Collections.unmodifiableList(books.stream().sorted(comparator).collect(Collectors.toList()));
    }

    public Map<Year, List<Book>> groupByPublicationYear(){
        return books.stream().collect(Collectors.groupingBy(book -> Year.of(book.getPublishedOn().getYear())));
    }

    public <K> Map<K, List<Book>> groupBy(Function<Book, K> fx){
        return books.stream().collect(Collectors.groupingBy(fx));
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(books.size()).append(" books found: ");
        books.forEach(book -> stringBuilder.append(book).append(", "));
        return stringBuilder.toString();
    }
}
