package life.threedee.game.demo;

import java.io.Serializable;

public final class Action implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 0 = mouse, 1 = key press, 2 = key release
	 */
	public final int type;
	
	public final int id;
	public final long when;
	
	public final int field1;
	public final int field2;
	
	public Action(int type, int id, long when, int field1, int field2) {
		this.type = type;
		this.id = id;
		this.when = when;
		this.field1 = field1;
		this.field2 = field2;
	}
}
