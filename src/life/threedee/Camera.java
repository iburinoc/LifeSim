package life.threedee;

import static java.lang.Math.PI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import life.lib.Tickable;

public class Camera extends JPanel{

	public final int INC = 4;
	
	protected Point loc;
	protected Vector dir;
	
	private double 
			width  = 1.53465397596, 
			height = 1.15099048197;
	public int
			screenWidth  = 960,
			screenHeight = 720;
	
	private double dx,dy;
	
	private List<CameraSlave> slaves;
	
	private BufferedImage buffer;
	private Graphics2D bufg;
	
	private int threadsDone;
	private Thread cur;
	
	protected List<ThreeDeeObject> objects;
	private List<Tickable> tickables;
	
	private Vector rightU;
	private Vector upU;
	
	protected Color[][] fbuf;
	
	protected Vector rdir;
	protected Point rloc;
	
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
		
		fbuf = new Color[screenWidth][screenHeight];
		
		objects = new ArrayList<ThreeDeeObject>();
		tickables = new ArrayList<Tickable>();
	}
	
	public Camera(){
		this(new Point(0,1.65,0),new Vector(1,0,1).setScalar(1));
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
			dir = Vector.fromPolarTransform(dirPolar[0], dirPolar[1], s);
		}
	}
	
	@Override
	public void paintComponent(Graphics g){
		paintBuffer(g);
	}
	
	public void tickTickables(){
		int b = 0;
		for(int i = 0; i < tickables.size(); i++){
			Tickable t = tickables.get(i);
			t.tick();
		}
	}
	
	private long last;
	
	public void draw(Graphics g){
		long t = System.currentTimeMillis();
		paintBuffer(g);
		long t2 = System.currentTimeMillis();
		System.out.println("Time: " + (t2 - t));
		System.out.println("Last: " + (last - t2));
		last = t2;
	}
	
	public void calcBuffer(){
		double[] dirPolar = dir.polarTransform();

		upU = Vector.fromPolarTransform(dirPolar[0], PI/2 + dirPolar[1], 1);
		rightU = Vector.fromPolarTransform(dirPolar[0] - PI/2, 0, 1);
		rdir = dir;
		rloc = loc;
		for(CameraSlave c : slaves){
			c.draw();
		}
		while(notDone());
	}
	
	public boolean notDone(){
		for(CameraSlave c : slaves){
			if(c.done() == false){
				return true;
			}
		}
		return false;
	}
	
	public void drawRange(int x1, int y1, int x2, int y2, int xOff){
		for(int x = x1; x < x2; x+=INC){
			for(int y = y1; y < y2; y+=INC){
				Vector v = rdir.add(getVectorForPixel(x, y, rightU, upU));
				ThreeDeeObject draw = closestInFront(v, rloc, x, y);
				if(draw != null)
					setfbuf(x,y,draw.c());
				else
					setfbuf(x,y,Color.white);
			}
		}
	}
	
	public void paintBuffer(Graphics g){
		for(int x = 0; x < screenWidth; x += INC){
			for(int y = 0; y < screenHeight; y += INC){
				g.setColor(fbuf[x][y]);
				g.fillRect(x, y, INC, INC);
			}
		}
		
		g.setColor(Color.black);
		int d = screenWidth / 4;
		for(int i = 0; i < 4; i++);
			//g.drawLine(d * i, 0, d * (i ), screenHeight);
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
	
	private void setfbuf(int x,int y, Color c){
		synchronized(fbuf){
			fbuf[x][y] = c;
		}
	}

	private ThreeDeeObject closestInFront(Vector dir, Point px, int x, int y){
		final boolean debug = false;
		//System.out.println(dir + " : " + px);
		double minT = Double.POSITIVE_INFINITY;
		ThreeDeeObject minPlane = null;
		if(x == 240 && y == 180 && debug){
			System.out.println("Center:" + dir);
		}
		if(x == 0 && y == 0 && debug){
			System.out.println("Top Left:" + dir);
		}
		if(x == 478 && y == 358 && debug){
			System.out.println("Bot Right:" + dir);
		}
		for(ThreeDeeObject p : objects){
			double t = p.calculateT(dir, px);
			if(minT > t && t >= 0 && t == t){
				minT = t;
				minPlane = p;
			}
		}
		//System.out.println(minPlane);
		return minPlane;
	}
	
	public synchronized void mouseMoved(int x,int y){
//		System.out.println(x + ";" + y);
		
		double[] dirPolar = dir.polarTransform();
		
		dirPolar[0] += -PI/180 * x;
		dirPolar[1] += -PI/360 * y;
		if(Math.abs(dirPolar[1]) > PI/2){
			dirPolar[1] = PI / 2 * Math.signum(dirPolar[1]);
		}
		dir = Vector.fromPolarTransform(dirPolar[0], dirPolar[1], dir.s);
    }

    public synchronized void move(int d) {
        if (d < 4) {
            double[] pt = dir.polarTransform();
            pt[0] += PI / 2 * d;
            Vector mov = Vector.fromPolarTransform(pt[0], d % 2 == 1 ? 0 : (d == 0 ? pt[1] : -pt[1]), 1);
            loc = new Point(loc.x+mov.x,loc.y+mov.y,loc.z+mov.z);
        }
    }

    /*
    public void move(int d){
        Vector mov = Vector.fromPolarTransform(dir.polarTransform() += PI / 2 * d, 0, 1);
        loc = new Point(loc.x+mov.x,loc.y+mov.y,loc.z+mov.z);
    }
    */

	public synchronized void scroll(int d){
		dir = dir.setScalar(Math.max(Math.min(dir.s + -d / 10.0, 5), 1e-100));
		System.out.println(dir.s);
	}
	
    public synchronized void translate(Vector shift){
        loc = new Point(new Vector(loc).add(shift));
    }
	
	public synchronized void add(ThreeDeeObject o){
		objects.add(o);
	}
	
	public synchronized void setObjects(List<ThreeDeeObject> o){
		objects = o;
	}
	
	public synchronized void addTickable(Tickable t){
		tickables.add(t);
	}
}
