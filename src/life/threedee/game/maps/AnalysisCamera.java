package life.threedee.game.maps;

import java.awt.Graphics;

import life.threedee.Camera;
import life.threedee.Point;
import life.threedee.TColorTransfer;
import life.threedee.Vector;

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
