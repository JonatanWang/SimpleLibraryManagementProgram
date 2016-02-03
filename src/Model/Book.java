package Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A Book is an object that contains information about a book.
 * @author 
 * @version 1.2
 * @since 0.3
 */
public class Book implements Comparable<Book>, Serializable {
    
    private String isbn;
    private String title;
    private int edition;
    private double price;
    private ArrayList<Author> author;
    
    /**
     * Constructor, initializes all data members with specified arguments and 
     * creates a new book object.
     * @param isbn The ISBN number of the book.
     * @param title The title of the book.
     * @param edition The edition of the book.
     * @param price The price of the book.
     * @param author The name(s) of the author(s).
     */
    public Book(String isbn, String title, int edition, double price, 
            Author author) throws IllegalArgumentException {
        if(isbn == null || title == null || author == null || isbn.isEmpty() ||
                title.isEmpty()) {
            throw new IllegalArgumentException("Recieved null or empty field");
        }
        if(edition < 0 || price < 0) {
            throw new IllegalArgumentException("Recieved negative value");
        }
        
        this.isbn = isbn;
        this.title = title;
        this.edition = edition;
        this.price = price;
        this.author = new ArrayList<>();
        this.author.add(author);
    }
    
    /**
     * Return the ISBN number.
     * @return isbn The ISBN number of the book.
     */
    public String getIsbn() {
        return this.isbn;
    }
    
    /**
     * Set a new specified ISBN number
     * @param isbn The new ISBN number of the book
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    /**
     * Return the title.
     * @return title The title of the book.
     */
    public String getTitle() {
        return this.title;
    }
    
    /**
     * Set a new specified title.
     * @param title The new title of the book.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Return the edition.
     * @return edition The edition of the book. 
     */
    public int getEdition() {
        return this.edition;
    }
    
    /**
     * Set a new specified edition.
     * @param edition The new edition number of the book.
     */
    public void setEdition(int edition) {
        this.edition = edition;
    }
    
    /**
     * Return the price.
     * @return price The price of the book.
     */
    public double getPrice() {
        return this.price;
    }
    
    /**
     * Set a new specified price.
     * @param price The new price of the book.
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
    /**
     * Add authors with specified names to the author ArrayList.
     * @param name The name to be appended to the author list.
     */
    public void addAuthor(String name) {
            author.add(new Author(name));
    }
    
    /**
     * Return a clone of the author ArrayList.
     * @return A clone of the list of authors.
     */
    public ArrayList<Author> getAuthors() {
        return (ArrayList<Author>) author.clone();
    }
    
    public String getAuthorNames() {
        StringBuilder authorList = new StringBuilder();
        
        for(Author temp: author) {
            authorList.append(temp.getName()).append(",");
        }
        
        return authorList.toString();
    }
    
    /**
     * Override the comparable compareTo.
     * Sort the books by title in alphabetic order.
     * @param other The other book to be compared.
     * @return The integer 1 if two titles are identical.
     */
    @Override
    public int compareTo(Book other) {
        return this.getTitle().compareTo(other.getTitle());
    }
    
    /**
     * Print a books isbn, title, edition, price and authors.
     * @return The string of a books information.
     */
    @Override
    public String toString() {
        StringBuilder bookList = new StringBuilder();
        
        bookList.append("ISBN: ").append(getIsbn()).append(", Title: ");
        bookList.append(getTitle()).append(", Edition: ");
        bookList.append(getEdition()).append(", Price: ");
        bookList.append(getPrice()).append(", Authors: ");
        for(Author authors: author) {
            bookList.append(authors.getName()).append(", ");
        }
        
        bookList.append("\n");
        return bookList.toString();
    }
}
