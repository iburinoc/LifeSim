package life.engine.creatures;

import java.util.ArrayList;
import life.engine.land.Land;
import life.engine.land.LandObject;

public class Wolf implements Carnivore {
    private double energy;
    private int x;
    private int y;
    private Land land;
    
    public Wolf (int x, int y) {
        this.energy = 100;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void step () {
        this.move();
        this.move();
        for (LandObject landObject : land.getObjects()) {
            if (landObject instanceof Creature && this.x == landObject.getX() && this.y == landObject.getY()) {
                this.feed((Creature) landObject);
            }
        }
        this.energy -= 5;
    }

    @Override
    public void move () {
        int[] preferredDirection = new int[4];
        ArrayList<LandObject> landObjects = land.getObjects();
        for (LandObject object: landObjects) {
            if (object instanceof Hare) {
                double directionX = object.getX() - this.getX();
                double directionY = object.getY() - this.getY();
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
    public void feed (Creature creature) {
        if (creature instanceof Hare) {
            this.energy += creature.getEnergy() / 2;
            land.getObjects().remove(creature);
        }
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

    public void setLand (Land l) {
        this.land = l;
    }
    
    public String getString () {
        return "W";
    }
}
