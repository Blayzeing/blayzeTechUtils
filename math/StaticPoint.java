package blayzeTechUtils.math;
import blayzeTechUtils.math.NVector;

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
	public StaticPoint(double[] values)
	{
		this.x = values[0];
		this.y = values[1];
	}
	/**
	 * Constructs a Point using the first two elements of an NVector.
	 * Note that this could be achieved by using `new StaticPoint(nVector.toArray())`,
	 * however this is more memory efficient and clips to the first two elements,
	 * allowing larger NVectors to be used.
	 */
	public StaticPoint(NVector v)
	{
		this(v.getElement(0), v.getElement(1));
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
