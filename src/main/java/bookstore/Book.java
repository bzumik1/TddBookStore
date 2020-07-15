package bookstore;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @RequiredArgsConstructor @ToString
public class Book implements Comparable<Book> {
    private final String title;
    private final String author;
    private final LocalDate publishedOn;
    private LocalDate startedReadingOn;
    private LocalDate finishedReadingOn;



    public boolean isRead(){
        return startedReadingOn != null && finishedReadingOn != null;
    }

    public boolean isInProgress(){
        return startedReadingOn != null && finishedReadingOn ==null;
    }

    @Override
    public int compareTo(Book that){
        return this.title.compareTo(that.title);
    }
}
