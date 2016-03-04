package classes.nonpolyshapes.env;

import classes.env.AbstractEntity;
import classes.env.DistancedHit;
import classes.math.Point;
import classes.math.MoarMath;
import classes.graphics.SimpleDisplay;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A mid-way class to hold all methods used by box-based entities with width and heights.
 */

public abstract class AbstractBoxBoundedEntity extends AbstractEntity
{
	private double width, height;

	public AbstractBoxBoundedEntity (double x, double y, double width, double height)
	{
		super(x,y);
		this.width = width;
		this.height = height;
	}

	public void setWidth(double w)
	{
		width = w;
	}
	public void setHeight(double h)
	{
		height = h;
	}

	public abstract DistancedHit hitScan(double x1, double y1, double x2, double y2);

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
		return (new Point(getX() + width, getY()));
	}
	public Point getBottomRightCorner()
	{
		return (new Point(getX() + width, getY() + height));
	}
	public Point getBottomLeftCorner()
	{
		return (new Point(getX(), getY() + height));
	}

	public String toString()
	{
		return ("[Abstract Box Bounded Entity] width: " + width + "  height: " + height + "\n'-> " + super.toString());
	}

	public void draw(Graphics2D g)
	{
		g.drawRect((int)this.getX() , (int)this.getY(), (int)width, (int)height);
	}
}
