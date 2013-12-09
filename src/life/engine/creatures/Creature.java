package life.engine.creatures;

import life.engine.land.LandObject;

public interface Creature extends LandObject{
    public void step();
    public void move();
    public void survives();
}