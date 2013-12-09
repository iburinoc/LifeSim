package life.engine.creatures;

import life.engine.land.Land;

public class Wolf implements Carnivore{
    private double energy;
    private int x;
    private int y;
    private Land l;
    
    @Override
    public void step() {
        
    }

    @Override
    public void move() {
        
    }

    @Override
    public void survives() {
        
    }

    @Override
    public void feed(Creature c) {
        
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

    public Land getL() {
        return l;
    }

    public void setL(Land l) {
        this.l = l;
    }
}
