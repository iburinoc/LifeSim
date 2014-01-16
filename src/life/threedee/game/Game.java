package life.threedee.game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import life.threedee.Point;
import life.threedee.ThreeDeeObject;
import life.threedee.Vector;
import life.threedee.game.maps.GameMap;
import life.threedee.game.maps.MapLocation;

import static life.threedee.game.GameUtilities.*;

/**
 * The main game class.  Contains the main method.
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 */
public class Game implements Runnable, Tickable{
	
	/**
	 * The number of ticks per second
	 */
	public static final int TICK_RATE = 60;
	
	/**
	 * The number of frames per second
	 */
	public static final int FRAME_RATE = 30;
	
	/**
	 * Creates a new game and starts it
	 * @param args
	 */
	public static void main(String[] args){
		new Game().run();
	}
	
	private List<ThreeDeeObject> objects;
	private List<Tickable> tickables;

    private List<Ghost> ghosts;
	
	private Player p;
	
	private GameMap m;
	
	private JFrame j;
	
	private Input i;
	
	private boolean running, gotExtraLife = false, lostLifeThisLevel = false;

    private int mode, level, pelletsEaten, score, lives = 2, preferredGhost = 1, ticksThisMode, gameStage, frightTicks, pointsPerGhost;
	
    private Object objLock;
    
	public Game() {
		j = new JFrame("Game");
		
		m = new GameMap(); // Create the map to give to the player
		
		p = new Player(this, m);
		
		objLock = new Object(); // create the lock on which to synchronize
		
		setObjects(new ArrayList<ThreeDeeObject>());
        setTickables(new ArrayList<Tickable>());
        ghosts = new ArrayList<Ghost>();
        ghosts.add(new Ghost(this, BLINKY));
        ghosts.add(new Ghost(this, PINKY));
        ghosts.add(new Ghost(this, INKY));
        ghosts.add(new Ghost(this, CLYDE));
        
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
		
		/* START REMOVE */
		{
		SpecialPointsConsumable spc = new SpecialPointsConsumable(new Point(0, 0, -2.5), 5);
		objects.add(spc);
		tickables.add(spc);
		}
		{
	        SpecialPointsConsumable spc = new SpecialPointsConsumable(new Point(-1, 0, -2.5), 3);
	        objects.add(spc);
	        tickables.add(spc);
	        }
		{
	        SpecialPointsConsumable spc = new SpecialPointsConsumable(new Point(-2, 0, -2.5), 4);
	        objects.add(spc);
	        tickables.add(spc);
	        }
		{
	        SpecialPointsConsumable spc = new SpecialPointsConsumable(new Point(1, 0, -2.5), 2);
	        objects.add(spc);
	        tickables.add(spc);
	        }
		{
	        SpecialPointsConsumable spc = new SpecialPointsConsumable(new Point(2, 0, -2.5), 1);
	        objects.add(spc);
	        tickables.add(spc);
	        }
		{
            SpecialPointsConsumable spc = new SpecialPointsConsumable(new Point(3, 0, -2.5), 100);
            objects.add(spc);
            tickables.add(spc);
            }
		/* END REMOVE */
	}
	
	// make the cursor invisible
	private void removeCursor() {
		Toolkit tk= p.getToolkit();
		Cursor transparent = tk.createCustomCursor(tk.getImage(""), new java.awt.Point(), "trans");
		j.setCursor(transparent);
	}

	// Add all consumables to the list of tickables
	private void tickableConsumables() {
		for(ThreeDeeObject o : m.getObjects()) {
			if(o instanceof Consumable) {
				tickables.add((Consumable)o);
			}
		}
	}
	
	@Override
	public void run() {
		removeCursor();
		
		tickableConsumables();
		
		RenderThread rt = new RenderThread();
		TickThread tt = new TickThread();
		
		// RenderThread and TickThread happen asynchronously to try to keep ticks as close to TICK_RATE as possible
		rt.start();
		tt.start();
	}

	private class RenderThread extends Thread {
		@Override
		public void run() {
			long frame_time = System.currentTimeMillis();
			int frame_delta = 0;
			
			int frameRateMillis = 1000/FRAME_RATE;
			while(running) {
				long frameT = System.currentTimeMillis();
				frame_delta += (int) (frameT - frame_time);
				frame_time = frameT;
				if(frame_delta >= frameRateMillis) {
					drawFrame();
					frame_delta -= frameRateMillis;
				}
			}
		}
	}
	
	private class TickThread extends Thread {
		@Override
		public void run() {
			long tick_time = System.currentTimeMillis();
			int tick_delta = 0;

			int tickRateMillis = 1000/TICK_RATE;
			while(running) {
				long tickT = System.currentTimeMillis();
				tick_delta += (int) (tickT - tick_time);
				tick_time = tickT;
				if(tick_delta >= tickRateMillis) {
					tickTickables(tick_delta);
					tick_delta -= tickRateMillis;
				}
			}
		}
	}
	
