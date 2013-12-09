package life.engine.resources;

public class Grass implements Resource{
    private double energy;
    private int x;
    private int y;    

    @Override
    public void step() {
        energy += 0.1;
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
}
