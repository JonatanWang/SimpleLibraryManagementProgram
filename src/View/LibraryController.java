package View;

import Model.Book;
import Model.CollectionOfBooks;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javafx.animation.PathTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Path;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * The LibraryController is the control part of MVC model
 * A LibraryController object defines the actions to be set on nodes in GUI
 * @author 
 */
public class LibraryController {
    private ArrayList<CollectionOfBooks> libraries;
    private Stage primaryStage;
    private LibraryView library;
    private ObservableList<Book> data;
    
    /**
     * Constructor of LibraryController
     * @param libraries The libraries(tabs) to show
     * @param primaryStage  The stage of the GUI
     * @param library The GUI
     */
    public LibraryController(ArrayList<CollectionOfBooks> libraries, 
            Stage primaryStage, LibraryView library) {
        this.libraries = libraries;
        this.primaryStage = primaryStage;
        this.library = library;
    }
    
    /**
     * Update the table view
     * Reload the items(books) in the observable array list
     * @param lib The index of tab(library Array List)
     */
    public void updateTableView(int lib) {
        if(library.getSelectedTab() < 0) {
            throw new ArrayIndexOutOfBoundsException("No libraries created");
        }
        if(libraries.get(lib).getSize() == 0) {
            library.showAlert("No results");
        }
        else {
            data = FXCollections.observableArrayList(libraries.get(lib).getBooks());
            library.getTables().get(lib).setItems(data);
        }
    }
    /**
     * Update the table view after search in a certain library(tab)
     * @param temp The search result saved in a temporary array list
     */
    public void updateSearchView(CollectionOfBooks temp) {
        if(temp.getSize() == 0) {
            library.showAlert("No results");
        }
        else {
            data = FXCollections.observableArrayList(temp.getBooks());
            library.getTables().get(library.getSelectedTab()).setItems(data);
        }
    }
    
    /**
     * Invoke the dialog window 
     * Ask user to choose whether Save, Cancel or Discard and Exit
     * @param exitSave The dialog window for Save and Exit
     * @param fileChooser The file chooser
     * @param tabs The tabs
     */
    public void trySave(Dialog exitSave, FileChooser fileChooser, ArrayList<Tab> tabs) {
        Optional result = exitSave.showAndWait();
        int temp = (int) result.get();
            
        switch(temp) {
            case 0:
                break;
            case 1:
                try {
                    saveAs(fileChooser, tabs);
                    primaryStage.close();
                }
                catch (NullPointerException ex) {
                    library.showAlert(ex.getMessage());
                    break;
                }
                break;
            case 2:
                primaryStage.close();
                break;
            default:
                break;
        }
    }
    
    /**
     * Invoke a dialog window when user closes a tab(library) 
     * Let user to choose a path and save changes in a file
     * @param fileChooser The file chooser
     * @param selected The library(tab) selected by user to shutdown
     */
    public void saveTabAs(FileChooser fileChooser, int selected) {
        fileChooser.setTitle("Save file");
        File name = fileChooser.showSaveDialog(primaryStage);
        if(name == null) {
            throw new NullPointerException("Save canceled");
        }
        try {
            libraries.get(selected).serializeToFile(name);
        } 
        catch (IOException ex) {
            library.showAlert(ex.getMessage());
            primaryStage.close();
        }
    }
    
    /**
     * Invoke a dialog Window when user click Exit
     * Let user to choose paths and save all libraries(tabs) in files
     * @param fileChooser The file Chooser
     * @param tabs The tabs(all)
     */
    public void saveAs(FileChooser fileChooser, ArrayList<Tab> tabs) {
        for(int i = 0; i < tabs.size(); i++) {
            fileChooser.setTitle("Save file library: " + (i+1));
            if(libraries.get(i).getSize() > 0) {
                File name = fileChooser.showSaveDialog(primaryStage);
                if(name == null) {
                    throw new NullPointerException("Save canceled");
                }
                try {
                    libraries.get(i).serializeToFile(name);
                } 
                catch (IOException ex) {
                    library.showAlert(ex.getMessage());
                    primaryStage.close();
                }
            }
        }
    }
    
    /**
     * Remove a book from the table
     * @param tables The tables in table view area
     */
    public void removeBook(ArrayList<TableView> tables) {
        int selected = library.getSelectedTab();
        updateTableView(selected);
        int index = tables.get(selected).getSelectionModel().
                getSelectedIndex();
        
        if(index < 0) {
            library.showAlert("Select book");
        }
        else {
            Book temp = (Book) data.get(index);
            data.remove(temp);
            libraries.get(selected).removeBook(temp);
            tables.get(selected).getSelectionModel().clearSelection();
        }
    }
    
    /**
     * Re-order the book items in the table
     */
    public void sortBooks() {
        libraries.get(library.getSelectedTab()).sortBooks();
        updateTableView(library.getSelectedTab());
    }
    
