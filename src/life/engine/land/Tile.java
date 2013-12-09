package life.engine.land;

import java.util.ArrayList;

import life.engine.resources.Resource;

public abstract class Tile {
    public long height;
    public double speedModifier;
    public ArrayList<Resource> resources;
    public double x;
    public double y;
    
    public abstract ArrayList<Resource> mine();
}