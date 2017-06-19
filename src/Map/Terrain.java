package Map;

import Schemes.Map.Colors;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by anka on 08.04.17.
 */

public class Terrain {
    // Size
    public int numRows;
    public int numCols;
    // Grid of terrain
    private Color[][] terrainGrid;
    private ArrayList<Section> sections;

    // Constructor
    public Terrain(String image) {
        // Buffering map
        BufferedImage map = null;
        try {
            map = ImageIO.read(this.getClass().getResource(image));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initializing
        this.numRows = map.getWidth();
        this.numCols = map.getHeight();
        this.terrainGrid = new Color[numRows][numCols];
        sections = new ArrayList<>();

        // Colloring city
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                terrainGrid[i][j] = Colors.CITY;
            }
        }

        // Finding roads
        for (int x = 0; x < numRows; x++) {
            for (int y = 0; y < numCols; y++) {
                int c = map.getRGB(x, y);
                Color color = new Color(c);
                if (color.getRed() > 150 && color.getGreen() > 150 && color.getBlue() > 150) {
                    terrainGrid[x][y] = Colors.ROAD;
                }
            }
        }

        // Variables
        int[][] tempGrid = new int[numRows][numCols];
        int[] id = new int[7000];
        int[] lut = new int[7000];

        for (int x = 0; x < numRows; x++) {
            for (int y = 0; y < numCols; y++) {
                tempGrid[x][y] = 0;
            }
        }
        int sectionsAmount = 1;
        for (int x = 0; x < 6999; x++) id[x] = x;

        // Marking sections
        for (int x = 1; x < numRows-1; x++) {
            for (int y = 1; y < numCols; y++) {
                if (terrainGrid[x][y] == Colors.CITY) {
                    int[] indexes = {tempGrid[x-1][y-1], tempGrid[x][y-1], tempGrid[x+1][y-1], tempGrid[x-1][y]};
                    int sum = IntStream.of(indexes).sum();

                    // New sections
                    if (sum == 0) {
                        tempGrid[x][y] = sectionsAmount;
                        sectionsAmount++;
                    }
                    else {
                        // Removing zeros
                        int n = 0;
                        for (int i = 0; i < indexes.length; i++) {
                            if (indexes[i] != 0) n++;
                        }

                        int[] nonZeros = new int[n];
                        int j = 0;

                        for (int i = 0; i < indexes.length; i++) {
                            if (indexes[i] != 0) {
                                nonZeros[j] = indexes[i];
                                j++;
                            }
                        }

                        // Getting min and max
                        int min = Arrays.stream(nonZeros).min().getAsInt();
                        int max = Arrays.stream(nonZeros).max().getAsInt();

                        // Assigning
                        if (min == max) tempGrid[x][y] = min;
                        else {
                            tempGrid[x][y] = min;
                            id = union(min, max, id);
                        }
                    }
                }
            }
        }

        // Unioning and making sections
        boolean found;
        for (int x = 0; x < 6999; x++) lut[x] = root(x,id);

        for (int x = 0; x < numRows; x++) {
            for (int y = 0; y < numCols; y++) {
                if (tempGrid[x][y] != 0) {
                    found = false;
                    tempGrid[x][y] = lut[tempGrid[x][y]];

                    // If section exists
                    for (Section section : sections) {
                        if (section.getIndex() == tempGrid[x][y]) {
                            found = true;
                            section.addPoint(new Point(x, y));
                            break;
                        }
                    }

                    // If section does not exist
                    if (!found){
                        Section section = new Section(tempGrid[x][y]);
                        section.addPoint(new Point(x,y));
                        sections.add(section);
                    }
                }
            }
        }

        // Type of sections and projection on terrainGrid  // TODO: 14.04.17 make it less random :D 
        Random r = new Random();
        for (Section section : sections){
            int random = r.nextInt(Colors.Sections.length);
            section.setType(Colors.Sections[random]);
            for (Point point : section.getPoints()){
                terrainGrid[point.x][point.y] = Colors.Sections[random];
            }
        }
    }

    // Private functions
    private int root(int index, int[] id ){
        int i = index;
        int wynik = index;

        while (id[i] != i){
            i = id[i];
            wynik = i;
        }
        return wynik;
    }
    private int[] union(int min, int max, int[] id){
        int[] wynik = id;
        wynik[root(min, wynik)] = root(max, id);
        return wynik;
    }

    // Drawing
    public void draw(Graphics g) {
        // Landscape
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Color terrainColor = terrainGrid[i][j];
                g.setColor(terrainColor);
                g.fillRect(i, j, 1, 1);
            }
        }
    }

    // Getters
    public Color[][] getTerrainGrid() {
        return terrainGrid;
    }
}