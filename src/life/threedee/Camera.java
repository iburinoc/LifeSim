package life.threedee;

import static java.lang.Math.PI;
import static life.threedee.game.GameUtilities.R_INC;
import static life.threedee.game.GameUtilities.SC_HEIGHT;
import static life.threedee.game.GameUtilities.SC_WIDTH;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import life.threedee.game.Tickable;

public class Camera extends JPanel{

	public final int INC = 4;
	
	protected Point loc;
	protected Vector dir;
	
	private double 
			width  = 1.53465397596, 
			height = 1.15099048197;
	
	private double dx,dy;
	
	private List<CameraSlave> slaves;
	
	protected List<ThreeDeeObject> objects;
	private List<Tickable> tickables;
	
	private Vector rightU;
	private Vector upU;
	
	protected Color[][] fbuf;
	
	protected Vector rdir;
	protected Point rloc;
	
	public Camera(Point loc, Vector dir){
		dx = width/SC_WIDTH;
		dy = height/SC_HEIGHT;
		this.loc = loc;
		this.dir = dir;
		
		this.setPreferredSize(new Dimension(SC_WIDTH, SC_HEIGHT));
		
		int numProc = Runtime.getRuntime().availableProcessors();
		slaves = new ArrayList<CameraSlave>();
		int d = SC_WIDTH / numProc;
		for(int i = 0; i < numProc; i++){
			CameraSlave c = new CameraSlave(this, d * i, 0, d * (i + 1), SC_HEIGHT);
			c.start();
			slaves.add(c);
		}
		
		fbuf = new Color[SC_WIDTH][SC_HEIGHT];
		
		objects = new ArrayList<ThreeDeeObject>();
		tickables = new ArrayList<Tickable>();
	}
	
	public Camera(){
		this(new Point(0,3.65,0),new Vector(1,0,1).setScalar(1));
	}
	
	@Override
	public void paintComponent(Graphics g){
		paintBuffer(g);
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
		for(int x = x1; x < x2; x+=R_INC){
			for(int y = y1; y < y2; y+=R_INC){
				Vector v = rdir.add(getVectorForPixel(x, y, rightU, upU));
				ThreeDeeObject draw = closestInFront(v, rloc, x, y);
				if(draw != null)
					setfbuf(x,y,draw.c());
				else
					setfbuf(x,y,Color.WHITE);
			}
		}
	}
	
	public void paintBuffer(Graphics g){
		for(int x = 0; x < SC_WIDTH; x += R_INC){
			for(int y = 0; y < SC_HEIGHT; y += R_INC){
				g.setColor(fbuf[x][y]);
				g.fillRect(x, y, R_INC, R_INC);
			}
		}
	}
	
	private Vector getVectorForPixel(int x,int y, Vector right, Vector up){
		int rx = x - SC_WIDTH / 2;
		int ry = SC_HEIGHT / 2 - y; // y is negated because for graphics top is 0, whereas in real life bottom is -
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
		for(ThreeDeeObject p : objects){
			double t = p.calculateT(dir, px);
			if(minT > t && t >= 0 && t == t){
				minT = t;
				minPlane = p;
			}
		}
		return minPlane;
	}
	
	public synchronized void mouseMoved(int x,int y){		
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
