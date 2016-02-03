package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A CollectionOfBooks is an object that contains a list of books.
 * @author 
 * @version 1.2
 * @since 0.3
 */
public class CollectionOfBooks {
    
    private ArrayList<Book> books;
    
    /**
     * Constructor, initializes a new ArrayList that can contain book objects
     */
    public CollectionOfBooks() {
        this.books = new ArrayList<>();
    }
    
    /**
     * Get the size of the collection of books
     * @return books.size()
     */
    public int getSize() {
        return books.size();
    }
    
    /**
     * Get the books from the collection of books
     * @return books
     */
    public ArrayList getBooks() {
        return (ArrayList<Book>) books.clone();
    }
    
    /**
     * Serialize books to the specified file.
     * @param filename The appointed file name under the package directory.
     * @throws IOException The exception might occur.
     */
    public void serializeToFile(File filename) throws IOException {
		
	ObjectOutputStream out = null;
		
        try {
            out = new ObjectOutputStream(
            new FileOutputStream(filename));
            out.writeObject(books);
        }
        finally {
            try {
		if(out != null)	out.close();
            } catch(Exception e) {}
        }
    }

    /**
     * Deserialize the books from the specified file.
     * @param filename The appointed file name under the package directory.
     * @throws IOException The exceptions might occur.
     * @throws ClassNotFoundException The exception might occur.
     */
    @SuppressWarnings("unchecked")
    public void deSerializeFromFile(File filename) throws IOException,
            ClassNotFoundException {
        
        ObjectInputStream in = null;
		
        try {
            in = new ObjectInputStream(new FileInputStream(filename));
            System.out.println("Found file: " + filename);
            // readObject returns a reference of type Object, 
            // hence the down-cast
            books = (ArrayList<Book>) in.readObject(); 
        }
        finally {
            try {
                if(in != null) 	in.close();
            } catch(Exception e) {}			
        }
    }
   
    /**
    * Adds a book object to the ArrayList.
    * @param book The object of a new book.
    */ 
    public void addBook(Book book) {
        this.books.add(book);
    }
    
    /**
     * Return a ArrayList of the books with the specified title. 
     * A search method.
     * @param title The title of the book to be searched.
     * @return titles The the book of the specified title founded.
     */
    public ArrayList getBooksByTitle(String title) {
        ArrayList<Book> titles = new ArrayList<>();
        for(Book temp: books) {
            if(temp.getTitle().contains(title)) {
                titles.add(temp);
            }
        }
        return titles;
    }
    
    /**
     * Return a ArrayList of the books with the specified author. 
     * A search method
     * @param author The name of the author to be searched.
     * @return authors The books of the specified author founded.
     */
    public ArrayList getBooksByAuthor(String author) {
        ArrayList<Book> authors = new ArrayList<>();
        
        for(Book temp: books) {
            ArrayList<Author> auth = temp.getAuthors();
            for(Author a: auth){
                if(a.getName().contains(author)){
                    authors.add(temp);
                }
            }
        }
        return authors;
    }
    
    /**
     * Return a ArrayList of the books with the specified ISBN number. 
     * A search method
     * @param isbn The ISBN number to be searched.
     * @return isbns The books with the specified ISBN number found.
     */
    public ArrayList getBooksByIsbn(String isbn) {
        ArrayList<Book> isbns = new ArrayList<>();
        for(Book temp: books) {
            if(temp.getIsbn().contains(isbn)) {
                isbns.add(temp);
            }
        }
        return isbns;
    }
    
    /**
     * Remove a specified book object from the ArrayList
     * Determine whether the book is deleted successfully or not.
     * @param deleteBook The book is to be deleted.
     * @return true if the book is removed, false otherwise.
     */
    public boolean removeBook(Book deleteBook) {
        return books.remove(deleteBook);
    }
    
    /**
     * Remove a book object specified by title from the ArrayList.
     * Determine whether the book is deleted or not.
     * @param title The title of the book to be removed.
     * @return true if the book is removed, false otherwise.
     */
    public boolean removeBookByTitle(String title) {
        for(Book temp: books) {
            if(temp.getTitle().equalsIgnoreCase(title)) {
                books.remove(temp);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Sort the ArrayList books by a sort method from the Collections class.
     */
    public void sortBooks() {
        Collections.sort(books);
    }
    
    /**
     * Print all of the books information.
     * @return The information of books.
     */
    @Override
    public String toString() {
        StringBuilder bookList = new StringBuilder();
        
        for (Book book : books) {
            bookList.append(book);
        }
        
        return bookList.toString();
    }
}
