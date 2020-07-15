package bookstore;



import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(BooksParameterResolver.class)
@DisplayName("<= BookShelf Specification =>")
class BookShelfSpec {
    private BookShelf bookShelf;
    private Book effectiveJava;
    private Book cleanCode;
    private Book codeComplete;
    private Book mythicalManMonth;

    private BookShelfSpec(TestInfo testInfo){
        //System.out.println("Working on test "+ testInfo.getDisplayName());
    }

    @BeforeEach
    void initializeBookShelf(Map<String, Book> books){
        bookShelf = new BookShelf();
        effectiveJava = books.get("Effective Java");
        cleanCode = books.get("Clean Code");
        codeComplete = books.get("Code Complete");
        mythicalManMonth = books.get("The Mythical Man-Month");
    }

    @Nested @DisplayName("is empty")
    class IsEmpty{
        @Test @DisplayName("new BookShelf is empty")
        void shelfEmptyWhenNoBookAdded(){
            assertThat(bookShelf.getBooks())
                    .as("BookShelf should not be null")
                    .isNotNull()
                    .as("BookShelf should be empty")
                    .isEmpty();
        }

        @Test @DisplayName("no book are added when nothing is provided")
        void noBooksAreAddedWhenAddMethodIsEmpty(){
            bookShelf.add();
            assertThat(bookShelf.getBooks())
                    .as("No book should be added.")
                    .isEmpty();
        }
    }

    @Nested @DisplayName("after adding books")
    class BooksAreAdded{
        @Test @DisplayName("when two books are added, then the book shelf includes two books")
        void shelfIncludesTwoBooksWhenTwoWereAdded(){
            bookShelf.add(effectiveJava,codeComplete);
            assertThat(bookShelf.getBooks().size())
                    .as("BookShelf should have two books.")
                    .isEqualTo(2);
        }

        @Test @DisplayName("books returned from BookShelf are immutable")
        void booksReturnedFromBookShelfAreImmutable(){
            bookShelf.add(effectiveJava, codeComplete);
            List<Book> books = bookShelf.getBooks();
            try{
                books.add(mythicalManMonth);
                fail("Should not be able to add book to books");
            } catch (Exception e){
                assertTrue(e instanceof UnsupportedOperationException, "Should throw Unsupported OperationException");
            }
        }
    }

    @Nested @DisplayName("arranging books in BookShelf")
    class ArrangementOfBooks{
        @Test @DisplayName("arrange method returns all books arranged by name")
        void bookShelfArrangedByBookTitle(){
            bookShelf.add(effectiveJava, codeComplete, mythicalManMonth);
            List<Book> books = bookShelf.arrange();
            assertThat(books).isSortedAccordingTo(Comparator.naturalOrder());
        }

        @Test @DisplayName("books in the BookShelf are in same order after calling arrange method")
        void booksInBookShelfAreInAfterCallingArrangeMethod(){
            bookShelf.add(effectiveJava, codeComplete, mythicalManMonth);
            bookShelf.arrange();
            assertThat(bookShelf.getBooks())
                    .as("Order of books should not be changed.")
                    .containsSequence(effectiveJava, codeComplete, mythicalManMonth);
        }

        @Test @DisplayName("arrange method returns all books arranged by user provided criteria")
        void bookShelfArrangedByUserProvidedCriteria(){
            bookShelf.add(effectiveJava,codeComplete,mythicalManMonth);
            Comparator<Book> reversed = Comparator.<Book>naturalOrder().reversed();
            List<Book> books = bookShelf.arrange(reversed);
            assertThat(books).isSortedAccordingTo(reversed);
        }
    }

    @Nested @DisplayName("grouping by")
    class GroupingBy{
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

    @Test @DisplayName("toString method should return number of books and it's names")
    void shelfToStringShouldPrintBookCountAndTitles(){
        bookShelf.add(effectiveJava);
        bookShelf.add(codeComplete);
        String shelfString = bookShelf.toString();
        assertAll(() -> assertTrue(shelfString.contains(effectiveJava.getTitle()), "1st book title missing."),
                  () -> assertTrue(shelfString.contains(codeComplete.getTitle()),"2nd book title missing."),
                  () -> assertTrue(shelfString.contains("2 books found"), "Book count missing."));

    }

    @Test @DisplayName("disabled test") @Disabled("Mock test is disabled.")
    void disabledTest(){
        System.out.println("Test is running");
    }

    @Nested @DisplayName("search")
    class BookShelfSearchSpec{
        @BeforeEach
        void searchInicialization(){
            bookShelf.add(codeComplete,effectiveJava, mythicalManMonth,cleanCode);
        }

        @Test @DisplayName("finds books with title containing search-term")
        void searchForBooksByGivenText(){
            List<Book> foundBooks = bookShelf.findBooksByTitle("code");
            assertThat(foundBooks.size())
                    .as("Should find two books.")
                    .isEqualTo(2);
        }

        @Test @DisplayName("finds books with title containing search-term and published after date")
        void searchForBooksByTitleAndDate(){
            List<Book> books = bookShelf.findBooksByTitle("code",
                    book -> book.getPublishedOn().isBefore(LocalDate.of(2014,Month.DECEMBER,31)));
            assertThat(books.size())
                    .as("Should find two books")
                    .isEqualTo(2);
        }

        @Test @DisplayName("finds books after aplying multiple filters for search")
        void compositeFilters(){
            BookFilter filterByName;
            BookFilter filterByYear = BookPublishedYearFilter.before(2016);
        }
    }




}
