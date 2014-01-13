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

/**
 * Represents a basic camera.<br>
 * It renders objects in the objects List using a few worker threads, 
 * creating as many threads as processors available.
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 */
public class Camera extends JPanel {
	/**
	 * The current location of the camera
	 */
	protected Point loc;
	
	/**
	 * The currect direction the camera is facing
	 */
	protected Vector dir;
	
	/**
	 * Width of the virtual rectangle in game representing the screen;
	 */
	private final double width  = 1.53465397596;
	
	/**
	 * Height of the virtual rectangle in game representing the screen;
	 */
	private final double height = 1.15099048197;
	
	/**
	 * The delta representing one pixel across
	 */
	private final double dx;
	
	/**
	 * The delta representing one pixel up
	 */
	private final double dy;
	
	/**
	 * The list of worker threads used
	 */
	private List<CameraSlave> slaves;
	
	/**
	 * The list of {@code ThreeDeeObject}s to render
	 */
	protected List<ThreeDeeObject> objects;
	
	/**
	 * The Vector representing one unit right on the screen
	 */
	private Vector rightU;
	
	/**
	 * The Vector representing one unit up on the screen
	 */
	private Vector upU;
	
	/**
	 * The number of processors
	 */
	private int numProc;
	
	/**
	 * An array of colors that represents an intermediate storage method for storing the screen buffer
	 */
	protected Color[][] fbuf;
	
	/**
	 * The current direction at the time of the start of the render, so as to avoid visual tears from the user turning during the render
	 */
	protected Vector rdir;
	
	/**
	 * The current location at the time of the start of the render, so as to avoid visual tears from the user moving during the render
	 */
	protected Point rloc;

	/**
	 * The thread that is waiting on this thread to finish painting its buffer
	 */
	private Thread waiter;
	
	/**
	 * Creates a new camera at the give location and direction
	 * @param loc
	 * @param dir
	 */
	public Camera(Point loc, Vector dir) {
		this.loc = loc;
		this.dir = dir;
		
		this.setPreferredSize(new Dimension(SC_WIDTH, SC_HEIGHT));

		// initialize dx and dy
		dx = width/SC_WIDTH;
		dy = height/SC_HEIGHT;
		
		// get the number of worker threads to use and initialize them
		numProc = Runtime.getRuntime().availableProcessors();
		slaves = new ArrayList<CameraSlave>();
		int d = SC_WIDTH / numProc;
		for(int i = 0; i < numProc; i++){
			CameraSlave c = new CameraSlave(this, d * i, 0, d * (i + 1), SC_HEIGHT);
			c.start();
			slaves.add(c);
		}
		
		// initialize fbuf
		fbuf = new Color[SC_WIDTH][SC_HEIGHT];
	}
	
	/**
	 * Default constructor<br>
	 * Constructs a Camera at point (0, 0, 0) and direction (0, 0, 1)
	 */
	public Camera() {
		this(new Point(0,0,0),new Vector(0,0,1));
	}
	
	/**
	 * Paints the cached screen to the graphics object and then notifies the waiter, if there is one
	 */
	@Override
	public void paintComponent(Graphics g){
		paintBuffer(g);
		if(waiter != null)
			waiter.interrupt();
	}
	
	/**
	 * Calculates what should be drawn to the screen with the current location and direction
	 */
	public void calcBuffer(){
		double[] dirPolar = dir.polarTransform();
		
		upU = Vector.fromPolarTransform(dirPolar[0], PI/2 + dirPolar[1], 1);
		rightU = Vector.fromPolarTransform(dirPolar[0] - PI/2, 0, 1);
		rdir = dir;
		rloc = loc;
		for(CameraSlave c : slaves){
			c.draw();
		}
		while(notDone()); // wait for the worker threads to be done
	}
	
	/**
	 * iterates through all worker threads and if any are not done, return true as we must keep waiting
	 * @return
	 */
	public boolean notDone(){
		for(CameraSlave c : slaves){
			if(c.done() == false){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Calculates the buffer for the given range
	 * @param x1 The left x value
	 * @param y1 The top y value
	 * @param x2 The right x value
	 * @param y2 The bottom y value
	 */
	public void drawRange(int x1, int y1, int x2, int y2){
		for(int x = x1; x < x2; x+=R_INC){
			for(int y = y1; y < y2; y+=R_INC){
				// get the vector from the camera point to the given pixel on the virtual screen
				Vector v = rdir.add(getVectorForPixel(x+R_INC/2, y+R_INC/2, rightU, upU)); 
				
				// find the closest object for that given pixel and draw it's color
				Color c = closestInFront(v, rloc).c;
				
				if(c != null)
					setfbuf(x,y,c);
				else
					setfbuf(x,y,Color.WHITE);
			}
		}
	}
	
	/**
	 * Gets the vector from the middle of the virtual screen to the given pixel
	 * @param x
	 * @param y
	 * @param right
	 * @param up
	 * @return
	 */
	protected Vector getVectorForPixel(int x,int y, Vector right, Vector up){
		int rx = x - SC_WIDTH / 2;
		int ry = SC_HEIGHT / 2 - y; // y is negated because for graphics top is 0, whereas in real life bottom is -
		double px = rx * dx;
		double py = ry * dy;
		
		Vector nright = right.setScalar(px);
		Vector nup = up.setScalar(py);
		return nup.add(nright);
	}
	
	/**
	 * Finds the closest object to the camera from the given point and distance
	 * @param dir
	 * @param px
	 * @return
	 */
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
	
	/**
	 * Sets a location in the buffer to the given colour
	 * @param x
	 * @param y
	 * @param c
	 */
	private void setfbuf(int x,int y, Color c){
		fbuf[x][y] = c;
	}
	
	/**
	 * Register a given thread to be sent an interrupt when done painting the buffer
	 * @param t
	 */
	public void registerWait(Thread t) {
		waiter = t;
	}
	
	/**
	 * Paints the current fbuf to the given graphics context
	 * @param g
	 */
	public void paintBuffer(Graphics g){
		int d = SC_WIDTH / numProc;
		for(int i = 0; i < numProc; i++){
			for(int x = d * i; x < d * (i + 1); x += R_INC){
				for(int y = 0; y < SC_HEIGHT; y += R_INC){
					g.setColor(fbuf[x][y]);
					g.fillRect(x, y, R_INC, R_INC);
				}
			}
		}
	}
	
	/**
	 * Moves the direction of the camera based on the delta given in x and y
	 * @param x
	 * @param y
	 */
	public synchronized void mouseMoved(int x,int y){		
		double[] dirPolar = dir.polarTransform();
		
		dirPolar[0] += -PI/180 * x;
		dirPolar[1] += -PI/360 * y;
		if(Math.abs(dirPolar[1]) > PI/2){
			dirPolar[1] = PI / 2 * Math.signum(dirPolar[1]);
		}
		dir = Vector.fromPolarTransform(dirPolar[0], dirPolar[1], dir.s());
    }
	
	/**
	 * Translates the camera by the given amount
	 * @param shift
	 */
    public synchronized void translate(Vector shift){
        loc = new Point(new Vector(loc).add(shift));
    }
    
    /**
     * Sets the direction to the given value
     * @param dir
     */
    public synchronized void setDir(Vector dir){
    	this.dir = dir;
    }
    
    /**
     * Sets the list of objects
     * @param o
     */
    public void setObjects(List<ThreeDeeObject> o) {
    	this.objects = o;
    }
}
