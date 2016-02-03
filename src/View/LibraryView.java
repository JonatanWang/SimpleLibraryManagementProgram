package View;

import Model.Author;
import Model.Book;
import javafx.scene.layout.BorderPane;
import Model.CollectionOfBooks;
import java.util.ArrayList;
import javafx.animation.PathTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Path;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 * A LibraryView is an object that contains nodes of the GUI
 * @author 
 */
public class LibraryView extends VBox {
    
    private final Stage primaryStage;
    private AnimationView animation;
    private ArrayList<CollectionOfBooks> libraries;
    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private LibraryController controller;
    
    private FileChooser fileChooser;
    private BorderPane pane;
    private Dialog<Book> dialogAdd;
    private Dialog exitSave;
    private ToggleGroup radioGroup;
    private TextField searchField;
    private TabPane tabView;
    private ArrayList<TableView> tables;
    private ArrayList<Tab> tabs;
    private PathTransition transition;
    private ImageView image;
    private Path path;
    
    /**
     * Constructor of LibraryView
     * @param primaryStage  The stage for GUI
     */
    public LibraryView(Stage primaryStage) {
        
        this.primaryStage = primaryStage;
        this.libraries = new ArrayList<>();
        this.tables = new ArrayList<>();
        this.tabs = new ArrayList<>();
        this.controller = new LibraryController(libraries, primaryStage, this);
        initView();
        initAnimation();
    }
    
    /**
     * Return table views
     * @return tables   The tables shown in the GUI
     */
    public ArrayList<TableView> getTables() {
        return this.tables;
    }
    
    /**
     * Return the tab selected by user
     * @return tabView  The index of the tab array list selected by user
     */
    public int getSelectedTab() {
        return tabView.getSelectionModel().getSelectedIndex();
    }
    
    /**
     * Clear the text fields of the add-dialog and await for new inputs
     * @param isbnField The text field for ISBN to be filled
     * @param titleField    The text field for Title to be filled
     * @param authorField   The text field for Authors to be filled
     * @param editionField  The text field for Edition to be filled
     * @param priceField    The text field for Price to be filled
     */
    public void clearAddFields(TextField isbnField, TextField titleField, 
            TextField authorField, TextField editionField, TextField priceField) {
        clearSearchFields(isbnField, titleField, authorField);
        editionField.clear();
        priceField.clear();
    }
    
    /**
     * Clear the text field of the search area and await for new input
     * @param isbnField     The text field for ISBN to be searched
     * @param titleField    The text field for Title to be searched
     * @param authorField   The text field for Author to be searched
     */
    public void clearSearchFields(TextField isbnField, TextField titleField, 
            TextField authorField) {
        isbnField.clear();
        titleField.clear();
        authorField.clear();
    }
    
    /**
     * Create a new tab
     * @return tab The tab created
     */
    public Tab createTab() {
        Tab tab = new Tab();
        tab.setText("Library " + libraries.size());
        tab.setContent(initTableView());
        tab.setOnCloseRequest(new CloseTabHandler());
        tabs.add(tab);
        tabView.getSelectionModel().select(tab);
        return tab;
    }
    
    /**
     * Remove a selected tab
     * @param tab The table removed
     */
    public void removeTab(int tab) {
        if(tab < 0) {
            throw new ArrayIndexOutOfBoundsException("Tab is out of bounds");
        }
        else {
            libraries.remove(tab);
            tables.remove(tab);
            tabs.remove(tab);
            tabView.getTabs().remove(tab);
        }
    }
    
