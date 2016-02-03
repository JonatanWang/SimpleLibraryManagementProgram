package View;

import javafx.animation.PathTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

/**
 * An animationView object invokes method to show animation effects
 * @author 
 */
public class AnimationView {
    private VBox root;
    private String image;
    
    /**
     * Constructor of AnimationView
     * @param root The pane for the image
     * @param image The picture selected to show in the animation
     */
    public AnimationView(VBox root, String image) {
        this.root = root;
        this.image = image;
    }
    
    /**
     * Create a path for the image
     * @return paths The path for the image
     */
    public Path createPath() {
        Path paths = new Path();
        LineTo line = new LineTo(0,50);
        line.xProperty().bind(root.widthProperty());
        paths.getElements().add(new MoveTo(50,50));
        paths.getElements().add(line);
        paths.setOpacity(0);
        return paths;
    }
    
    /**
     * Create a transition for the image
     * @param path The path for the image
     * @param img   The picture selected for the animation
     * @return thePath The transition for the image
     */
    public PathTransition createTransition(Path path, ImageView img) {
        PathTransition thePath = new PathTransition();
        thePath.setDuration(Duration.seconds(2.0));
        thePath.setPath(path);
        thePath.setNode(img);
        return thePath;
    }
    
    /**
     * Create an image for the animation effect
     * @param height The height of the image
     * @param width The width of the image
     * @return img  The image in the animation
     */
    public ImageView createImage(double height, double width) {
        Image images = new Image(this.getClass().getResource("/resources/" + this.image).
                toString());
        
        ImageView img = new ImageView(images);
        img.setFitHeight(height);
        img.setFitWidth(width);
        return img;
    }
    
}
