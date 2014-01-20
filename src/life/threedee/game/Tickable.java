package life.threedee.game;

/**
 * An interface representing an object that does an action per tick of the game.
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 *
 */
public interface Tickable{
    /**
     * The action that is to be performed each tick of the game.
     */
	public void tick();
}
