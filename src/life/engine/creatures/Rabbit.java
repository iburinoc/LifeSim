package life.engine.creatures;

import java.util.ArrayList;

import life.engine.land.Land;
import life.engine.land.LandObject;
import life.engine.resources.Grass;
import life.engine.resources.Resource;

public class Rabbit implements Herbivore{
    private double energy;
    private int x;
    private int y;
    private Land l;
    
    public Rabbit(Rabbit parent1, Rabbit parent2) {
        energy = parent1.getEnergy()/3+parent2.getEnergy()/3;
        parent1.setEnergy(2*parent1.getEnergy()/3);
        parent2.setEnergy(2*parent2.getEnergy()/3);
        x = parent1.getX()+parent2.getX();
        y = parent1.getY()+parent2.getY();
    }
    
    @Override
    public void step() {
    }
    
    @Override
    public void move() {
        /*0 is up, clockwise from there.*/
        int[] preferredDirection = new int[4];
        ArrayList<LandObject> lo = l.getObjects();
        for (LandObject o: lo) {
            if (o instanceof Carnivore) {
                double dx = o.getX()-this.getX();
                double dy = o.getY()-this.getY();
                if (Math.abs(dx)>Math.abs(dy)) {
                    if (dx<0) {
                        preferredDirection[3]++;
                    } else {
                        preferredDirection[1]++;
                    }
                } else {
                    if (dy<0) {
                        preferredDirection[0]++;
                    } else {
                        preferredDirection[2]++;
                    }
                }
            } else if (o instanceof Grass) {
                double dx = o.getX()-this.getX();
                double dy = o.getY()-this.getY();
                if (Math.abs(dx)>Math.abs(dy)) {
                    if (dx<0) {
                        preferredDirection[1]++;
                    } else {
                        preferredDirection[3]++;
                    }
                } else {
                    if (dy<0) {
                        preferredDirection[2]++;
                    } else {
                        preferredDirection[0]++;
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
    public void survives() {
        
    }

    @Override
    public void feed(Resource r) {
        if (r instanceof Grass) {
            this.energy += r.getEnergy()/2;
        } else {
            this.energy += r.getEnergy()/4;
        }
    }
    

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
