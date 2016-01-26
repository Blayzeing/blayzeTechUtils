package classes.env;

/**
 * An entity that has rotation and scale properties, it's *transformable*.
 */

import classes.env.*;
import classes.math.*;
import java.util.ArrayList;

public class TPolygonEntity extends PolygonEntity {
	//Below store the transformation information:
	private double rotation = 0;
	private double xScale = 1;
	private double yScale = 1;
	private SMatrix inverseTransform = SMatrix.identity(2), transform = SMatrix.identity(2);// This stores the actual transform matrcies

	public TPolygonEntity (double x, double y) { super(x,y); }
	public TPolygonEntity (double x, double y, StaticPoint[] p) { super(x,y,p); }
	public TPolygonEntity (double x, double y, ArrayList<StaticPoint> p) { super(x,y,p); }

	public double getRotation() { return rotation; }
	public double getXscale() { return xScale; }
	public double getYscale() { return yScale; }

	public void setRotation (double r)
	{
		rotation = r;
		createTransformMatrices();
	}
	public void setXscale (double x)
	{
		xScale = x;
		createTransformMatrices();
	}
	public void setYscale (double y)
	{
		yScale = y;
		createTransformMatrices();
	}
	public void resetTransformation ()
	{
		yScale = 1;
		xScale = 1;
		rotation = 0;
		createTransformMatrices();
	}
	public void resetRotation () { setRotation(0); }
	public void resetScale () { setXscale(1); setYscale(1); }

	public void createTransformMatrices ()
	{
		SMatrix scale = new SMatrix(2);
		scale.setElements(new double[][]{new double[]{xScale, 0}, new double[]{0, yScale}});
		System.out.println(scale);
		System.out.println(vertices.get(0).toVertMatrix());
	}

	// Note for the below: please check that the contains code for points and static points still works using the new contains code as below
	@Override
	public boolean contains (double x, double y)
	{
		//Use the inverseTransform to transform x and y to a position as if the normal contains would be occuring
		return (contains(x,y));
	}
	// Returns the point in a global format, with transformations applied
	/*@Override
	public Point getGlobalPoint (int i)
	{
		//Apply transformation matrix
		//Apply translation based on x and y
	}*/

	public static void main (String[] args)
	{
		TPolygonEntity p = new TPolygonEntity(0,0, new StaticPoint[]{new StaticPoint(2,3)});
		p.setXscale(20);
	}

}
