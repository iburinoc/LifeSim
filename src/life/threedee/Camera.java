package life.threedee;

import static java.lang.Math.PI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Camera{

	private Point loc;
	private Vector dir;
	
	private double 
			width  = 1.53465397596, 
			height = 1.15099048197;
	private int
			screenWidth  = 960,
			screenHeight = 720;
	
	private double dx,dy;
	
	private List<CameraSlave> slaves;
	
	private Image buffer;
	private Graphics bufg;
	
	private int threadsDone;
	private Thread cur;
	
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
		
		buffer = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
		bufg = buffer.getGraphics();
	}
	
	public Camera(){
		this(new Point(0,1,0),new Vector(1,0,1).setScalar(1));
	}
	
	private int count;
	
	private void modDir(){
		count++;
		if(count == 2){
			count = 0;
			double[] dirPolar = dir.polarTransform();
			double s = dir.s;
			dirPolar[0] += PI/48;
			dirPolar[0]%= 2 * PI;
			//dirPolar[1] = (dirPolar[0] % PI) - PI/2;
			dir = Vector.fromPolarTransform(dirPolar[0], dirPolar[1], s);
		}
	}
	
	public void draw(Graphics g, List<Plane> objects){
		
//		modDir();
		
		double[] dirPolar = dir.polarTransform();

		Vector upU = Vector.fromPolarTransform(dirPolar[0], PI/2 + dirPolar[1], 1);
		
		Vector rightU = Vector.fromPolarTransform(dirPolar[0] - PI/2, 0, 1);
		if(false){
			System.out.println("Polar Dir: yaw:" + dirPolar[0] + "; pitch: " + dirPolar[1]);
			System.out.println("upU:"+upU);
			System.out.println("rightU:"+rightU);
			System.out.println("dir"+dir);
		}
		
//		bufg.clearRect(0, 0, 1280, 720);
		drawRange(g,objects,0,0,screenWidth,screenHeight,0,rightU,upU);
		g.drawImage(buffer, 0, 0, null);
		/*
		threadsDone = 0;
		cur = Thread.currentThread();
		for(CameraSlave c : slaves){
			c.draw(g, objects, rightU, upU);
		}
		/*
		while(threadsDone < 4){
			try{
				Thread.sleep(1000);
			}
			catch (InterruptedException e){
			}
		}
		for(CameraSlave c : slaves){
			g.drawImage(c.getBuffer(), c.getX(), c.getY(), null);
		}
		*/
	}
	
	public void threadDone(){
		threadsDone++;
		cur.interrupt();
	}
	
	public void drawRange(Graphics g, List<Plane> objects, int x1, int y1, int x2, int y2, int xOff, Vector rightU, Vector upU){
		int inc = 4;
		for(int x = 0; x < screenWidth; x+=inc){
			for(int y = 0; y < screenHeight; y+=inc){
				Vector v = dir.add(getVectorForPixel(x, y, rightU, upU));
				Plane draw = closestInFront(objects, v, loc, x, y);
				if(draw != null)
					g.setColor(draw.c);
				else
					g.setColor(Color.WHITE);
				g.fillRect(x - xOff, y, inc, inc);
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
	
	public void mouseMoved(int x,int y){
		System.out.println(x + ";" + y);
		
		double[] dirPolar = dir.polarTransform();
		
		dirPolar[0] += -PI/180 * x;
		dirPolar[1] += -PI/360 * y;
		dir = Vector.fromPolarTransform(dirPolar[0], dirPolar[1], dir.s);
		/*
		Vector upU = Vector.fromPolarTransform(dirPolar[0], PI/2 + dirPolar[1], 1);
		Vector rightU = Vector.fromPolarTransform(dirPolar[0] - PI/2, 0, 1);
		dir = dir.add(getVectorForPixel(x,y,rightU,upU)).setScalar(1);
		*/
	}
	
	public void move(int d){
		double yaw = dir.polarTransform()[0];
		yaw -= PI / 2 * d;
		Vector mov = Vector.fromPolarTransform(yaw, 0, 1);
		loc = new Point(loc.x+mov.x,loc.y,loc.z+mov.z);
	}
}
