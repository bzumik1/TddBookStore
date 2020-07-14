package bookstore;



import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("<= BookShelf Specification =>")
public class BookShelfSpec {
    private BookShelf bookShelf;
    private Book effectiveJava;
    private Book cleanCode;
    private Book codeComplete;
    private Book mythicalManMonth;

    private BookShelfSpec(TestInfo testInfo){
        //System.out.println("Working on test "+ testInfo.getDisplayName());
    }

    @BeforeEach
    void initializeBookShelf(){
        bookShelf = new BookShelf();
        effectiveJava = new Book("Effective Java", "Joshua Bloch", LocalDate.of(2008, Month.MAY, 8));
        cleanCode = new Book("Clean Code", "Robert Cecil Martin", LocalDate.of(2008, Month.AUGUST, 1));
        codeComplete = new Book("Code Complete","Steve McConnel", LocalDate.of(2004, Month.JUNE, 9));
        mythicalManMonth = new Book("The Mythical Man-Month", "Frederick Phillips Brooks", LocalDate.of(1975, Month.JANUARY, 1));
    }

    @Test @DisplayName("new BookShelf is empty")
    void shelfEmptyWhenNoBookAdded(){
        assertTrue(bookShelf.getBooks().isEmpty(), "BookShelf should be empty.");
    }

    @Test @DisplayName("when two books are added, then the book shelf includes two books")
    void shelfIncludesTwoBooksWhenTwoWereAdded(){
        bookShelf.add(effectiveJava,codeComplete);
        assertEquals(2,bookShelf.getBooks().size(),() -> "BookShelf should have two books.");
    }

    @Test @DisplayName("no book are added when nothing is provided")
    void noBooksAreAddedWhenAddMethodIsEmpty(){
        bookShelf.add();
        assertTrue(bookShelf.getBooks().isEmpty(),() -> "No book should be added.");
    }

    @Test @DisplayName("books returned from BookShelf are immutable")
    void booksReturnedFromBookShelfAreImmutable(){
        bookShelf.add(effectiveJava, codeComplete);
        List<Book> books = bookShelf.getBooks();
        try{
            books.add(mythicalManMonth);
            fail(() -> "Should not be able to add book to books");
        } catch (Exception e){
            assertTrue(e instanceof UnsupportedOperationException, () -> "Should throw Unsupported OperationException");
        }
    }

    @Test @DisplayName("arrange method returns all books arranged by name")
    void bookShelfArrangedByBookTitle(){
        bookShelf.add(effectiveJava, codeComplete, mythicalManMonth);
        List<Book> books = bookShelf.arrange();
        assertThat(books).isSortedAccordingTo(Comparator.<Book>naturalOrder());
    }

    @Test @DisplayName("books in the BookShelf are in same order after calling arrange method")
    void booksInBookShelfAreInAfterCallingArrangeMethod(){
        bookShelf.add(effectiveJava, codeComplete, mythicalManMonth);
        bookShelf.arrange();
        assertEquals(bookShelf.getBooks(),Arrays.asList(effectiveJava, codeComplete, mythicalManMonth),() -> "Order of books should not be changed.");
    }

    @Test @DisplayName("toString method should return number of books and it's names")
    void shelfToStringShouldPrintBookCountAndTitles(){
        List<Book> books = bookShelf.getBooks();
        bookShelf.add(effectiveJava);
        bookShelf.add(codeComplete);
        String shelfString = bookShelf.toString();
        assertAll(() -> assertTrue(shelfString.contains(effectiveJava.getTitle()), "1st book title missing."),
                  () -> assertTrue(shelfString.contains(codeComplete.getTitle()),"2nd book title missing."),
                  () -> assertTrue(shelfString.contains("2 books found"), "Book count missing."));

    }

    @Test @DisplayName("arrange method returns all books arranged by user provided criteria")
    void bookShelfArrangedByUserProvidedCriteria(){
        bookShelf.add(effectiveJava,codeComplete,mythicalManMonth);
        Comparator<Book> reversed = Comparator.<Book>naturalOrder().reversed();
        List<Book> books = bookShelf.arrange(reversed);
        assertThat(books).isSortedAccordingTo(reversed);
    }

    @Test @DisplayName("disabled test") @Disabled("Mock test is disabled.")
    void disabledTest(){
        System.out.println("Test is running");
    }

    @Test @DisplayName("books inside BookShelf are grouped by publication year")
    void groupBooksInsideBookShelfByPublicationYear(){
        bookShelf.add(effectiveJava,codeComplete,mythicalManMonth,cleanCode);
        Map<Year, List<Book>> booksByPublicationYear = bookShelf.groupByPublicationYear();

        assertThat(booksByPublicationYear)
                .containsKey(Year.of(2008))
                .containsValues(Arrays.asList(effectiveJava, cleanCode));

        assertThat(booksByPublicationYear)
                .containsKey(Year.of(2004))
                .containsValues(Collections.singletonList(codeComplete));

        assertThat(booksByPublicationYear)
                .containsKey(Year.of(1975))
                .containsValues(Collections.singletonList(mythicalManMonth));
    }

    @Test @DisplayName("books inside BookShelf are grouped by user provided criteria")
    void groupBooksInsideBookShelfByUserProvidedCriteria(){
        bookShelf.add(effectiveJava,codeComplete,mythicalManMonth,cleanCode);
        Map<String, List<Book>> booksByPublicationYear = bookShelf.groupBy(Book::getAuthor);

        assertThat(booksByPublicationYear)
                .containsKey("Joshua Bloch")
                .containsValues(Collections.singletonList(effectiveJava));

        assertThat(booksByPublicationYear)
                .containsKey("Steve McConnel")
                .containsValues(Collections.singletonList(codeComplete));

        assertThat(booksByPublicationYear)
                .containsKey("Frederick Phillips Brooks")
                .containsValues(Collections.singletonList(mythicalManMonth));

        assertThat(booksByPublicationYear)
                .containsKey("Robert Cecil Martin")
                .containsValues(Collections.singletonList(cleanCode));
    }
}