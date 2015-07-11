package classes.agents;

import classes.agents.AbstractEntity;
import classes.agents.DistancedHit;
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
		double[] intPoint;
		double distance;
		double[] closestIntPoint = null;
		boolean contact = false;
		//Scan left edge
		if(x1 < getX() && x2 > getX())
		{
			contact = true;
			intPoint = MoarMath.lineIntersectNoSkew(getX(), getY(), getX(), getY() + height, x1,y1,x2,y2);
			distance = Math.hypot(x1-intPoint[0], y1-intPoint[1]);
			if(distance < closest)
			{
				closest = distance;
				closestIntPoint = intPoint;
			}
		}
		//Scan up edge
		if(y1 < getY() && y2 > getY())
		{
			contact = true;
			intPoint = MoarMath.lineIntersectNoSkew(getX(), getY(), getX() + width, getY(), x1,y1,x2,y2);
			distance = Math.hypot(x1-intPoint[0], y1-intPoint[1]);
			if(distance < closest)
			{
				closest = distance;
				closestIntPoint = intPoint;
			}		}
		//Scan right edge
		if(x1 > getX() + width && x2 < getX() + width)
		{
			contact = true;
			intPoint = MoarMath.lineIntersectNoSkew(getX() + width, getY(), getX() + width, getY() + height, x1,y1,x2,y2);
			distance = Math.hypot(x1-intPoint[0], y1-intPoint[1]);
			if(distance < closest)
			{
				closest = distance;
				closestIntPoint = intPoint;
			}
		}
		//Scan down edge
		if(y1 > getY() + height && y2 < getY() + height)
		{
			contact = true;
			intPoint = MoarMath.lineIntersectNoSkew(getX(), getY() + height, getX() + width, getY() + height, x1,y1,x2,y2);
			distance = Math.hypot(x1-intPoint[0], y1-intPoint[1]);
			if(distance < closest)
			{
				closest = distance;
				closestIntPoint = intPoint;
			}
		}
		return(new DistancedHit(contact, closestIntPoint[0], closestIntPoint[1], closest));
	}

	public void setWidth(double w)
	{
		width = w;
	}
	public void setHeight(double h)
	{
		height = h;
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

	public static void main(String[] args)
	{
		
	}
}