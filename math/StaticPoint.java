package classes.math;
import classes.math.NVector;

/**
 * A read-only point in 2D space.
 */
 
 public class StaticPoint {
	protected double x;
	protected double y;
	
	public StaticPoint(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
	}

	public String toString()
	{
		return ("[StaticPoint] x: "+x+"  y: "+y);
	}
	public NVector toNVector()
	{
		return (new NVector(new double[]{x,y}));
	}
	public NMatrix toHozMatrix()
	{
		return (new NMatrix(new double[][]{new double[]{x,y}}));
	}
	public NMatrix toVertMatrix()
	{
		return (new NMatrix(new double[][]{new double[]{x}, new double[]{y}}));
	}
	public StaticPoint clone()
	{
		return (new StaticPoint(x,y));
	}
}
