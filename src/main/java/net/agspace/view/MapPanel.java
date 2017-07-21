package net.agspace.view;

import net.agspace.control.PanelClickListener;
import net.agspace.model.PathMap;
import net.agspace.model.PathTile;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Andrew Lalis
 * Panel which displays a PathMap
 */
public class MapPanel extends JPanel implements Observer {

    private PathMap map;

    public static final int TILE_SIZE = 16;
    public static final int BORDER_SIZE = 2;
    //Colors for some aspects of rendering.
    private static final Color BORDER_COLOR = new Color(5, 0, 75);
    private static final Color TILE_COLOR_DEFAULT = Color.white;
    private static final Color TILE_COLOR_TRAVERSED = new Color(255, 233, 233);
    private static final Color TILE_COLOR_START = new Color(0, 192, 5);
    private static final Color TILE_COLOR_GOAL = new Color(255, 144, 0);

    public MapPanel(PathMap map){
        this.map = map;
        this.addMouseListener(new PanelClickListener(map));
        map.addObserver(this);
        this.setPreferredSize(getDisplaySize());
        this.setBackground(Color.white);
        this.setDoubleBuffered(true);
    }

    /**
     * Draw the tiles on the screen.
     * @param g The graphics context created by Swing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintBorders(g);
        paintTiles(g);
    }

    /**
     * Paints the borders of the map onto a graphics context.
     * @param g The graphics context.
     */
    private void paintBorders(Graphics g){
        g.setColor(BORDER_COLOR);
        Dimension size = getDisplaySize();
        for (int x = 0; x < size.width; x += (TILE_SIZE+BORDER_SIZE)){
            g.fillRect(x, 0, BORDER_SIZE, size.height);
        }
        for (int y = 0; y < size.height; y += (TILE_SIZE+BORDER_SIZE)){
            g.fillRect(0, y, size.width, BORDER_SIZE);
        }
    }

    /**
     * Paints the tiles of the map onto a graphics context.
     * @param g The graphics context.
     */
    private void paintTiles(Graphics g){
        for (int x = 0; x < map.getWidth(); x++){
            for (int y = 0; y < map.getHeight(); y++){
                int displayX = x*TILE_SIZE + (x+1)*BORDER_SIZE;
                int displayY = y*TILE_SIZE + (y+1)*BORDER_SIZE;
                PathTile tile = map.getTile(x, y);
                if (tile.isStart()){
                    g.setColor(TILE_COLOR_START);
                } else if (tile.isGoal()){
                    g.setColor(TILE_COLOR_GOAL);
                } else if (tile.isTraversed()){
                    g.setColor(TILE_COLOR_TRAVERSED);
                } else {
                    g.setColor(TILE_COLOR_DEFAULT);
                }
                g.fillRect(displayX, displayY, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    /**
     * Gets the real display size, based on the current map.
     * This is the size of each tile, plus the size of the borders.
     * @return A Dimension of the real width and height of the panel's display.
     */
    public Dimension getDisplaySize(){
        return new Dimension(map.getWidth()*TILE_SIZE + (map.getWidth()+1)*BORDER_SIZE,
                map.getHeight()*TILE_SIZE + (map.getHeight()+1)*BORDER_SIZE);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof PathMap) {
            this.repaint();
        }
    }
}