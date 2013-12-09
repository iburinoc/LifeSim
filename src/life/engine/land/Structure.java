package life.engine.land;

import life.engine.creatures.Creature;

public abstract class Structure {
    public Tile location;
    public long hardness;
    public long height;
    public double speedModifier;
    public abstract boolean permeable(Creature c);
}
