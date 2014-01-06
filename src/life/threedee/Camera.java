package life.threedee;

import static java.lang.Math.PI;
import static life.threedee.game.GameUtilities.R_INC;
import static life.threedee.game.GameUtilities.SC_HEIGHT;
import static life.threedee.game.GameUtilities.SC_WIDTH;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
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
	
	private Vector rightU;
	private Vector upU;
	
	private int numProc;
	
	protected Color[][] fbuf;
	
	protected Vector rdir;
	protected Point rloc;
	
	private BufferedImage buf;
	private Graphics bufg;
	
	private Thread waiter;
	
	public Camera(Point loc, Vector dir){
		dx = width/SC_WIDTH;
		dy = height/SC_HEIGHT;
		this.loc = loc;
		this.dir = dir;
		
		this.setPreferredSize(new Dimension(SC_WIDTH, SC_HEIGHT));
		
		numProc = Runtime.getRuntime().availableProcessors();
		slaves = new ArrayList<CameraSlave>();
		int d = SC_WIDTH / numProc;
		for(int i = 0; i < numProc; i++){
			CameraSlave c = new CameraSlave(this, d * i, 0, d * (i + 1), SC_HEIGHT);
			c.start();
			slaves.add(c);
		}
		
		buf = new BufferedImage(SC_WIDTH, SC_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		bufg = buf.createGraphics();
		
		fbuf = new Color[SC_WIDTH][SC_HEIGHT];
	}
	
	public Camera(){
		this(new Point(5,1,0),new Vector(0,0,1).setScalar(1));
	}
	
	@Override
	public void paintComponent(Graphics g){
		paintBuffer(g);
		if(waiter != null)
			waiter.interrupt();
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

	public void registerWait(Thread t) {
		waiter = t;
	}
	
	public void drawRange(int x1, int y1, int x2, int y2){
		for(int x = x1; x < x2; x+=R_INC){
			for(int y = y1; y < y2; y+=R_INC){
				Vector v = rdir.add(getVectorForPixel(x+R_INC/2, y+R_INC/2, rightU, upU));
				Color c = closestInFront(v, rloc).c;
				if(c != null)
					setfbuf(x,y,c);
				else
					setfbuf(x,y,Color.WHITE);
			}
		}
	}
	
	public void paintBuffer(Graphics g){
		for(int x = 0; x < SC_WIDTH; x += R_INC){
			for(int y = 0; y < SC_HEIGHT; y += R_INC){
				bufg.setColor(fbuf[x][y]);
				bufg.fillRect(x, y, R_INC, R_INC);
			}
		}
		
		g.drawImage(buf, 0, 0, null);
	}
	
	protected Vector getVectorForPixel(int x,int y, Vector right, Vector up){
		int rx = x - SC_WIDTH / 2;
		int ry = SC_HEIGHT / 2 - y; // y is negated because for graphics top is 0, whereas in real life bottom is -
		double px = rx * dx;
		double py = ry * dy;
		
		Vector nright = right.setScalar(px);
		Vector nup = up.setScalar(py);
		return nup.add(nright);
	}
	
	private void setfbuf(int x,int y, Color c){
		fbuf[x][y] = c;
	}

	protected TColorTransfer closestInFront(Vector dir, Point px){
		TColorTransfer min = new TColorTransfer(Double.MAX_VALUE, Color.white, null);
		for(ThreeDeeObject p : objects){
			TColorTransfer o = p.getRData(dir, px, min.t);
			if(min.t > o.t && o.t >= 0 && o.t == o.t && o.c != null && o.c.getAlpha() != 0){
				min = o;
			}
		}
		return min;
	}
	
	public synchronized void mouseMoved(int x,int y){		
		double[] dirPolar = dir.polarTransform();
		
		dirPolar[0] += -PI/180 * x;
		dirPolar[1] += -PI/360 * y;
		if(Math.abs(dirPolar[1]) > PI/2){
			dirPolar[1] = PI / 2 * Math.signum(dirPolar[1]);
		}
		dir = Vector.fromPolarTransform(dirPolar[0], dirPolar[1], dir.s());
    }
	
    public synchronized void translate(Vector shift){
        loc = new Point(new Vector(loc).add(shift));
    }
    
    public synchronized void setDir(Vector dir){
    	
    }
    
    public void setObjects(List<ThreeDeeObject> o) {
    	this.objects = o;
    }
}
