package life.engine.test;

import java.util.ArrayList;

import life.engine.creatures.Hare;
import life.engine.creatures.Wolf;
import life.engine.land.Land;
import life.engine.land.LandObject;
import life.engine.land.Structure;
import life.engine.land.Tile;
import life.engine.resources.Grass;

public class EngineTest {
    public static void main(String[] args) {
        ArrayList<LandObject> objects = new ArrayList<LandObject>(0);
        objects.add(new Wolf(0, 0));
        objects.add(new Hare(5, 5));
        objects.add(new Grass(7, 3));
        objects.add(new Grass(7, 2));
        ArrayList<Structure> structures = new ArrayList<Structure>(0);
        ArrayList<ArrayList<Tile>> land = new ArrayList<ArrayList<Tile>>(0);
        Land l = new Land(objects, structures, land);
        l.outputStringToConsole();
        for (int i = 0; i < 50; i++) {
            l.step();
            l.outputStringToConsole();
            if (i == 5) {
                i = 5;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
