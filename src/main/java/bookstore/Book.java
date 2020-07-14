package bookstore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @AllArgsConstructor @ToString
public class Book implements Comparable<Book> {
    private final String title;
    private final String author;
    private final LocalDate publishedOn;

    @Override
    public int compareTo(Book that){
        return this.title.compareTo(that.title);
    }
}
