package life.engine.resources;

import life.engine.land.Land;

public class Grass implements Resource{
    private double energy;
    private int x;
    private int y;    
    private Land l;

    public Grass(int x, int y) {
        this.energy = 1;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void step() {
        energy += 1;
    }
    
    @Override
    public void consume() {
        
    }

    @Override
    public void getValue() {
        
    }

    @Override
    public double getEnergy() {
        return this.energy;
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

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public Land getLand() {
        return this.l;
    }
    
    public void setLand(Land l) {
        this.l = l;
    }
    
    public String getStr() {
        return ".";
    }
}
