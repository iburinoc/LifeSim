package life.threedee.game.maps;

/**
 * All map features have a map id so that they can be reconstructed deterministically
 * @author Sean
 *
 */
public interface MapFeature {
	public int getID();
}
