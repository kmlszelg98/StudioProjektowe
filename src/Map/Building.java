package Map;

import Schemes.Map.Colors;

import java.awt.*;
import java.util.Random;

public class Building  {
    private int width, height, rotation;
    private Point location;

    public Building (Point location, int width, int height) {
        Random r = new Random();
        this.location = new Point(location);
        this.width = width;
        this.height = height;
        this.rotation = r.nextInt(91);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        // Building
        g2d.setColor(Colors.BUILDING_1);
        g2d.rotate(Math.toRadians(rotation), location.x, location.y);
        g2d.fillRect(location.x - width/2, location.y - height/2, width, height);
//        // Center
//        g2d.setColor(Colors.LOCATION);
//        g2d.fillOval(location.x-2, location.y-2, 4, 4);

    }

    // Getters
    public Point getLocation() {
        return location;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}