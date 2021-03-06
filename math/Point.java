package blayzeTechUtils.math;

import blayzeTechUtils.math.NVector;
import blayzeTechUtils.math.StaticPoint;

/**
 * An up-datable point in 2D space.
 * A basic class to use as a basis for anything that has a location in 2D space.
 */

public class Point extends StaticPoint {
	
	public Point(double x, double y)
	{
		super(x,y);
	}
	public Point(double[] values)
	{
		super(values[0], values[1]);
	}
	/**
	 * Constructs a Point using the first two elements of an NVector.
	 * Note that this could be achieved by using `new Point(nVector.toArray())`,
	 * however this is more memory efficient and clips to the first two elements,
	 * allowing larger NVectors to be used.
	 */
	public Point(NVector v)
	{
		super(v);
	}

	public void setX(double x)
	{
		this.x = x;
	}
	public void setY(double y)
	{
		this.y = y;
	}
	public void setXY(StaticPoint p)
	{
		this.x = p.getX();
		this.y = p.getY();
	}

	public String toString()
	{
		return ("[Point] x: "+x+"  y: "+y);
	}
	/** Performs a deep copy of this point. */
	public Point clone()
	{
		return (new Point(x,y));
	}
}
