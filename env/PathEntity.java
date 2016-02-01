package classes.env;

/**
 * An entity that consists of a (potentially lopped) path.
 */

import java.util.ArrayList;

public class PathEntity extends AbstractEntity {

	public double strokeRadius = 10.0;
	public ArrayList<Point> vertices = new ArrayList<Point>();
	public boolean invert = false;

}
