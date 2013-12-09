package life.engine.creatures;

import java.util.ArrayList;

import life.engine.land.Land;
import life.engine.land.LandObject;

public class Wolf implements Carnivore{
    private double energy;
    private int x;
    private int y;
    private Land l;
    
    public Wolf(int x, int y) {
        this.energy = 100;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void step() {
        this.move();
        this.move();
        for (LandObject lo: l.getObjects()) {
            if (lo instanceof Creature && this.x == lo.getX() && this.y == lo.getY()) {
                this.feed((Creature) lo);
            }
        }
        this.energy -= 5;
    }

    @Override
    public void move() {
        int[] preferredDirection = new int[4];
        ArrayList<LandObject> lo = l.getObjects();
        for (LandObject o: lo) {
            if (o instanceof Hare) {
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
    public void feed(Creature c) {
        if (c instanceof Hare) {
            this.energy += c.getEnergy()/2;
            l.getObjects().remove(c);
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

    public Land getLand() {
        return l;
    }

    public void setLand(Land l) {
        this.l = l;
    }
    
    public String getStr() {
        return "W";
    }
}
