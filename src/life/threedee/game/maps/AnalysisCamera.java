package life.threedee.game.maps;

import java.awt.Graphics;

import life.threedee.Camera;
import life.threedee.Point;
import life.threedee.TColorTransfer;
import life.threedee.Vector;

/**
 * A fake camera used for doing the calculations required to analyze the map and determine which planes are visible from where.  Extends visibility to it's package for use in MapAnalysis
 * @author Sean
 *
 */
public class AnalysisCamera extends Camera{
	@Override
	public void paintComponent(Graphics g) {
		
	}
	
	void setLoc(Point loc) {
		this.loc = loc;
	}
	
	@Override
	protected Vector getVectorForPixel(int x, int y, Vector right, Vector up) {
		return super.getVectorForPixel(x, y, right, up);
	}

	@Override
	protected TColorTransfer closestInFront(Vector v, Point p) {
		return super.closestInFront(v, p);
	}
}
