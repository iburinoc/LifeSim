package life.engine;

import java.util.ArrayList;

public abstract class Tile {
    public long height;
    public double speedModifier;
    public ArrayList<Resource> resources;
    public double x;
    public double y;
    
    public abstract ArrayList<Resource> mine();
}