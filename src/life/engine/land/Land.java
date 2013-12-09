package life.engine.land;

import java.util.ArrayList;

import life.engine.creatures.Creature;

public class Land {
    private ArrayList<LandObject> objects;
    private ArrayList<Structure> structures;
    private ArrayList<ArrayList<Tile>> land;
    
    public Land(ArrayList<LandObject> objects, ArrayList<Structure> structures,
            ArrayList<ArrayList<Tile>> land) {
        super();
        this.objects = objects;
        this.structures = structures;
        this.land = land;
        for (LandObject lo: objects) {
            lo.setLand(this);
        }
    }

    public void step() {
        for (LandObject object: objects) {
            if (object instanceof Creature) {
                Creature c = (Creature) object;
                if (c.survives()) {
                    c.step();
                } else {
                    objects.remove(c);
                }
            }
        }
    }
    
    public void outputStringToConsole() {
        int minX = 0, minY = 0, maxX = 0, maxY = 0;
        for (LandObject lo: objects) {
            if (lo.getX() < minX) {
                minX = lo.getX();
            }
            if (lo.getY() < minY) {
                minY = lo.getY();
            }
            if (lo.getX() > maxX) {
                maxX = lo.getX();
            }
            if (lo.getY() > maxY) {
                maxY = lo.getY();
            }
        }
        System.out.println("---");
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                boolean thingThere = false;
                for (LandObject lo: objects) {
                    if (lo.getX() == i && lo.getY() == j) {
                        System.out.print(lo.getStr());
                        thingThere = true;
                    }
                }
                if (!thingThere) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        System.out.println("---");
    }
    
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
