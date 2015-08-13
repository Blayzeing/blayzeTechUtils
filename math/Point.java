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

	public void setX(double x)
	{
		this.x = x;
	}
	public void setY(double y)
	{
		this.y = y;
	}

	public String toString()
	{
		return ("[Point] x: "+x+"  y: "+y);
	}
	public Point clone()
	{
		return (new Point(x,y));
	}
}