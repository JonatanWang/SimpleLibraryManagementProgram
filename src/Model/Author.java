package Model;

import java.io.Serializable;

/**
 * An Author is an object that contains a writer's information.
 * @author 
 * @version 1.2
 * @since 0.3
 */
public class Author implements Serializable {
    
    private String name;
    
    /**
     * Constructor, initializes data member name with specified name and 
     * create a new author object.
     * @param name The name of author.
     */
    public Author(String name) {
        this.name = name;
    }
    
    /**
     * Return the author's name.
     * @return name The name of the author.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Set a new specified author name.
     * @param name The new name of the books author.
     */
    public void changeName(String name) {
        this.name = name;
    }
    
    /**
     * Compare two authors with each other, useless method for this program.
     * @param other The other book to be compared.
     * @return true if the two authors are identical, false otherwise.
     */
    public boolean equals(Author other) {
        return this.name.equalsIgnoreCase(other.getName());
    }
    
    /**
     * Print author's name.
     * @return The information of the author(name).
     */
    @Override
    public String toString() {
        String info;
        info = this.name;
        return info;
    }
}
