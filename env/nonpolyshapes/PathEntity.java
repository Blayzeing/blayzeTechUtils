package blayzeTechUtils.nonpolyshapes.env;

/**
 * An entity that consists of a (potentially lopped) path.
 *
 * TODO: WORK IN PROGRESS
 */

import java.util.ArrayList;
import blayzeTechUtils.env.*;
import blayzeTechUtils.math.*;
import java.awt.Graphics2D;

public class PathEntity extends AbstractEntity {

	public double strokeRadius = 10.0;
	public ArrayList<Point> vertices = new ArrayList<Point>();
	public boolean invert = false;

	public PathEntity(double x, double y)
	{
		super(x,y);
	}

	public void draw(Graphics2D g)
	{
		
	}
	public DistancedHit hitScan(double x1, double y1, double x2, double y2) { return null; }

}
