package life.engine;


public abstract class Structure {
    public Tile location;
    public long hardness;
    public long height;
    public double speedModifier;
    public abstract boolean permeable(Creature c);
}
