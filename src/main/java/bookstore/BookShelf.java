package bookstore;




import lombok.Getter;

import java.time.Year;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
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

    public Progress progress(){
        int readBooks = Long.valueOf(books.stream().filter(Book::isRead).count()).intValue();
        int booksInProgress = Long.valueOf(books.stream().filter(Book::isInProgress).count()).intValue();
        if(books.isEmpty()){
            return new Progress(0,0,0);
        }
        int booksToRead = books.size()-readBooks;

        int completed = (readBooks*100/books.size());
        int toRead = (booksToRead*100/books.size());
        int inProgress = (booksInProgress*100)/books.size();


        return new Progress(completed,toRead,inProgress);
    }

    public List<Book> findBooksByTitle(String searchTerm){
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> findBooksByTitle(String searchTerm, BookFilter filter){
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(searchTerm.toLowerCase()))
                .filter(filter::apply)
                .collect(Collectors.toList());
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(books.size()).append(" books found: ");
        books.forEach(book -> stringBuilder.append(book).append(", "));
        return stringBuilder.toString();
    }
}
