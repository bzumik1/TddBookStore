package bookstore;

public class BookTitleFilter implements BookFilter {
    private final String searchTerm;

    private BookTitleFilter(String searchTerm){
        this.searchTerm = searchTerm;
    }

    static BookTitleFilter searchTerm(String searchTerm){
        return new BookTitleFilter(searchTerm);
    }

    @Override
    public boolean apply(Book book) {
        if(book == null){
            return false;
        }
        return book.getTitle().toLowerCase().contains(searchTerm.toLowerCase());
    }
}