    /**
     * Create a new library(tab) in the table view area
     * @param tabView The tab pane
     */
    public void createLibrary(TabPane tabView) {
        libraries.add(new CollectionOfBooks());
        tabView.getTabs().add(library.createTab());
    }
    
    /**
     * Shutdown the program when user clicks the Close button on the window
     * Let user to choose Save or Discard
     * @param exitSave The dialog window for Save and Exit
     * @param fileChooser The file chooser
     * @param tabs The tabs(all)
     */
    public void closeWindow(Dialog exitSave, FileChooser fileChooser, ArrayList<Tab> tabs) {
        trySave(exitSave, fileChooser, tabs);
    }
    
    /**
     * Try to shutdown a tab
     * Let user to choose Discard, Cancel or Save changes
     * @param fileChooser The file Chooser
     * @param exitSave The dialog window for Save and Exit
     */
    public void trySaveTab(FileChooser fileChooser, Dialog exitSave) {
        int selected = library.getSelectedTab();
        Optional result = exitSave.showAndWait();
        int temp = (int) result.get();
        
        switch(temp) {
            case 0:
                break;
            case 1:
                try {
                    saveTabAs(fileChooser, selected);
                    try {
                        library.removeTab(selected);
                    }
                    catch(ArrayIndexOutOfBoundsException ex) {
                        library.showAlert("Error, cant close tab " + ex.getMessage());
                    }
                }
                catch (NullPointerException ex) {
                    library.showAlert(ex.getMessage());
                    break;
                }
                break;
            case 2:
                try {
                    library.removeTab(selected);
                }
                catch(ArrayIndexOutOfBoundsException ex) {
                    library.showAlert("Error, cant close tab");
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * Add book to the library
     * Invoke dialog window, add book, refresh table and show animation effect
     * @param dialogAdd The dialog window for add book
     * @param pane The pane for the add-book window
     * @param path  The path for image in animation
     * @param image The picture for animation
     * @param transition The transition of image in the animation
     */
    public void addBook(Dialog<Book> dialogAdd, BorderPane pane, Path path, 
        ImageView image, PathTransition transition) {
        
        Optional<Book> result = dialogAdd.showAndWait();
            
        if(result.isPresent()) {
            Book temp = result.get();
            int selected = library.getSelectedTab();
            libraries.get(selected).addBook(temp);
            updateTableView(selected);
            pane.getChildren().addAll(path,image);
            transition.play();
        }
    }
    
    /**
     * Search a book in a library(tab)
     * Read in searched text according the radio button selected
     * Find the books and refresh the table view
     * @param radioGroup The toggle group of buttons
     * @param isbnRadio The radio button for ISBN
     * @param titleRadio    The radio button for Title
     * @param authorRadio   The radio button for Authors
     * @param searchField   The text field in search area
     */
    public void searchBook(ToggleGroup radioGroup, RadioButton isbnRadio, 
            RadioButton titleRadio, RadioButton authorRadio, TextField searchField) {
        
        String search = searchField.getText();
            
        if(search == null || search.isEmpty()) {
            library.showAlert("Invalid search, try again");
        }
        else {
            CollectionOfBooks temp = new CollectionOfBooks();
                
            if(radioGroup.getSelectedToggle() == isbnRadio) {
                    
                ArrayList<Book> isbn = libraries.get(library.getSelectedTab()).
                        getBooksByIsbn(search);
                    
                for(Book found: isbn) {
                    temp.addBook(found);
                }
            }
            else if(radioGroup.getSelectedToggle() == titleRadio) {
                ArrayList<Book> title = libraries.get(library.getSelectedTab()).
                        getBooksByTitle(search);
                    
                for(Book found: title) {
                    temp.addBook(found);
                }
            }
            else if(radioGroup.getSelectedToggle() == authorRadio) {
                ArrayList<Book> author = libraries.get(library.getSelectedTab()).
                        getBooksByAuthor(search);
                
                for(Book found: author) {
                    temp.addBook(found);
                }
            } 
            updateSearchView(temp);
        }
    }
    
    /**
     * Refresh table view
     */
    public void refreshTable() {
        try {
            updateTableView(library.getSelectedTab());
        }
        catch(ArrayIndexOutOfBoundsException ex) {
            library.showAlert(ex.getMessage());
        }
    }
    
    /**
     * Load an existing file in a tab(library)
     * @param fileChooser   The file chooser    
     * @param tabView   The tab to hold the file(library)
     */
    public void loadFile(FileChooser fileChooser, TabPane tabView) {
        fileChooser.setTitle("Open file");
        try {
            File name = fileChooser.showOpenDialog(primaryStage);
            if(name != null) {
                createLibrary(tabView);
                libraries.get(library.getSelectedTab()).deSerializeFromFile(name);
                updateTableView(library.getSelectedTab());
            }
            else {
                library.showAlert("Canceled file load");
            }
        } 
        catch (IOException | ClassNotFoundException ex) {
            library.showAlert(ex.getMessage());
            primaryStage.close();
        } 
    }
}
