package life.threedee;

import static java.lang.Math.PI;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
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
	
	private List<CameraSlave> slaves;
	
	public Camera(Point loc, Vector dir){
		dx = width/screenWidth;
		dy = height/screenHeight;
		this.loc = loc;
		this.dir = dir;
		int numProc = Runtime.getRuntime().availableProcessors();
		slaves = new ArrayList<CameraSlave>();
		int d = screenWidth / numProc;
		for(int i = 0; i < numProc; i++){
			CameraSlave c = new CameraSlave(this, d * i, 0, d * (i + 1), screenHeight);
			c.start();
			slaves.add(c);
		}
	}
	
	public Camera(){
		this(new Point(0,1,0),new Vector(0,0,1).setScalar(1));
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
		
		Vector rightU = Vector.fromPolarTransform(dirPolar[0] - PI/2, 0, 1);
		
		System.out.println("upU:"+upU);
		System.out.println("rightU:"+rightU);
		System.out.println("dir"+dir);
		
//		drawRange(g,objects,0,0,screenWidth,screenHeight,rightU,upU);
		for(CameraSlave c : slaves){
			c.draw(g, objects, rightU, upU);
		}
		//System.out.println(System.)
	}
	
	public void drawRange(Graphics g, List<Plane> objects, int x1, int y1, int x2, int y2, int xOff, Vector rightU, Vector upU){
		for(int x = 0; x < screenWidth; x++){
			for(int y = 0; y < screenHeight; y++){
				Vector v = dir.add(getVectorForPixel(x, y, rightU, upU));
				Plane draw = closestInFront(objects, v, loc, x, y);
				if(draw != null)
					g.setColor(draw.c);
				else
					g.setColor(Color.WHITE);
				g.fillRect(x - xOff, y, 1, 1);
			}
		}
	}
	
	private Vector getVectorForPixel(int x,int y, Vector right, Vector up){
		int rx = x - screenWidth / 2;
		int ry = screenHeight / 2 - y; // y is negated because for graphics top is 0, whereas in real life bottom is -
		double px = rx * dx;
		double py = ry * dy;
		
		Vector nright = right.setScalar(px);
		Vector nup = up.setScalar(py);
		return nup.add(nright);
	}

	private Plane closestInFront(List<Plane> objects, Vector dir, Point px, int x, int y){
		final boolean debug = false;
		//System.out.println(dir + " : " + px);
		double minT = Double.POSITIVE_INFINITY;
		Plane minPlane = null;
		if(x == 240 && y == 180 && debug){
			System.out.println("Center:" + dir);
		}
		if(x == 0 && y == 0 && debug){
			System.out.println("Top Left:" + dir);
		}
		if(x == 478 && y == 358 && debug){
			System.out.println("Bot Right:" + dir);
		}
		for(Plane p : objects){
			double t = p.calculateT(dir, px);
			if(minT > t && t >= 0 && t == t){
				minT = t;
				minPlane = p;
			}
		}
		//System.out.println(minPlane);
		return minPlane;
	}
}
