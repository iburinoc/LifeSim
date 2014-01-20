package life.threedee;

/**
 * A camera worker thread. Simply waits for the interrupt to begin drawing, and
 * delegates the actual calculation to methods in the master camera passed to
 * it.
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 */
public final class CameraSlave extends Thread {
	// The master of this worker
	private Camera master;

	// The bounds of the range this worker draws
	private int x1, y1, x2, y2;

	// Whether this worker should keep running or not
	private boolean running;

	// Whether this worker has a job or not
	private boolean job;

	// Whether this worker is done its job or not
	private boolean done;

	/**
	 * Creates a new CameraSlave with the given master and range
	 * 
	 * @param master
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public CameraSlave(Camera master, int x1, int y1, int x2, int y2) {
		super();
		this.master = master;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.running = true;
		this.setDaemon(true);
	}

	/**
	 * Starts the worker's loop
	 */
	@Override
	public void run() {
		while (running) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			if (job) { // if it has a job to do, draw range, and then set it as
						// done
				try{
					master.drawRange(x1, y1, x2, y2);
				}
				catch(NullPointerException e) {
					
				}
				setDone(true);
			}
		}
	}

	/**
	 * The call to start the draw
	 */
	public void draw() {
		setDone(false);
		job = true;
		this.interrupt();
	}

	/**
	 * Returns whether its done or not, synchronized to ensure no multithreaded
	 * weirdness occurs
	 * 
	 * @return
	 */
	public synchronized boolean done() {
		return done;
	}

	/**
	 * Sets whether its done or not, synchronized to ensure no multithreaded
	 * weirdness occurs
	 * 
	 * @param val
	 */
	private synchronized void setDone(boolean val) {
		done = val;
	}
}
