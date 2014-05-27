import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

/*
 * This class does not compile. It is just an example of how to dynamically
 * change the size of elements in a Javafx Application when you resize the 
 * window.
 */
public class GrowElements {
	//Allow elements to grow as the window is resized
    ColumnConstraints column0 = new ColumnConstraints();
    column0.setPercentWidth(20);
    ColumnConstraints column1 = new ColumnConstraints();
    column1.setPercentWidth(25);
    ColumnConstraints column2 = new ColumnConstraints();
    column2.setPercentWidth(10);
    ColumnConstraints column3 = new ColumnConstraints();
    column3.setPercentWidth(25);
    ColumnConstraints column4 = new ColumnConstraints();
    column4.setPercentWidth(20);
    grid.getColumnConstraints().addAll(column0, column1, column2, column3, column4);
    
    RowConstraints rowConstraints0 = new RowConstraints();
    rowConstraints0.setPercentHeight(10);
    RowConstraints rowConstraints1 = new RowConstraints();
    rowConstraints1.setPercentHeight(5);
    RowConstraints rowConstraints2 = new RowConstraints();
    rowConstraints2.setPercentHeight(10);
    RowConstraints rowConstraints3 = new RowConstraints();
    rowConstraints3.setPercentHeight(65);
    RowConstraints rowConstraints4 = new RowConstraints();
    rowConstraints4.setPercentHeight(10);
    grid.getRowConstraints().addAll(rowConstraints0, rowConstraints1, rowConstraints2, rowConstraints3, rowConstraints4);
    
    //set the minimum width and height of the window
    primaryStage.setMinWidth(500);
    primaryStage.setMinHeight(500);
}