    @Override
    public void tick(){
        if (preferredGhost < 4 && !ghosts.get(preferredGhost).inside()) {
            preferredGhost++;
        }
        if (preferredGhost < 4) {
            ghosts.get(preferredGhost).addToTimer();
        }
        if (pelletsEaten == 240){
            die();
            for(Pellet pellet : m.pelletsList()) {
                pellet.spawn();
            }
            for(Energizer energizer : m.energyList()) {
                energizer.spawn();
            }
            level++;
            pelletsEaten = 0;
            lostLifeThisLevel = false;
        }
        if (score >= 10000 && !gotExtraLife){
            gotExtraLife = true;
            lives++;
        }
        Point loc = p.getLoc();
        MapLocation coords = new MapLocation(loc.x, loc.z);
        for (Ghost ghost : ghosts){
            Point ghostLoc = ghost.getLocation();
            MapLocation ghostCoords = new MapLocation(ghostLoc.x, ghostLoc.z);
            boolean[] open = INTERSECTIONS[coords.mx][coords.my].clone();
            if (!(open[0] || open[1] || open[2] || open[3])) {
                System.out.println("Are x: " + coords.mx + " y: " + coords.my);
                boolean[] xTile = INTERSECTIONS[coords.mx + ((loc.x % 1) + 1) % 1 > 0.5 ? 1 : -1][coords.my].clone();
                boolean[] zTile = INTERSECTIONS[coords.mx][coords.my + ((loc.z % 1) + 1) % 1 > 0.5 ? 1 : -1].clone();
                boolean offsetX = (xTile[0] || xTile[1] || xTile[2] || xTile[3]);
                boolean offsetZ = (zTile[0] || zTile[1] || zTile[2] || zTile[3]);
                coords = new MapLocation(coords.mx + (offsetX ? (((loc.x % 1) + 1) % 1 > 0.5 ? 1 : -1) : 0), 
                        coords.my + (offsetZ ? (((loc.z % 1) + 1) % 1 > 0.5 ? 1 : -1) : 0));
                System.out.println("Should be x: " + coords.mx + " y: " + coords.my);
            }
            if (coords.equals(ghostCoords)) {
                if (ghost.ghostNum != SCARED && ghost.ghostNum != SCARED_FLASHING && ghost.ghostNum != EATEN){
                    lives--;
                    lostLifeThisLevel = true;
                    die();
                } else if (ghost.ghostNum == SCARED || ghost.ghostNum == SCARED_FLASHING){
                    score += pointsPerGhost;
                    pointsPerGhost *= 2;
                    ghost.getAte();
                }
            }
        }
        for (Consumable c : m.consumableList()){
            Point consumableLoc = c.getCenter();
            MapLocation consumableCoords = new MapLocation(consumableLoc.x, consumableLoc.z);
            if (coords.equals(consumableCoords) && !c.getEaten()) {
                c.eat(this, p);
            }
        }
        if (mode != -1) {
            ticksThisMode++;
        } else {
            frightTicks++;
        }
        if (frightTicks > FRIGHTENED_DATA[getArraySafeLevel()][0]) {
            mode = gameStage % 2;
        }
        if (gameStage > 6 && mode != -1) {
            mode = 1;
        } else if (ticksThisMode == GameUtilities.MODE_TIMES[level][gameStage]) {
            ticksThisMode = 0;
            gameStage++;
            mode = gameStage % 2;
        }
    }
	
	private void drawFrame() {
		long startT = System.currentTimeMillis();
		synchronized(objLock) {
			p.calcBuffer();
		}
		p.repaint();
		p.registerWait(Thread.currentThread());
		try{
			Thread.sleep(1000);
		}
		catch(InterruptedException e){
		}
		long time = System.currentTimeMillis() - startT;
	}
	
	private void tickTickables(int delta){
		synchronized(objLock) {
			for(int i = 0; i < tickables.size(); i++){
				try{
					tickables.get(i).tick();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected void drawScore(Graphics g) {
		final int height = 50;
		g.setColor(Color.WHITE);
		g.setFont(GameUtilities.SCORE_FONT);
		g.drawString("Score: " + score, 5, GameUtilities.SC_HEIGHT - height);
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

    public int getPelletsEaten(){
        return pelletsEaten;
    }

    public int getPelletsRemaining(){
        return 240 - getPelletsEaten();
    }

    public Player getPlayer(){
        return p;
    }

    public List<Ghost> getGhosts(){
        return ghosts;
    }
    
    public void die() {
        p.setLoc(new Point(0, 1, -8.5));
        p.setDir(new Vector(-1, 0, 0));
        for(Ghost ghost : ghosts) {
            ghost.reset();
        }
    }
    
    public void startFrightened() {
        // ANDREY! THIS IS WHERE THE CODE FOR STARTING AND ENDING (MAYBE) FRIGHTENED MODE GOES!
        // ANDREY! I'M USING "ANDREY!" AS TODO NOW!
        mode = -1;
        frightTicks = 0;
        score += 50;
        pointsPerGhost = 200;
        for (Ghost ghost : ghosts) {
            ghost.scare(GameUtilities.FRIGHTENED_DATA[this.getArraySafeLevel()][0]);
        }
    }
    
    public void pelletEaten() {
        pelletsEaten++;
        score += 10;
        if (preferredGhost < 4) {
            ghosts.get(preferredGhost).addToCounter();
            ghosts.get(preferredGhost).resetTimer();
        }
    }
    
    public void pointsBonus() {
        score += GameUtilities.GAME_DATA[getArraySafeLevel()][2];
    }

    public int getTicksThisMode() {
        return ticksThisMode;
    }

    public int getGameStage() {
        return gameStage;
    }

    public int getPreferredGhost() {
        return preferredGhost;
    }
    
    public int getArraySafeLevel() {
        return (Math.min(level, 20));
    }
}
