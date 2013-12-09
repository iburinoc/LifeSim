package life.engine.resources;

import life.engine.land.LandObject;

public interface Resource extends LandObject {
    public void step();
    public void consume();
    public void getValue();
    public double getEnergy();
}
