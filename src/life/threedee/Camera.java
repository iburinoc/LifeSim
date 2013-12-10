package life.threedee;

import java.awt.Graphics;
import java.util.List;

public class Camera{

	private Point loc;
	private Vector dir;
	
	private double 
			width  = 1.53465397596, 
			height = 1.15099048197;
	private int
			screenWidth  = 480,
			screenHeight = 360;
	
	private double dx,dy;
	
	public Camera(Point loc, Vector dir){
		dx = width/screenWidth;
		dy = height/screenHeight;
		this.loc = loc;
		this.dir = dir;
	}
	
	public Camera(){
		this(new Point(0,0,0),new Vector(0,0,1));
	}
	
	public void draw(Graphics g, List<Plane> objects){
		Vector right = Vector.UNIT_X;
		Vector up = Vector.UNIT_Y;
		for(int x = 0; x < screenWidth; x++){
			for(int y = 0; y < screenHeight; y++){
				Vector dir = getVector
			}
		}
	}
	
	private Vector getVectorForPixel(int x,int y, Vector right, Vector up){
		int rx = screenWidth / 2 - x;
		int ry = screenHeight / 2 - y;
		double px = rx * dx;
		double py = ry * dy;
		
		Vector nright = right.setScalar(px);
		Vector nup = right.setScalar(py);
		return nup.add(nright);
	}

	private Plane closestInFront(List<Plane> objects, Vector dir, Point px){
		double minT = Double.POSITIVE_INFINITY;
		Plane minPlane = null;
		for(Plane p : objects){
			double t = p.calculateT(dir, px);
			if(minT > t){
				minT = t;
				minPlane = p;
			}
		}
		
		return minPlane;
	}
}
