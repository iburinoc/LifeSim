package life.engine.creatures;

import life.engine.land.LandObject;
//TODO: Add canConsume(LandObject lo) method, so that we don't have to do that much confusing error prevention in the program.
public interface Creature extends LandObject{
    public void step();
    public void move();
    public boolean survives();
}