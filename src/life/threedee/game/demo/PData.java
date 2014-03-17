package life.threedee.game.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import life.threedee.Point;
import life.threedee.Vector;

public class PData implements Serializable{
	private static final long serialVersionUID = 1L;
	public final Vector dir;
	public final Point loc;

	public PData(Vector dir, Point loc) {
		super();
		this.dir = dir;
		this.loc = loc;
	}

	public void serialize(OutputStream o) throws IOException {
		dir.serialize(o);
		loc.serialize(o);
	}

	public static PData deserialize(InputStream i) throws IOException {
		return new PData(Vector.deserialize(i), Point.deserialize(i));
	}
}