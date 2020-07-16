package bookstore;

public class BookShelfCapacityReached extends RuntimeException{
    public BookShelfCapacityReached(){
        super("BookShelf max capacity reached.");
    }

}
