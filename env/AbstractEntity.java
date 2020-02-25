package blayzeTechUtils.env;

import blayzeTechUtils.math.Point;
import blayzeTechUtils.math.StaticPoint;
import blayzeTechUtils.env.Hit;
import blayzeTechUtils.graphics.Drawable;
import java.awt.Graphics2D;
import java.util.ArrayList;

public abstract class AbstractEntity extends Point implements Drawable, HitScannable {
	
	public AbstractEntity(double x, double y)
	{
		super(x,y);
	}
	
	/**
	 * Performs a hitScan.
	 * Works out how the Entity interacts with the line between [(x1, y1), (x2, y2)]
	 * @return	DistancedHit	true if collision made, x and y values are the collision position (if no collision made, x2 and y2 returned)
	 */
	public abstract DistancedHit hitScan(double x1, double y1, double x2, double y2);
	public DistancedHit hitScan(StaticPoint p1, StaticPoint p2)
	{
		return(hitScan(p1.getX(), p1.getY(),p2.getX(),p2.getY()));
	}
	/** Calculates if a given point is inside the entity or not.
	 * Returns false as default for entities that do not have collision meshes (and because I was being lazy with the nonpoly shapes)
	 * @return	boolean		true if the point lies within this entity; False by default if no method has overwritten this
	 */
	public boolean contains(double x, double y)
	{
		return false;
	}
	public boolean contains(StaticPoint p)
	{
		return contains(p.getX(), p.getY());
	}
	public boolean contains(StaticPoint[] ps)
	{
		for(StaticPoint p : ps)
			if(contains(p))
				return true;
		return false;
	}
	public boolean contains(ArrayList<StaticPoint> ps)
	{
		for(StaticPoint p : ps)
			if(contains(p))
				return true;
		return false;
	}
	public abstract void draw(Graphics2D g);
	//public abstract double getWidth();
	//public abstract double getHeight();
	//public abstract Point getTopLeftCorner();
	//public abstract Point getTopRightCorner();
	//public abstract Point getBottomRightCorner();
	//public abstract Point getBottomLeftCorner();

	public String toString()
	{
		return ("[Abstract Entity]\n'-> "+super.toString());
	}
}
