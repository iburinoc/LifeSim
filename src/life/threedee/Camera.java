package life.threedee;

import java.awt.Graphics;
import java.util.List;

import static java.lang.Math.PI;

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
		double[] dirPolar = dir.polarTransform();
		
		double yaw = Double.NaN;
		
		if(dirPolar[1] < 0){
			yaw = PI/2;
		}else if(dirPolar[1] > 0){
			yaw = 3*PI/2;
		}
		Vector upU = Vector.fromPolarTransform(yaw, PI/2 + dirPolar[1], 1);
		
		Vector rightU = Vector.fromPolarTransform(dirPolar[0], 0, 1);
		
		for(int x = 0; x < screenWidth; x++){
			for(int y = 0; y < screenHeight; y++){
				Vector v = dir.add(getVectorForPixel(x, y, rightU, upU));
				Plane draw = closestInFront(objects, v, loc);
				g.setColor(draw.c);
				g.fillRect(x, y, 1, 1);
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
