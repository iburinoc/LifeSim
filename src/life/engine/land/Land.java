package life.engine.land;

import java.util.ArrayList;

public class Land {
    private ArrayList<LandObject> objects;
    private ArrayList<Structure> structures;
    private ArrayList<ArrayList<Tile>> land;
    
    public ArrayList<LandObject> getObjects() {
        return this.objects;
    }
    public void setObjects(ArrayList<LandObject> objects) {
        this.objects = objects;
    }
    public ArrayList<Structure> getStructures() {
        return structures;
    }
    public void setStructures(ArrayList<Structure> structures) {
        this.structures = structures;
    }
    public ArrayList<ArrayList<Tile>> getLand() {
        return land;
    }
    public void setLand(ArrayList<ArrayList<Tile>> land) {
        this.land = land;
    }
}
