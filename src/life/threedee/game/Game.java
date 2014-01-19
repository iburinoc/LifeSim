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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import life.threedee.Point;
import life.threedee.ThreeDeeObject;
import life.threedee.Vector;
import life.threedee.game.maps.GameMap;
import life.threedee.game.maps.MapLocation;

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
	
	private boolean running, gotExtraLife = false, lostLifeThisLevel = false, fruitOnMap = false, dead, globalCounterEnabled;

    /**
     * Indicates which motion phase ghosts are in<br>
     * <li> -1: frightened
     * <li> 0: scatter
     * <li> 1: chase
     */
	private int mode;
    
    private int level, pelletsEaten, score, lives = GameUtilities.STARTING_LIVES, preferredGhost = 1, globalPelletCounter = 0, ticksThisMode, gameStage, frightTicks, pointsPerGhost, fruitTimer, fruitTimerLimit;
    
    private int fade;
    
    private List<String> highscore;
    
    private int scoreboardDrawMode;
    
    private String name;
    
    private Object objLock;
    
    private SpecialPointsConsumable spc;
    
    private int gameMode;
    
    private List<MenuOption> menu;
    
    private int menuChoice;
    
    private BufferedImage minimap;
    private Graphics miniG;
    
    private int pacCounter;
    
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
				gameMode++;
				newGame();
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
		menu.add(quit);
	}
	
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
    public void tick() {
    	if(dead) {
    		if(fade < 256) { 
    			fade++;
    		} else {
    			gameMode = 2;
    		}
    		return;
    	}
    	if(gameMode != 1) {
    		return;
    	}
        if (fruitOnMap) {
            fruitTimer++;
        }
        if (fruitTimer >= fruitTimerLimit) {
            spc.despawn();
            fruitOnMap = false;
        }
        if (lostLifeThisLevel) {
            globalPelletCounter = 0;
            globalCounterEnabled = true;
            lostLifeThisLevel = false;
        }
        if (globalPelletCounter == 32 && ghosts.get(CLYDE).inside()) {
            globalCounterEnabled = false;
            globalPelletCounter = 0;
        }
        if (preferredGhost < 4 && !ghosts.get(preferredGhost).inside()) {
            preferredGhost++;
        }
        if (preferredGhost < 4) {
            ghosts.get(preferredGhost).addToTimer();
        }
        if (pelletsEaten == 70 && !fruitOnMap) {
            spc.updateLevel(level);
            spc.spawn();
            fruitOnMap = true;
            fruitTimer = 0;
            fruitTimerLimit = (int) (60 * (9 + Math.random()));
        }
        if (pelletsEaten == 170 && !fruitOnMap) {
            spc.updateLevel(level);
            spc.spawn();
            fruitOnMap = true;
            fruitTimer = 0;
            fruitTimerLimit = (int) (60 * (9 + Math.random()));
        }
        if (pelletsEaten == 240) {
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
        for (Consumable c : m.consumableList()){
            Point consumableLoc = c.getCenter();
            MapLocation consumableCoords = new MapLocation(consumableLoc.x, consumableLoc.z);
            if (coords.equals(consumableCoords) && !c.getEaten()) {
                c.eat(this, p);
            }
        }
        {
            Point consumableLoc = spc.getCenter();
            MapLocation consumableCoords = new MapLocation(consumableLoc.x, consumableLoc.z);
            if (coords.equals(consumableCoords) && !spc.getEaten()) {
                spc.eat(this, p);
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
        } else if (mode != -1 && ticksThisMode == GameUtilities.MODE_TIMES[getArraySafeLevel()][gameStage]) {
            ticksThisMode = 0;
            gameStage++;
            mode = gameStage % 2;
        }
        if(lives < 0) {
        	endGame();
        }
        
        pacCounter++;
        if(pacCounter >= 40) {
        	pacCounter = 0;
        }
    }

    public void die() {
        p.setLoc(new Point(0, 1, -8.5));
        p.setDir(new Vector(-1, 0, 0));
        mode = 0;
        gameStage = 0;
        ticksThisMode = 0;
        preferredGhost = BLINKY;
        for(Ghost ghost : ghosts) {
            ghost.reset();
            ghost.resetCounter();
        }
        fruitTimer = 0;
        fruitOnMap = false;
    }
	
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
	
	private void drawScore(Graphics g) {
		final int height = 50;
		g.setColor(Color.WHITE);
		g.setFont(GameUtilities.SCORE_FONT);
		g.drawString("Score: " + score, 5, GameUtilities.SC_HEIGHT - height);
		g.drawString("Lives: " + lives, 5, GameUtilities.SC_HEIGHT - height + g.getFontMetrics().getHeight());
	}
	
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
	
	private void endGame() {
		dead = true;
		scoreboardDrawMode = 1;
		name = "";
	}

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

	boolean draw3D() {
		return fade < 256 && gameMode == 1;
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

    public void rackTest() {
        pelletsEaten = 240;
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
        if (globalCounterEnabled) {
            globalPelletCounter++;
        } else if (preferredGhost < 4) {
            ghosts.get(preferredGhost).addToCounter();
            ghosts.get(preferredGhost).resetTimer();
        }
    }
    
    public void pointsBonus() {
        score += GameUtilities.GAME_DATA[getArraySafeLevel()][2];
    }

    public int getGlobalPelletCounter() {
        return globalPelletCounter;
    }

    public boolean getGlobalCounterEnabled() {
        return globalCounterEnabled;
    }

    public int getTicksThisMode() {
        return ticksThisMode;
    }

    public int getGameStage() {
        return gameStage;
    }
    
    public int getArraySafeLevel() {
        return (Math.min(level, 20));
    }
    
    public void newGame() {
        die();
        p.reset();
        for(Pellet pellet : m.pelletsList()) {
            pellet.spawn();
        }
        for(Energizer energizer : m.energyList()) {
            energizer.spawn();
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
    }
}
