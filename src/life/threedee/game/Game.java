package life.threedee.game;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import life.threedee.Point;
import life.threedee.ThreeDeeObject;
import life.threedee.Vector;
import life.threedee.game.maps.GameMap;
import life.threedee.game.maps.MapBuilder;
import life.threedee.game.maps.MapLocation;

public class Game implements Runnable, Tickable{
	public static final int TICK_RATE = 60;
	public static final int FRAME_RATE = 30;
	
	public static void main(String[] args){
		new Thread(new Game()).start();
	}
	
	private List<ThreeDeeObject> objects;
	private List<Tickable> tickables;

    private List<Ghost> ghosts;
	
	private Player p;
	
	private JFrame j;
	
	private Input i;
	
	private boolean running, first = false, second = false;

    private int mode, level, dotsEaten, score, lives = 2;
	
	public Game() {
		j = new JFrame("Game");
		
		GameMap m = new GameMap();
		
		p = new Player(this, m);
		
		setObjects(new ArrayList<ThreeDeeObject>());
        setTickables(new ArrayList<Tickable>());
		
        ghosts = new ArrayList<Ghost>();
        ghosts.add(new Blinky(this));
        ghosts.get(0).translate(new Vector(0.0,0.0,3.5*GameUtilities.MPT));
        ghosts.add(new Pinky(this));
        ghosts.get(1).translate(new Vector(0.0, 0.0, 0.5 * GameUtilities.MPT));
        ghosts.add(new Inky(this));
        ghosts.get(2).translate(new Vector(-2.0 * GameUtilities.MPT, 0.0, 0.5));
        ghosts.add(new Clyde(this));
        ghosts.get(3).translate(new Vector(2.0*GameUtilities.MPT,0.0,0.5));
        /* CRUISE ELROY SUMMONING RITUAL. REMOVE LATER*/
        ghosts.add(new Ghost(GameUtilities.GHOST_LOCATIONS[6], GameUtilities.GHOST_ORIENTATIONS[6], this, 6));
        ghosts.get(4).translate(new Vector(2.0*GameUtilities.MPT,0.0,3.5*GameUtilities.MPT));
        ghosts.add(new Ghost(GameUtilities.GHOST_LOCATIONS[7], GameUtilities.GHOST_ORIENTATIONS[7], this, 7));
        ghosts.get(5).translate(new Vector(-2.0*GameUtilities.MPT,0.0,3.5*GameUtilities.MPT));
		
		i = new Input(p, this, j);
		
		running = true;
		
		j.addMouseListener(i);
		j.addMouseMotionListener(i);
		j.addKeyListener(i);
		
		j.add(p);
		j.pack();
		j.setVisible(true);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tickables.add(p);
		tickables.add(this);
	}
	
	private void removeCursor() {
		Toolkit tk= p.getToolkit();
		Cursor transparent = tk.createCustomCursor(tk.getImage(""), new java.awt.Point(), "trans");
		j.setCursor(transparent);
	}
	
	private void betaObjects() {
		objects.addAll(MapBuilder.generateBetaScreenShotObjects());
		Pellet p = new Pellet(new Point(0.5, 0, 3.5));
		objects.add(p);
		tickables.add(p);
		
	}
	
	@Override
	public void run() {
		betaObjects();
		removeCursor();
		
		long tick_time = System.currentTimeMillis();
		int tick_delta = 0;
		
		long frame_time = System.currentTimeMillis();
		int frame_delta = 0;
		
		int tickRateMillis = 1000/TICK_RATE;
		int frameRateMillis = 1000/FRAME_RATE;
		while(running) {
			long frameT = System.currentTimeMillis();
			frame_delta += (int) (frameT - frame_time);
			frame_time = frameT;
			if(frame_delta >= frameRateMillis) {
				drawFrame();
				frame_delta -= frameRateMillis;
			}
			
			long tickT = System.currentTimeMillis();
			tick_delta += (int) (tickT - tick_time);
			tick_time = tickT;
			if(tick_delta >= tickRateMillis) {
				tickTickables(tick_delta);
				tick_delta -= tickRateMillis;
			}
//			for (ThreeDeeObject obj: objects) { // BAD.  Replace with tickables pls
//			    if (obj instanceof GhostPlane) {
//			        ((GhostPlane) obj).shiftTexture(false); // There now you'll notice it
//			    }
//			}
		}
	}

    @Override
    public void tick(){
        if (dotsEaten == 240){
            level++;
        }
        if (score >= 10000 && !first){
            first = true;
            lives++;
        }
        if (score >= 100000 && !second){
            second = true;
            lives++;
        }
        for (Ghost ghost : ghosts){
            Location loc = p.getLoc();
            Location ghostLoc = ghost.getLocation();
            MapLocation coords = new MapLocation(loc.x, loc.z);
            MapLocation ghostCoords = new MapLocation(ghostLoc.x, ghostLoc.z);
            if (coords.equals(ghostCoords) && mode != -1){
                lives--;
                die();
            }
        }
    }

    private void die(){
        p.setLoc(new Point(0, 1, -8.5));
        p.setDir(new Vector(-1, 0, 0));
    }
	
	private void drawFrame() {
		long startT = System.currentTimeMillis();
		p.calcBuffer();
		p.repaint();
		p.registerWait(Thread.currentThread());
		try{
			Thread.sleep(1000);
		}
		catch(InterruptedException e){
		}
		System.out.println("frame");
		long time = System.currentTimeMillis() - startT;
		System.out.println(time);
	}
	
	private void tickTickables(int delta){
		for(int i = 0; i < tickables.size(); i++){
			tickables.get(i).tick();
		}
	}
	
	public void addTickable(Tickable t){
		tickables.add(t);
	}
	
	public void setTickables(List<Tickable> l){
		tickables = Collections.synchronizedList(l);
	}
	
	public List<Tickable> tickables(){
		return tickables;
	}
	
	public void addObject(ThreeDeeObject o) {
		objects.add(o);
	}
	
	public void setObjects(List<ThreeDeeObject> o){
		objects = Collections.synchronizedList(o);
	}
	
	public List<ThreeDeeObject> objects() {
		return objects;
    }
    
    public int getLevel(){
        return level;
    }

    public int getMode(){
        return mode;
    }

    public int getDotsEaten(){
        return dotsEaten;
    }

    public int getDotsRemaining(){
        return 240 - getDotsEaten();
    }

    public Player getPlayer(){
        return p;
    }

    public List<Ghost> getGhosts(){
        return ghosts;
    }
}
