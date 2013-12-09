package life.engine.creatures;

import java.util.ArrayList;
import life.engine.land.Land;
import life.engine.land.LandObject;
import life.engine.resources.Grass;
import life.engine.resources.Resource;

public class Hare implements Herbivore {
    private double energy;
    private int x;
    private int y;
    private Land land;
    
    public Hare (int x, int y) {
        this.energy = 100;
        this.x = x;
        this.y = y;
    }
    
    public Hare (Hare parent1, Hare parent2) {
        energy = parent1.getEnergy() / 3 + parent2.getEnergy() / 3;
        parent1.setEnergy(2 * parent1.getEnergy() / 3);
        parent2.setEnergy(2 * parent2.getEnergy() / 3);
        x = parent1.getX() + parent2.getX();
        y = parent1.getY() + parent2.getY();
    }
    
    @Override
    public void step () {
        this.move();
        for (LandObject landObject: land.getObjects()) {
            if (landObject instanceof Resource && this.x == landObject.getX() && this.y == landObject.getY()) {
                this.feed((Resource) landObject);
            }
        }
        this.energy -= 1;
    }
    
    @Override
    public void move () {
        /*0 is up, clockwise from there.*/
        int[] preferredDirection = new int[4];
        ArrayList<LandObject> landObjects = land.getObjects();
        for (LandObject landObject: landObjects) {
            if (landObject instanceof Carnivore) {
                double directionX = landObject.getX() - this.getX();
                double directionY = landObject.getY() - this.getY();
                if (Math.abs(directionX) > Math.abs(directionY)) {
                    if (directionX < 0) {
                        preferredDirection[1]++;
                    } else {
                        preferredDirection[3]++;
                    }
                } else {
                    if (directionY < 0) {
                        preferredDirection[2]++;
                    } else {
                        preferredDirection[0]++;
                    }
                }
            } else if (landObject instanceof Grass) {
                double directionX = landObject.getX() - this.getX();
                double directionY = landObject.getY() - this.getY();
                if (Math.abs(directionX) > Math.abs(directionY)) {
                    if (directionX < 0) {
                        preferredDirection[3]++;
                    } else {
                        preferredDirection[1]++;
                    }
                } else {
                    if (directionY < 0) {
                        preferredDirection[0]++;
                    } else {
                        preferredDirection[2]++;
                    }
                }
            }
        }
        int bestDirection = 0;
        for (int i = 1; i < preferredDirection.length; i++) {
            if (preferredDirection[i] >= preferredDirection[bestDirection]) {
                bestDirection = i;
            }
        }
        switch(bestDirection) {
        case 0: this.y--;
            break;
        case 1: this.x++;
            break;
        case 2: this.y++;
            break;
        case 3: this.x--;
            break;
        }
    }

    @Override
    public boolean survives () {
        return this.energy > 0;
    }

    @Override
    public void feed (Resource resource) {
        if (resource instanceof Grass) {
            this.energy += resource.getEnergy() / 2;
        } else {
            this.energy += resource.getEnergy() / 4;
        }
        land.getObjects().remove(resource);
    }
    

    public double getEnergy () {
        return energy;
    }

    public void setEnergy (double energy) {
        this.energy = energy;
    }

    public int getX () {
        return x;
    }

    public void setX (int x) {
        this.x = x;
    }

    public int getY () {
        return y;
    }

    public void setY (int y) {
        this.y = y;
    }
    
    public Land getLand () {
        return land;
    }

    public void setLand (Land land) {
        this.land = land;
    }
    
    public String getString() {
        return "H";
    }
}