    /**
     * Initialize the file chooser to load an existing file
     */
    private void initFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "SER", "*.ser"));
    }
    /**
     * Initialize a dialog window for saving purpose
     * User can chose save or cancel
     */
    private void initSaveDialog() {
        exitSave = new Dialog();
        exitSave.setContentText("Save before closing?");
        exitSave.setTitle("Close and save");
        
        ButtonType yesButton = new ButtonType("Yes", ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        
        exitSave.getDialogPane().getButtonTypes().addAll(yesButton, noButton, cancelButton);
        exitSave.setResultConverter(new Callback<ButtonType, Integer>() {

            @Override
            public Integer call(ButtonType b) {
                if(b == yesButton) {
                    return 1;
                }
                else if(b == noButton) {
                    return 2;
                }
                else {
                    return 0;
                }
            }
        });
    }
    /**
     * Initialize a menu bar and add menu items
     * @param menuBar   The menu bar to be created
     * @return menuBar  The menu bar created
     */
    private MenuBar initMenuView(MenuBar menuBar) {
        Menu menu = new Menu("Menu");
        MenuItem createItem = new MenuItem("New Library");
        MenuItem addItem = new MenuItem("Add book");
        MenuItem sortItem = new MenuItem("Sort books");
        MenuItem removeItem = new MenuItem("Remove book");
        MenuItem exitItem = new MenuItem("Exit");
        
        createItem.setOnAction(new CreateLibraryHandler());
        addItem.setOnAction(new AddHandler());
        sortItem.setOnAction(new SortHandler());
        removeItem.setOnAction(new RemoveHandler());
        exitItem.setOnAction(new ExitHandler());
        
        menu.getItems().addAll(createItem,addItem,sortItem,removeItem,exitItem);
        
        Menu fileMenu = new Menu("File");
        MenuItem saveItem = new MenuItem("Save as");
        MenuItem loadItem = new MenuItem("Load file");
        
        saveItem.setOnAction(new SaveHandler());
        loadItem.setOnAction(new LoadHandler());
        
        fileMenu.getItems().addAll(saveItem, loadItem);
        
        Menu helpMenu = new Menu("Help");
        MenuItem versionItem = new MenuItem("Version");
        MenuItem aboutItem = new MenuItem("About");
        
        versionItem.setOnAction(new VersionHandler());
        aboutItem.setOnAction(new AboutHandler());
        
        helpMenu.getItems().addAll(versionItem, aboutItem);

        menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu, fileMenu, helpMenu);
        
        return menuBar;
    }
    
    /**
     * Initialize a table view which contains the tabs
     * Set columns for the table
     * @return table    The table initialized
     */
    private TableView initTableView() {
        TableView table = new TableView();
        table.setTableMenuButtonVisible(true);
        table.setEditable(false);
        TableColumn isbnColumn = new TableColumn("ISBN");
        isbnColumn.setMinWidth(100);
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        
        TableColumn titleColumn = new TableColumn("Title");
        titleColumn.setMinWidth(300);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        
        TableColumn editionColumn = new TableColumn("Edition");
        editionColumn.setMinWidth(50);
        editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
        
        TableColumn priceColumn = new TableColumn("Price");
        priceColumn.setMinWidth(80);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        // The method to read author names from ArrayList
        TableColumn authorsColumn = new TableColumn("Authors");
        authorsColumn.setMinWidth(300);
        authorsColumn.setCellValueFactory(new Callback<CellDataFeatures<Book,String>,
                ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Book, String> c) {
                return new SimpleStringProperty(c.getValue().getAuthorNames());
            }
        }) ;
        
        table.getColumns().addAll(isbnColumn, titleColumn, editionColumn, 
                priceColumn, authorsColumn);
        
        tables.add(table);
        return table;
    }
    
    /**
     * Initialize the buttons in a HBox
     * @return buttons  The buttons created
     */
    private HBox initButtonView() {
        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);
        Button refreshButton = new Button("Refresh");
        Button addButton = new Button("Add");
        Button removeButton = new Button("Remove");
        Button sortButton = new Button("Sort");
        buttons.getChildren().addAll(refreshButton, addButton, removeButton, sortButton);
        
        refreshButton.setOnAction(new RefreshHandler());
        addButton.setOnAction(new AddHandler());
        removeButton.setOnAction(new RemoveHandler());
        sortButton.setOnAction(new SortHandler());
        
        return buttons;
    }
    
    /**
     * Initialize a dialog window for the Add button
     */
    private void initAddDialogView() {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        
        TextField isbnField = new TextField();
        isbnField.setPromptText("Enter ISBN number");
        TextField titleField = new TextField();
        titleField.setPromptText("Enter title");
        TextField editionField = new TextField();
        editionField.setPromptText("Enter book edition");
        TextField priceField = new TextField();
        priceField.setPromptText("Enter price");
        TextField authorField = new TextField();
        authorField.setPromptText("Enter author names, separated with commas");
        authorField.setMinWidth(250);
        
        ButtonType addButton = new ButtonType("Add", ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        
        grid.add(new Label("ISBN: "), 0, 0);
        grid.add(isbnField, 1, 0);
        grid.add(new Label("Title: "), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new Label("Edition: "), 0, 2);
        grid.add(editionField, 1, 2);
        grid.add(new Label("Price: "), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Author: "), 0, 4);
        grid.add(authorField, 1, 4);
        
        dialogAdd = new Dialog<Book>();
        dialogAdd.setTitle("Add book");
        dialogAdd.setResizable(false);
        dialogAdd.getDialogPane().setHeaderText("Add new book to library");
        dialogAdd.getDialogPane().setContent(grid);
        dialogAdd.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);
        
        dialogAdd.setResultConverter(new Callback<ButtonType, Book>() {
            @Override
            public Book call(ButtonType b) {
                if (b == addButton) {
                    try {
                        int edition = Integer.parseInt(editionField.getText());
                        double price = Double.parseDouble(priceField.getText());
                        
                        String author = authorField.getText();
                        String[] authors = author.split(",");

                        Book temp = new Book(isbnField.getText(), titleField.getText(),
                        edition, price, new Author(authors[0]));
                        
                        for(int i = 1; i < authors.length; i++) {
                            temp.addAuthor(authors[i]);
                        }
                        
                        clearAddFields(isbnField, titleField, authorField, editionField,
                            priceField);

                        return temp;
                    }
                    catch (IllegalArgumentException ex) {
                        showAlert("Empty field or negative value, try again");
                    }
                }
                else if (b == cancelButton) {
                    clearAddFields(isbnField, titleField, authorField, editionField,
                            priceField);
                }
                return null;
            }
        });
    }
    /**
     * Initialize a HBox node for the Search area
     * Set radio buttons and the Search button
     * @return boxSearch    The search area created(HBox)
     */
    private HBox initSearchDialogView() {
        HBox boxSearch = new HBox(20);
        boxSearch.setAlignment(Pos.CENTER);
        
        radioGroup = new ToggleGroup();
        RadioButton isbnRadio = new RadioButton("ISBN");
        isbnRadio.setToggleGroup(radioGroup);
        RadioButton titleRadio = new RadioButton("Title");
        titleRadio.setToggleGroup(radioGroup);
        RadioButton authorRadio = new RadioButton("Author");
        authorRadio.setToggleGroup(radioGroup);
        
        Button searchButton = new Button("Search");
        
        searchField = new TextField();
        searchField.setPromptText("Enter search");
        searchField.setMinWidth(190);
        
        boxSearch.getChildren().addAll(isbnRadio, titleRadio, authorRadio, 
                searchField, searchButton);
        
        searchButton.setOnAction(new SearchHandler(isbnRadio, titleRadio, authorRadio));
        
        return boxSearch;
    }
    
    /**
     * Initialize the animation
     */
    private void initAnimation() {
        this.animation = new AnimationView(this, "book.png");
        this.path = animation.createPath();
        this.image = animation.createImage(80,80);
        this.transition = animation.createTransition(path, image);
        this.transition.setOnFinished(new LibraryView.AnimationFinishedHandler());
    }
    
    /**
     * Initialize the view of GUI
     */
    private void initView() {
        MenuBar menuBar = new MenuBar();
        menuBar = initMenuView(menuBar);
        
        pane = new BorderPane();
        tabView = new TabPane();
        
        pane.setCenter(tabView);
        pane.setBottom(initButtonView());
        pane.setTop(initSearchDialogView());
        pane.prefHeightProperty().bind(this.heightProperty());
        pane.prefWidthProperty().bind(this.widthProperty());
        
        initAddDialogView();
        initFileChooser();
        initSaveDialog();
        
        this.getChildren().addAll(menuBar, pane);
        
        controller.createLibrary(tabView);
        
        primaryStage.setOnCloseRequest(new CloseHandler());
    }
    
    /**
     * Show message by Alert
     * @param message   The message to be shown in Alert window
     */
    public void showAlert(String message) {
        alert.setHeaderText("");
        alert.setTitle("Alert!");
        alert.setContentText(message);
        alert.show();
    }
    
    /**
     * A CreateLibraryHandler object invokes method to create a new library
     */
    private class CreateLibraryHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            controller.createLibrary(tabView);
        }
    }
    
    /**
     * A CloseHandler object invokes method to close the window
     * User can choose Save or Discard the change
     */
    private class CloseHandler implements EventHandler<WindowEvent> {

        @Override
        public void handle(WindowEvent event) {
            event.consume();
            controller.closeWindow(exitSave, fileChooser, tabs);
        }
    }
    /**
     * An ExitHandler object invokes method to exit the program
     * User can choose Save or Discard the change
     */
    private class ExitHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            event.consume();
            controller.closeWindow(exitSave, fileChooser, tabs);
        }
    }
    /**
     * A CloseTabHandler invokes method to close a tab
     * User can decide whether to Save or Discard
     */
    private class CloseTabHandler implements EventHandler<Event> {

        @Override
        public void handle(Event event) {
            event.consume();
            controller.trySaveTab(fileChooser, exitSave);
        }
    }
    /**
     * An AddHandler invokes method to add a new book
     */
    private class AddHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            controller.addBook(dialogAdd, pane, path, image, transition);
        }
    }
    
    /**
     * A SearchHandler invokes method to search a book by ISBN, title or authors
     */
    private class SearchHandler implements EventHandler<ActionEvent> {
        private RadioButton isbnRadio;
        private RadioButton titleRadio;
        private RadioButton authorRadio;
        
        public SearchHandler(RadioButton isbnRadio, RadioButton titleRadio, 
                RadioButton authorRadio) {
            this.isbnRadio = isbnRadio;
            this.titleRadio = titleRadio;
            this.authorRadio = authorRadio;
        }

        @Override
        public void handle(ActionEvent event) {
            controller.searchBook(radioGroup, isbnRadio, titleRadio, authorRadio, 
                    searchField);
        }
    }
    
    /**
     * A RefreshHandler object invokes method to restore the content in the table
     */
    private class RefreshHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            controller.refreshTable();
        }
    }
    /**
     * A RemoveHandler object invokes method to remove a book
     */
    private class RemoveHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            controller.removeBook(tables);
        }
    }
    
    /**
     * A SortHandler object invokes method to reorder books in the table by alphabet order of titles
     */
    private class SortHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            controller.sortBooks();
        }
    }
    
    /**
     * A SaveHandler object invokes method to save the change in a file
     */
    private class SaveHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            try {
                controller.saveAs(fileChooser, tabs);
            }
            catch (NullPointerException ex) {
                showAlert(ex.getMessage());
            }
        }
    }
    
    /**
     * A LoadHandler object invokes method to load an existing file
     */
    private class LoadHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            controller.loadFile(fileChooser, tabView);
        }
    }
    
    /**
     * A VersionHandler object invokes method to show information of version
     */
    private class VersionHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            showAlert("Version 1.0");
        }
    }
    
    /**
     * An AboutHandler object invokes method to show the program author's information
     */
    private class AboutHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            showAlert("Created by: ");
        }
    }
    
    /**
     * An AnimationFinishedHandler object invokes method to terminate the animation effect
     */
    private class AnimationFinishedHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            pane.getChildren().removeAll(path,image);
        }
    }
}
