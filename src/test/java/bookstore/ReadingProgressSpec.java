package bookstore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(BooksParameterResolver.class)
@DisplayName("<= Reading Progress Specification =>")
class ReadingProgressSpec {
    private BookShelf bookShelf;
    private Book effectiveJava;
    private Book cleanCode;
    private Book codeComplete;
    private Book mythicalManMonth;
    private Book javaUnitTesting;

    @BeforeEach
    void initializeBookShelf(Map<String, Book> books){
        bookShelf = new BookShelf();
        effectiveJava = books.get("Effective Java");
        cleanCode = books.get("Clean Code");
        codeComplete = books.get("Code Complete");
        mythicalManMonth = books.get("The Mythical Man-Month");
        javaUnitTesting = books.get("Java Unit Testing");

        bookShelf.add(effectiveJava,cleanCode,codeComplete,mythicalManMonth,javaUnitTesting);
    }

    @Test @DisplayName("is 0% completed and 100% to-read when no book is read yet")
    void progress100PercentUnread(){
        Progress progress = bookShelf.progress();
        assertThat(progress.getCompleted()).isEqualTo(0);
        assertThat(progress.getToRead()).isEqualTo(100);
    }

    @Test @DisplayName("is 40% completed and 60% to-read when 2 books are finished and 3 not read yet.")
    void progressWithCompletedAndToReadPercentages(){
        effectiveJava.setStartedReadingOn(LocalDate.of(2016, Month.JUNE,1));
        effectiveJava.setFinishedReadingOn(LocalDate.of(2016, Month.JULY,31));
        cleanCode.setStartedReadingOn(LocalDate.of(2016, Month.AUGUST,1));
        cleanCode.setFinishedReadingOn(LocalDate.of(2016, Month.AUGUST,31));

        Progress progress = bookShelf.progress();

        assertThat(progress.getCompleted())
                .as("Completed should be 40.")
                .isEqualTo(40);
        assertThat(progress.getToRead())
                .as("ToRead should be 60")
                .isEqualTo(60);
    }

    @Test @DisplayName("is 20% in-progress when 1 book is in progress and 4 not read yet")
    void inProgress(){
        effectiveJava.setStartedReadingOn(LocalDate.of(2016, Month.JUNE,1));
        Progress progress = bookShelf.progress();
        assertThat(progress.getInProgress())
                .as("InProgress should be 20.")
                .isEqualTo(20);
    }

    @Test @DisplayName("is 100% completed, 0% in-progress and 0% to-read when all books are read")
    void allBooksAreRead(){
        effectiveJava.setStartedReadingOn(LocalDate.of(2016, Month.JUNE,1));
        effectiveJava.setFinishedReadingOn(LocalDate.of(2016, Month.JULY,31));

        cleanCode.setStartedReadingOn(LocalDate.of(2016, Month.AUGUST,1));
        cleanCode.setFinishedReadingOn(LocalDate.of(2016, Month.AUGUST,31));

        codeComplete.setStartedReadingOn(LocalDate.of(2016, Month.MAY,1));
        codeComplete.setFinishedReadingOn(LocalDate.of(2016, Month.MAY,31));

        mythicalManMonth.setStartedReadingOn(LocalDate.of(2016, Month.DECEMBER,1));
        mythicalManMonth.setFinishedReadingOn(LocalDate.of(2016, Month.DECEMBER,31));

        javaUnitTesting.setStartedReadingOn(LocalDate.of(2016, Month.SEPTEMBER,1));
        javaUnitTesting.setFinishedReadingOn(LocalDate.of(2016, Month.OCTOBER,31));

        Progress progress = bookShelf.progress();
        assertThat(progress)
                .as("Should be 100% completed, 0% in-progress and 0% to-read.")
                .isEqualToComparingFieldByField(new Progress(100,0,0));
    }

}
