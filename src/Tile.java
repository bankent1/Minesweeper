import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile {
	Rectangle rect = new Rectangle();
	
	public Tile(int size) {
		rect.setHeight(size);
		rect.setWidth(size);
		rect.setFill(Color.CADETBLUE);
	}
	
	public Rectangle getRect() {
		return rect;
	}
}
