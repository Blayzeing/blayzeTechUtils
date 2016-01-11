package classes.env;

import classes.math.Point;
import classes.env.Hit;
import java.awt.Graphics2D;

public abstract class AbstractEntity extends Point{
	
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
	public DistancedHit hitScan(Point p1, Point p2)
	{
		return(hitScan(p1.getX(), p1.getY(),p2.getX(),p2.getY()));
	}
	public abstract void draw(Graphics2D g);
	public abstract double getWidth();
	public abstract double getHeight();
	public abstract Point getTopLeftCorner();
	public abstract Point getTopRightCorner();
	public abstract Point getBottomRightCorner();
	public abstract Point getBottomLeftCorner();

	/*public boolean equals(Object obj)
	{
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		
		return true;
	}*/
	public String toString()
	{
		return ("[Abstract Entity]\n'-> "+super.toString());
	}
}
