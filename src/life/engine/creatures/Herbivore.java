package life.engine.creatures;

import life.engine.resources.Resource;

public interface Herbivore extends Creature {
    public void feed(Resource resource);
}
