package classes.agents;

import classes.agents.AbstractEntity;
import classes.agents.Hit;
import classes.math.MoarMath;

public class BoxBoundedEntity extends AbstractEntity{
	
	private double width, height;
	
	public BoxBoundedEntity (double x, double y, double width, double height)
	{
		super(x,y);
		this.width = width;
		this.height = height;
	}
	public BoundedEntity (Rectangle bounds)
	{
		this(bounds.x, bounds.y, bounds.width, bounds.height);
	}
	
	public Hit hitScan(double x1, double y1, double x2, double y2)
	{
		double closest = Math.hypot(x1-x2, y1-y2);
		//Check that the origin is not inside the bound
		if(x1 > getX() && x1 < getX() + width && y1 > getY() && y2 < getY() + height)
			return 0;
		//Scan left edge
		if(x1 < getX() && x2 > getX())
		{
			
		}
		//Scan up edge
		if(y1 < getY() && y2 > getY())
		{
			
		}
		//Scan right edge
		if(x1 > getX() + width && x2 < getX() + width)
		{
			
		}
		//Scan down edge
		if(y1 > getY() + height && y2 < getY() + height)
		{
			
		}
		return closest;
	}
	
	public double getWidth()
	{
		return width;
	}
	public double getHeight()
	{
		return height;
	}
	public Point getTopLeftCorner()
	{
		return (new Point(getX(), getY()));
	}
	public Point getTopRightCorner()
	{
		return (new Point(getX() + width, getY());
	}
	public Point getBottomRightCorner()
	{
		return (new Point(getX() + width, getY() + height));
	}
	public Point getBottomLeftCorner()
	{
		return (new Point(getX(), getY() + height));
	}
}