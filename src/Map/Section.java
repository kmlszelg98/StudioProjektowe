package Map;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by anka on 08.04.17.
 */

public class Section {
    private ArrayList<Point> points;
    private int index;
    private Color type;

    // Constructor
    public Section(int index) {
        this.index = index;
        this.points = new ArrayList<>();
    }

    // Public functions
    public void addPoint(Point point){
        points.add(point);
    }

    // Getters and Setters
    public int getIndex() {
        return index;
    }
    public void setType(Color type) {
        this.type = type;
    }
    public ArrayList<Point> getPoints() {
        return points;
    }
    public Color getType() {
        return type;
    }
}
