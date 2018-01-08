package classes.math;

 import classes.math.StaticPoint;

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
