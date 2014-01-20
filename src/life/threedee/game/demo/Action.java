package life.threedee.game.demo;

import static life.threedee.game.GameUtilities.readInt;
import static life.threedee.game.GameUtilities.readLong;
import static life.threedee.game.GameUtilities.writeInt;
import static life.threedee.game.GameUtilities.writeLong;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Represents a single input, either a mousemoved, key pressed, or key released
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 *
 */
public final class Action implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 0 = mouse, 1 = key press, 2 = key release
	 */
	public final int type;
	
	/**
	 * ID for the event that has to be replicated
	 */
	public final int id;
	
	/**
	 * When the event happened
	 */
	public final long when;
	
	/**
	 * Field1 (x for mouse, keyCode for keyboard)
	 */
	public final int field1;
	
	/**
	 * Field2 (y for mouse, keyChar for keyboard)
	 */
	public final int field2;
	
	/**
	 * Creates an action from the parameters
	 * @param type
	 * @param id
	 * @param when
	 * @param field1
	 * @param field2
	 */
	public Action(int type, int id, long when, int field1, int field2) {
		this.type = type;
		this.id = id;
		this.when = when;
		this.field1 = field1;
		this.field2 = field2;
	}

	/**
	 * Serializes this action to the input stream
	 * @param s
	 * @return
	 * @throws IOException
	 */
	public static Action deserialize(InputStream s) throws IOException{
		int type = readInt(s);
		int id = readInt(s);
		long when = readLong(s);
		int field1 = readInt(s);
		int field2 = readInt(s);
		return new Action(type, id, when, field1, field2);
	}
	
	/**
	 * Deserializes this action from the input stream
	 * @param s
	 * @throws IOException
	 */
	public void serialize(OutputStream s) throws IOException{
		writeInt(s, type);
		writeInt(s, id);
		writeLong(s, when);
		writeInt(s, field1);
		writeInt(s, field2);
	}
}
