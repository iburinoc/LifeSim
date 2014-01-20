package life.threedee.game.demo;

import static life.threedee.game.GameUtilities.readInt;
import static life.threedee.game.GameUtilities.readLong;
import static life.threedee.game.GameUtilities.writeInt;
import static life.threedee.game.GameUtilities.writeLong;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

	public static Action deserialize(InputStream s) throws IOException{
		int type = readInt(s);
		int id = readInt(s);
		long when = readLong(s);
		int field1 = readInt(s);
		int field2 = readInt(s);
		return new Action(type, id, when, field1, field2);
	}
	
	public void serialize(OutputStream s) throws IOException{
		writeInt(s, type);
		writeInt(s, id);
		writeLong(s, when);
		writeInt(s, field1);
		writeInt(s, field2);
	}
}
