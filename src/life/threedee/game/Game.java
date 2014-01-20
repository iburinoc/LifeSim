package life.threedee.game;

import static life.threedee.game.GameUtilities.BLINKY;
import static life.threedee.game.GameUtilities.CLYDE;
import static life.threedee.game.GameUtilities.EATEN;
import static life.threedee.game.GameUtilities.FRIGHTENED_DATA;
import static life.threedee.game.GameUtilities.INKY;
import static life.threedee.game.GameUtilities.INTERSECTIONS;
import static life.threedee.game.GameUtilities.PINKY;
import static life.threedee.game.GameUtilities.SCARED;
import static life.threedee.game.GameUtilities.SCARED_FLASHING;
import static life.threedee.game.GameUtilities.open;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import life.threedee.Point;
import life.threedee.ThreeDeeObject;
import life.threedee.Vector;
import life.threedee.game.demo.Demo;
import life.threedee.game.demo.InputPlayback;
import life.threedee.game.maps.GameMap;
import life.threedee.game.maps.MapLocation;

/**
 * The main game class.  Contains the main method.
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 * 
 */
public class Game implements Runnable, Tickable{
	
	/**
	 * The number of ticks per second.
	 */
	public static final int TICK_RATE = 60;
	
	/**
	 * The number of frames per second.
	 */
	public static final int FRAME_RATE = 30;
	
	/**
	 * Creates a new game and starts it.
	 * @param args This needs to be here.
	 */
	public static void main(String[] args){
		new Game().run();
	}
	
	/**
	 * The random number generator for random number generating purposes
	 */
	public Random rand;
	
	/**
	 * The list of all objects
	 */
	private List<ThreeDeeObject> objects;
	
	/**
	 * The list of all tickables
	 */
	private List<Tickable> tickables;

	/**
	 * The ghosts
	 */
    private List<Ghost> ghosts;
	
    /**
     * The player
     */
	private Player p;
	
	/**
	 * The object containing map data
	 */
	private GameMap m;
	
	/**
	 * The window containing the game
	 */
	private JFrame j;
	
	/**
	 * The input object handling user input
	 */
	private Input i;
	
	/**
	 * The input used during the game for Playback and Record
	 */
	private Input gameI;
	
	// some data
	private boolean running, gotExtraLife, lostLifeThisLevel, fruitOneOnMap, fruitTwoOnMap, deactivateFruitTimer, dead, globalCounterEnabled;

    /**
     * Indicates which motion phase ghosts are in<br>
     * <li> -1: frightened
     * <li> 0: scatter
     * <li> 1: chase
     */
	private int mode;
    
	// basic game data
    private int level, pelletsEaten, score, lives = GameUtilities.STARTING_LIVES, preferredGhost = 1, globalPelletCounter, ticksThisMode, gameStage, frightTicks, pointsPerGhost, fruitTimer, fruitTimerLimit;
    
    // the screen fade to black
    private int fade;
    
    // the highscores
    private List<String> highscore;
    
    // what stage of scoreboard its at
    private int scoreboardDrawMode;
    
    // the name inputted
    private String name;
    
    // the lock used for synchronising drawing and ticking
    private Object objLock;
    
    // the "fruit
    private SpecialPointsConsumable spc;
    
    // what mode the game is in: menu, game, scoreboard
    private int gameMode;
    
    // the list of menu options
    private List<MenuOption> menu;
    
    // the currently selected option
    private int menuChoice;
    
    // the minimap
    private BufferedImage minimap;
    private Graphics miniG;
    
    // the pacman sprite to draw on the minimap
    private int pacCounter;
    
    // 0: normal, 1: recording, 2: demo playback
    private int gameType;
    
    private Demo d;

    private long tickNum;
    
    /**
     * The default constructor for Game. This creates a new game with a player and ghosts. It also handles graphics.
     */
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
		
		initMenu();
		
