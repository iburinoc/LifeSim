package life.engine.creatures;

import java.util.ArrayList;

import life.engine.land.Land;
import life.engine.land.LandObject;
import life.engine.resources.Grass;
import life.engine.resources.Resource;

public class Hare implements Herbivore{
    private double energy;
    private int x;
    private int y;
    private Land l;
    
    public Hare(int x, int y) {
        this.energy = 100;
        this.x = x;
        this.y = y;
    }
    
    public Hare(Hare parent1, Hare parent2) {
        energy = parent1.getEnergy()/3+parent2.getEnergy()/3;
        parent1.setEnergy(2*parent1.getEnergy()/3);
        parent2.setEnergy(2*parent2.getEnergy()/3);
        x = parent1.getX()+parent2.getX();
        y = parent1.getY()+parent2.getY();
    }
    
    @Override
    public void step() {
        this.move();
        for (LandObject lo: l.getObjects()) {
            if (lo instanceof Resource && this.x == lo.getX() && this.y == lo.getY()) {
                this.feed((Resource) lo);
            }
        }
        this.energy -= 1;
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
            } else if (o instanceof Grass) {
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
    public boolean survives() {
        return this.energy > 0;
    }

    @Override
    public void feed(Resource r) {
        if (r instanceof Grass) {
            this.energy += r.getEnergy()/2;
        } else {
            this.energy += r.getEnergy()/4;
        }
        l.getObjects().remove(r);
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
    
    public Land getLand() {
        return l;
    }

    public void setLand(Land l) {
        this.l = l;
    }
    
    public String getStr() {
        return "H";
    }
}