		minimap = new BufferedImage(112, 112, BufferedImage.TYPE_INT_RGB);
		miniG = minimap.createGraphics();
	}
	
	// initialize the menu with anonymous classes
	private void initMenu() {
		highscore = HighScore.getHighScores();
		
		menu = new ArrayList<Game.MenuOption>();
		MenuOption start = new MenuOption() {
			@Override
			public String name() {
				return "START GAME";
			}
			
			@Override
			public void selected() {
				rand = new Random(System.currentTimeMillis());
				newGame();
			}
		};
		
		MenuOption record = new MenuOption() {
			@Override
			public String name() {
				return "RECORD GAME";
			}
			
			@Override
			public void selected() {
				long seed = System.currentTimeMillis();
				d = new Demo(seed);
				rand = new Random(seed);
				gameType = 1;
				newGame();
			}
		};
		
		MenuOption playback = new MenuOption() {
			@Override
			public String name() {
				return "PLAYBACK GAME";
			}

			@Override
			public void selected() {
				final JFileChooser fc = new JFileChooser(".");
				int choice = fc.showOpenDialog(j);
				if(choice == JFileChooser.APPROVE_OPTION) {
					try{
						j.removeKeyListener(i);
						j.removeMouseListener(i);
						j.removeMouseMotionListener(i);
						gameI = new InputPlayback(p, Game.this, j);
						j.addKeyListener(gameI);
						j.addMouseListener(gameI);
						j.addMouseMotionListener(gameI);
						FileInputStream s = new FileInputStream(fc.getSelectedFile());
						ObjectInputStream o = new ObjectInputStream(s);
						d = (Demo) o.readObject();
						o.close();
						s.close();
						rand = new Random(d.seed);
						gameType = 2;
						newGame();
					}
					catch(IOException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(j, "File not valid demo file");
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						System.err.println("WAT");
						JOptionPane.showMessageDialog(j, "File not valid demo file");
					}
				}
			}
			
		};
		
		MenuOption quit = new MenuOption() {
			@Override
			public String name() {
				return "QUIT";
			}
			
			@Override
			public void selected() {
				System.exit(0);
			}
		};
		
		menu.add(start);
		menu.add(record);
		menu.add(playback);
		menu.add(quit);
	}
	
	/**
	 * Represents an option in the menu
	 * @author Sean
	 *
	 */
	private interface MenuOption {
		String name();
		void selected();
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
		spc = new SpecialPointsConsumable(GameUtilities.FRUIT_LOCATION, 1);
        objects.add(spc);
        tickables.add(spc);
	}
	
	/**
	 * Starts the game
	 */
	@Override
	public void run() {
		initMenu();
		removeCursor();
		
		tickableConsumables();
		
		RenderThread rt = new RenderThread();
		TickThread tt = new TickThread();
		
		// RenderThread and TickThread happen asynchronously to try to keep ticks as close to TICK_RATE as possible
		rt.start();
		tt.start();
	}

	private class RenderThread extends Thread {
		/**
		 * Draws a frame every 1000/FRAME_RATE milliseconds
		 */
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
		/**
		 * Ticks every 1000/TICK_RATE milliseconds
		 */
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
					tickTickables();
					tick_delta -= tickRateMillis;
				}
			}
		}
	}

    @Override
    public void tick() {
    	if(dead) {//if you lost all your lives, fade to black
    		if(fade < 256) { 
    			fade++;
    		} else {
    			if(gameType == 2) {
    				gameMode = 0;
    			} else {
    				gameMode = 2;
    			}
    		}
    		return;
    	}
    	switch(gameType) {
    	case 1:
    		d.recordTick(p);
    		break;
    	case 2:
    		d.replayTick(p, tickNum++);
    		break;
    	}
    	if (gameMode != 1) {//if you are currently not in game, end method
    		return;
    	}
        if (!deactivateFruitTimer) {//if the fruit timer is on, add to it
            fruitTimer++;
        }
        if (fruitTimer >= fruitTimerLimit) {//if it's time for the fruit to disappear, it goes away
            spc.despawn();
            deactivateFruitTimer = true;
        }
        if (lostLifeThisLevel) {//enables and reset the global dot counter if you die
            globalPelletCounter = 0;
            globalCounterEnabled = true;
            lostLifeThisLevel = false;
        }
        if (globalPelletCounter == 32 && ghosts.get(CLYDE).inside()) {//deactivates counter if it reaches 32 and clyde is inside
            globalCounterEnabled = false;
            globalPelletCounter = 0;
        }
        if (preferredGhost < 4 && !ghosts.get(preferredGhost).inside()) {//updates preferred ghost if a ghost leaves
            preferredGhost++;
        }
        if (preferredGhost < 4) {//updates the timer of the preferred ghost
            ghosts.get(preferredGhost).addToTimer();
        }
        if (pelletsEaten == 70 && !fruitOneOnMap) {//spawn first Special Points Consumable at 70 pellets
            spc.updateLevel(level);
            spc.spawn();
            fruitOneOnMap = true;
            fruitTimer = 0;
            deactivateFruitTimer = false;
            fruitTimerLimit = 2400;
        }
        if (pelletsEaten == 170 && !fruitTwoOnMap) {//spawn second Special Points Consumable at 170 pellets
            spc.updateLevel(level);
            spc.spawn();
            fruitTwoOnMap = true;
            fruitTimer = 0;
            deactivateFruitTimer = false;
            fruitTimerLimit = 2400;
        }
        if (pelletsEaten == 240) {//clear level at 240 pellets
            die();
            for(Pellet pellet : m.pelletsList()) {
                pellet.spawn();
            }
            for(Energizer energizer : m.energyList()) {
                energizer.spawn();
            }
            level++;
            pelletsEaten = 0;
            globalPelletCounter = 0;
            ticksThisMode = 0;
            globalCounterEnabled = false;
            lostLifeThisLevel = false;
            spc.updateLevel(level);
        }
        if (score >= 10000 && !gotExtraLife) {//reward you with an extra life at 10000 points
            gotExtraLife = true;
            lives++;
        }
        Point loc = p.getLoc();//update player location
        MapLocation coords = new MapLocation(loc.x, loc.z);//turn it into coordinates
        for (Ghost ghost : ghosts){//complicated process making sure you cannot go around ghosts that checks if a ghost ate you or if you ate a ghost for each ghost
            Point ghostLoc = ghost.getLocation();
            MapLocation ghostCoords = new MapLocation(ghostLoc.x, ghostLoc.z);
            boolean[] open = INTERSECTIONS[coords.mx][coords.my].clone();
            if (!open(open)) {
                Point tempLoc = loc;
                if (!open(INTERSECTIONS[(coords.mx + 1) % INTERSECTIONS.length][coords.my]) && !open(INTERSECTIONS[(((coords.mx - 1) % INTERSECTIONS.length) + INTERSECTIONS.length) % INTERSECTIONS.length][coords.my])) {
                    tempLoc = new Point(tempLoc.x, tempLoc.y, tempLoc.z + (((tempLoc.z % 1) + 1) % 1 > 0.5 ? 1 : -1));
                } else if (!open(INTERSECTIONS[coords.mx][coords.my + 1 % INTERSECTIONS[0].length]) && !open(INTERSECTIONS[coords.mx][(((coords.my - 1) % INTERSECTIONS[0].length) + INTERSECTIONS[0].length) % INTERSECTIONS[0].length])) {
                    tempLoc = new Point(tempLoc.x + (((tempLoc.x % 1) + 1) % 1 > 0.5 ? 1 : -1), tempLoc.y, tempLoc.z);
                }
                coords = new MapLocation(tempLoc);
            }
            if (coords.equals(ghostCoords)) {
                if (ghost.ghostNum != SCARED && ghost.ghostNum != SCARED_FLASHING && ghost.ghostNum != EATEN){
                    lives--;
                    lostLifeThisLevel = true;
                    if(lives >= 0) {
                    	die();
                    }
                } else if (ghost.ghostNum == SCARED || ghost.ghostNum == SCARED_FLASHING){
                    score += pointsPerGhost;
                    pointsPerGhost *= 2;
                    ghost.getAte();
                }
            }
        }
        for (Consumable c : m.consumableList()){//checks if you ate anything consumable for each consumable
            Point consumableLoc = c.getCenter();
            MapLocation consumableCoords = new MapLocation(consumableLoc.x, consumableLoc.z);
            if (coords.equals(consumableCoords) && !c.getEaten()) {
                c.eat(this, p);
            }
        }
        {//checks if you ate a special points consumable
            Point consumableLoc = spc.getCenter();
            MapLocation consumableCoords = new MapLocation(consumableLoc.x, consumableLoc.z);
            if (coords.equals(consumableCoords) && !spc.getEaten()) {
                spc.eat(this, p);
            }
        }
        if (mode != -1) {//updates the amount of time this scatter or chase mode has lasted
            ticksThisMode++;
        } else {//updates the amount of time this fright mode has lasted
            frightTicks++;
        }
        if (frightTicks >= FRIGHTENED_DATA[getArraySafeLevel()][0]) {//if fright mode is over, goes back to the stage the game was on
            mode = gameStage % 2;
        }
        if (gameStage > 6 && mode != -1) {//if all the stages have cycled, chase is activated permanently
            mode = 1;
        } else if (mode != -1 && ticksThisMode == GameUtilities.MODE_TIMES[getArraySafeLevel()][gameStage]) {//if this stage is over, it moves on to the next one
            ticksThisMode = 0;
            gameStage++;
            mode = gameStage % 2;
        }
        if(lives < 0) {//if you lose all your lives, you get a game over
        	endGame();
        }
        pacCounter++;//the amount that the player's mouth is open on the mini map
        if(pacCounter >= 40) {//resets the value if it goes to 40
        	pacCounter = 0;
        }
    }

    /**
     * This method is run if you die. It partly resets the game, some aspects are changed.
     */
    public void die() {
        p.setLoc(new Point(0, 1, -8.5));
        p.setDir(new Vector(-1, 0, 0));
        mode = 0;
        gameStage = 0;
        ticksThisMode = 0;
        preferredGhost = BLINKY;
        for(Ghost ghost : ghosts) {
            ghost.reset();
        }
        fruitTimer = 0;
        fruitOneOnMap = false;
        fruitTwoOnMap = false;
    }
	
    // draws a single frame
	private void drawFrame() {
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
	}

    /**
     * This method runs the tick method of everything in the tickables list.
     */
	private void tickTickables(){
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
	
	/**
	 * This draws the non-3d world part of the game
	 * @param g
	 */
	void drawSpecial(Graphics g) {
		switch(gameMode) {
		case 0:
			drawMenu(g);
			break;
		case 1:
			if(!dead) {
				drawScore(g);
				drawMinimap(g);
			} else {
				drawDead(g);
			}
			break;
		case 2:
			drawDead(g);
			break;
		}
	}
	
	/**
	 * Draw the main menu
	 * @param g
	 */
	private void drawMenu(Graphics g) {
		if(menu == null) {
			return;
		}
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GameUtilities.SC_WIDTH, GameUtilities.SC_HEIGHT);
		g.setFont(GameUtilities.TITLE_FONT);
		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.YELLOW);
		
		String title = "PACMAN";
		g.drawString(title, GameUtilities.SC_WIDTH/2 - fm.stringWidth(title)/2, 100);
		
		g.setFont(GameUtilities.TITLE_OPTION_FONT);
		fm = g.getFontMetrics();
		
		for(int i = 0; i < menu.size(); i++) {
			MenuOption mo = menu.get(i);
			g.setColor(i == menuChoice ? Color.YELLOW : Color.WHITE);
			g.drawString(mo.name(), GameUtilities.SC_WIDTH/2 - fm.stringWidth(mo.name())/2, 250 + i * 75);
		}
		
		if(highscore != null && highscore.get(0) != null) {
			g.setFont(GameUtilities.GAME_OVER_FONT);
			g.setColor(Color.WHITE);
			fm = g.getFontMetrics();
			String highscoreS = highscore.get(0);
			int val = Integer.parseInt(highscoreS.substring(highscoreS.indexOf(':') + 2));
			String display = "Highscore: " + val;
			g.drawString(display, GameUtilities.SC_WIDTH/2 - fm.stringWidth("________"), GameUtilities.SC_HEIGHT - 10);
		}
	}
	
	// draw the score
	private void drawScore(Graphics g) {
		final int height = 50;
		g.setColor(Color.WHITE);
		g.setFont(GameUtilities.SCORE_FONT);
		g.drawString("Score: " + score, 5, GameUtilities.SC_HEIGHT - height);
		g.drawString("Lives: " + lives, 5, GameUtilities.SC_HEIGHT - height + g.getFontMetrics().getHeight());
	}
	
	// draw the minimap
	private void drawMinimap(Graphics g) {
		Point m = p.getLoc();
		miniG.setColor(Color.BLACK);
		miniG.fillRect(0, 0, 112, 112);

		double mx = ((m.x + 42) % 28);
		double my = (-m.z + 18);
		
		int px = (int) (-(mx) * 8) + 56;
		int py = (int) (-(my) * 8) + 56;
		
		miniG.drawImage(GameUtilities.MAP, px, py, null);


		miniG.setColor(Color.YELLOW);
		for(Pellet p : this.m.pelletsList()) {
			if(!p.eaten) {
				MapLocation l = new MapLocation(p.center);
				int x = (int) (l.mx * 8) + 3 + px;
				int y = (int) (l.my * 8) + 3 + py;
				miniG.fillRect(x, y, 2, 2);
			}
		}
		
		miniG.setColor(Color.WHITE);
		for(Energizer e : this.m.energyList()) {
			if(!e.eaten) {
				MapLocation l = new MapLocation(e.center);
				int x = (int) (l.mx * 8) + 2 + px;
				int y = (int) (l.my * 8) + 2 + py;
				miniG.fillRect(x, y, 4, 4);
			}
		}
		
		if(!spc.eaten) {
			miniG.setColor(Color.RED);
			MapLocation l = new MapLocation(spc.center);
			int x = (int) (l.mx * 8) + 2 + px;
			int y = (int) (l.my * 8) + 2 + py;
			miniG.fillRect(x, y, 4, 4);
		}
		
		{
			int dir = (int) (((Math.PI * 3 / 4 - p.getDir().yaw() + Math.PI * 2) % (Math.PI * 2)) / (Math.PI / 2));
			int frameNum = pacCounter / 10;
			if(frameNum == 3) {
				frameNum = 1;
			}
			Point p = this.p.getLoc();
			
			double ix = ((p.x + 41.5) % 28);
			double iy = (-p.z + 17.5);
			
			int x = (int) (ix * 8) - 4 + px;
			int y = (int) (iy * 8) - 4 + py;
			miniG.drawImage(GameUtilities.PAC_SPRITES_ARR[dir][frameNum], x, y, null);
		}
		
		g.drawImage(minimap.getScaledInstance(168, 168, 0), GameUtilities.SC_WIDTH - 180, GameUtilities.SC_HEIGHT - 180, null);
		g.setColor(Color.WHITE);
		g.drawRect(GameUtilities.SC_WIDTH - 180, GameUtilities.SC_HEIGHT - 180, 168, 168);
	}
	
	// draw the end of the game
	private void drawDead(Graphics g) {
		g.setFont(GameUtilities.GAME_OVER_FONT);
		FontMetrics fm = g.getFontMetrics();
		if(fade < 256) {
			g.setColor(new Color(0, 0, 0, fade));
			g.fillRect(0, 0, GameUtilities.SC_WIDTH, GameUtilities.SC_HEIGHT);
		} else {
			g.setColor(new Color(0, 0, 0, 255));
			g.fillRect(0, 0, GameUtilities.SC_WIDTH, GameUtilities.SC_HEIGHT);
			switch(scoreboardDrawMode){
				case 1:
					drawEnterName(g);
					break;
				case 2:
					drawScoreBoard(g);
					break;
			}
		}
		g.setColor(Color.RED);
		g.drawString("Game Over", GameUtilities.SC_WIDTH/2 - fm.stringWidth("Game Over")/2, GameUtilities.SC_HEIGHT - 100);
	}
	 
	// draw entering your name
	private void drawEnterName(Graphics g) {
		g.setFont(GameUtilities.GAME_OVER_FONT);
		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.WHITE);
		String prompt = "Enter Initials:";
		g.drawString(prompt, GameUtilities.SC_WIDTH/2 - fm.stringWidth(prompt)/2, 200);
		for(int i = 0; i < 3; i++) {
			int x = GameUtilities.SC_WIDTH/2 - fm.charWidth('_')/2 + (i - 1) * fm.charWidth('_');
			try{
				g.drawString("" + name.charAt(i), x, 250);
			}
			catch(StringIndexOutOfBoundsException e) {
				g.drawString("_", x, 250);
			}
		}
	}
	
	private void drawScoreBoard(Graphics g) {
		g.setFont(GameUtilities.GAME_OVER_FONT);
		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.WHITE);
		String sc = "Score: " + score;
		g.drawString(sc, GameUtilities.SC_WIDTH/2 - fm.stringWidth(sc)/2, 50);
		if(highscore == null) {
			String high = "Highscores could not be retrieved";
			g.drawString(high, GameUtilities.SC_WIDTH/2 - fm.stringWidth(high)/2, 100);
		} else {
			String high = "Highscores:";
			g.drawString(high, GameUtilities.SC_WIDTH/2 - fm.stringWidth(high)/2, 100);
			for(int i = 0; i < highscore.size(); i++) {
				String h = highscore.get(i);
				g.drawString(h, GameUtilities.SC_WIDTH/2 - fm.stringWidth("___") + fm.charWidth(':')/2, 130 + 30 * i);
			}
		}
	}
	
	// end the game, and do special things for the special games
	private void endGame() {
		dead = true;
		scoreboardDrawMode = 1;
		name = "";
		switch(gameType) {
		case 1:
			final JFileChooser fc = new JFileChooser(".");
			int choice = fc.showSaveDialog(j);
			if(choice == JFileChooser.APPROVE_OPTION) {
				try{
					FileOutputStream fs = new FileOutputStream(fc.getSelectedFile());
					ObjectOutputStream o = new ObjectOutputStream(fs);
					o.writeObject(d);
				}
				catch(IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(j, "Could not write demo file");
				}
			}
			break;
		case 2:
			j.removeKeyListener(gameI);
			j.removeMouseListener(gameI);
			j.removeMouseMotionListener(gameI);
			j.addMouseListener(i);
			j.addMouseMotionListener(i);
			j.addKeyListener(i);
			d = null;
			gameType = 0;
			break;
		}
	}

	/**
	 * React to key input
	 * @param code
	 * @param key
	 */
	void keyPressed(int code, char key) {
		switch(gameMode) {
		case 0:
			if(code == 38) {
				menuChoice = (menuChoice + menu.size() - 1) % menu.size();
			}
			if(code == 40) {
				menuChoice = (menuChoice + 1) % menu.size();
			}
			if(code == 10) {
				menu.get(menuChoice).selected();
			}
			break;
		case 1:
			break;
		case 2: System.out.println(code);
			key = Character.toUpperCase(key);
			if(scoreboardDrawMode == 1) {
				if(key >= 'A' && key <= 'Z') {
					if(name.length() < 3) {
						name += key;
					}
				}
				if(code == 8) {
					if(name.length() >= 1) {
						name = name.substring(0, name.length() - 1);
					}
				}
				if(code == 10) {
					if(name.length() >= 1) {
						while(name.length() < 3) {
							name += " ";
						}
						HighScore.postHighScores(name, score);
						highscore = HighScore.getHighScores();
						scoreboardDrawMode++;
					}
				}
			} else if(scoreboardDrawMode == 2) {
				if(code == 10) {
					newGame();
					gameMode = 0;
				}
			}
		}
	}

	/**
	 * Whether the 3d buffer should be calculated
	 * @return
	 */
	boolean draw3D() {
		return fade < 256 && gameMode == 1;
	}
	
	/**
	 * Add a tickable
	 * @param t
	 */
	public void addTickable(Tickable t){
		tickables.add(t);
	}
	
	/**
	 * Set the list of tickables
	 * @param l
	 */
	public void setTickables(List<Tickable> l){
		tickables = Collections.synchronizedList(l);
	}
	
	/**
	 * Add an object
	 * @param o
	 */
	public void addObject(ThreeDeeObject o) {
		objects.add(o);
	}
	
	/**
	 * Set the list of objects
	 * @param o
	 */
	public void setObjects(List<ThreeDeeObject> o){
		objects = Collections.synchronizedList(o);
	}
	
	/**
	 * Get the list of objects
	 * @return
	 */
	public List<ThreeDeeObject> objects() {
		return objects;
    }

    /**
     * Mode getter.
     * @return Returns the current game mode.
     */
    public int getMode(){
        return mode;
    }

    /**
     * Getter of pellets that were eaten.
     * @return Returns the number of pellets you ate.
     */
    public int getPelletsEaten(){
        return pelletsEaten;
    }

    /**
     * Getter of pellets that are left on the board.
     * @return Returns the total number of pellets on the board minus the number of pellets that were eaten.
     */
    public int getPelletsRemaining(){
        return 240 - getPelletsEaten();
    }

    /**
     * Player getter.
     * @return Returns the player in the game.
     */
    public Player getPlayer(){
        return p;
    }

    /**
     * Getter for the list of ghosts.
     * @return Returns the list of ghosts.
     */
    public List<Ghost> getGhosts(){
        return ghosts;
    }

    /**
     * Automatically moves you to the next level. Called by pressing "=" in developer mode. Only works in developer mode.
     */
    public void rackTest() {
        pelletsEaten = 240;
    }

    /**
     * This method is called when you eat an energizer. It is also called by pressing "`" in developer mode.
     */
    public void startFrightened() {
        mode = -1;
        frightTicks = 0;
        score += 50;
        pointsPerGhost = 200;
        for (Ghost ghost : ghosts) {
            ghost.scare(GameUtilities.FRIGHTENED_DATA[this.getArraySafeLevel()][0]);
        }
    }

    /**
     * This method is called when you eat a pellet. It is also called by pressing "\" in developer mode.
     */
    public void pelletEaten() {
        pelletsEaten++;
        score += 10;
        if (globalCounterEnabled) {
            globalPelletCounter++;
            if (preferredGhost < 4) {
                ghosts.get(preferredGhost).resetTimer();
            }
        } else if (preferredGhost < 4) {
            ghosts.get(preferredGhost).addToCounter();
        }
    }

    /**
     * This method is called when you eat a special points consumable. It rewards you with points.
     */
    public void pointsBonus() {
        score += GameUtilities.GAME_DATA[getArraySafeLevel()][2];
    }

    /**
     * Global pellet counter getter.
     * @return Returns the value of the global pellet counter.
     */
    public int getGlobalPelletCounter() {
        return globalPelletCounter;
    }

    /**
     * Getter for whether or not the global pellet counter is enabled.
     * @return Returns the value of the global pellet counter.
     */
    public boolean getGlobalCounterEnabled() {
        return globalCounterEnabled;
    }

    /**
     * Getter for the number of ticks that have elapsed this mode.
     * @return Returns the number of ticks that have elapsed this mode.
     */
    public int getTicksThisMode() {
        return ticksThisMode;
    }

    /**
     * Getter for the stage that the game is currently in.
     * @return Returns the current game stage.
     */
    public int getGameStage() {
        return gameStage;
    }

    /**
     * Since all levels past level 21 (20 when indexed from 0) are identical, the getter for the level returns 20 for all values of level greater than 20.
     * @return Returns the level that is safe to use in arrays.
     */
    public int getArraySafeLevel() {
        return (Math.min(level, 20));
    }

    /**
     * A method that is called when a new game begins. It resets all the values of variables to their initial state.
     */
    public void newGame() {
    	gameMode = 1;
        die();
        i.recenter();
        p.reset();
        for(Pellet pellet : m.pelletsList()) {
            pellet.spawn();
        }
        for(Energizer energizer : m.energyList()) {
            energizer.spawn();
        }
        for (Ghost ghost : ghosts) {
            ghost.resetCounter();
        }
        lives=GameUtilities.STARTING_LIVES;
        level=0;
        pelletsEaten = 0;
        globalPelletCounter = 0;
        score = 0;
        gameStage = 0;
        mode = 0;
        globalCounterEnabled = false;
        lostLifeThisLevel = false;
        spc.updateLevel(1);
        dead = false;
        highscore = null;
        name = null;
        fade = 0;
        scoreboardDrawMode = 0;
        tickNum = 0;
    }
}
