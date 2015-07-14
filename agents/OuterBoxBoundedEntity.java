package classes.agents;

import classes.agents.AbstractEntity;
import classes.agents.DistancedHit;
import classes.math.MoarMath;
import classes.graphics.SimpleDisplay;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class OuterBoxBoundedEntity extends AbstractEntity{
	
	private double width, height;
	
	public OuterBoxBoundedEntity (double x, double y, double width, double height)
	{
		super(x,y);
		this.width = width;
		this.height = height;
	}
	public OuterBoxBoundedEntity (Rectangle bounds)
	{
		this(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
	}

	public DistancedHit hitScan(double x1, double y1, double x2, double y2)
	{
		double closest = Math.hypot(x1-x2, y1-y2);
		//Check that the origin is not outside the bounds
		if(x1 < getX() || x1 > getX() + width || y1 < getY() || y1 > getY() + height)
			return (new DistancedHit(true, x1, y1, 0));
		double[] intPoint;
		double distance;
		double[] closestIntPoint = new double[]{x2,y2};
		boolean contact = false;
		//Scan left edge
		if(x2 < getX() && x1 > getX())
		{
			contact = true;
			intPoint = MoarMath.lineIntersectNoSkew(getX(), getY(), getX(), getY() + height, x1,y1,x2,y2);
			distance = Math.hypot(x1-intPoint[0], y1-intPoint[1]);
			if(intPoint[1] > getY() && intPoint[1] < getY() + height && distance < closest)
			{
				closest = distance;
				closestIntPoint = intPoint;
			}
		}
		//Scan up edge
		if(y2 < getY() && y1 > getY())
		{
			contact = true;
			intPoint = MoarMath.lineIntersectNoSkew(getX(), getY(), getX() + width, getY(), x1,y1,x2,y2);
			distance = Math.hypot(x1-intPoint[0], y1-intPoint[1]);
			if(intPoint[0] > getX() && intPoint[0] < getX() + width && distance < closest)
			{
				closest = distance;
				closestIntPoint = intPoint;
			}
		}
		//Scan right edge
		if(x2 > getX() + width && x1 < getX() + width)
		{
			contact = true;
			intPoint = MoarMath.lineIntersectNoSkew(getX() + width, getY(), getX() + width, getY() + height, x1,y1,x2,y2);
			distance = Math.hypot(x1-intPoint[0], y1-intPoint[1]);
			if(intPoint[1] > getY() && intPoint[1] < getY() + height && distance < closest)
			{
				closest = distance;
				closestIntPoint = intPoint;
			}
		}
		//Scan down edge
		if(y2 > getY() + height && y1 < getY() + height)
		{
			contact = true;
			intPoint = MoarMath.lineIntersectNoSkew(getX(), getY() + height, getX() + width, getY() + height, x1,y1,x2,y2);
			distance = Math.hypot(x1-intPoint[0], y1-intPoint[1]);
			if(intPoint[0] > getX() && intPoint[0] < getX() + width && distance < closest)
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

	public static void main(String[] args) throws InterruptedException
	{
		OuterBoxBoundedEntity b = new OuterBoxBoundedEntity(130, 90, 300, 200);
		SimpleDisplay d = new SimpleDisplay(550,400, true, true);
		Graphics2D g = d.getGraphics2D();
		g.setColor(Color.RED);
		
		while(true)
		{
			Thread.sleep(10);
			double startx = Math.random() * 550;
			double starty = Math.random() * 400;
			double angle = Math.random() * Math.PI * 2 - Math.PI;
			double endy = starty + Math.sin(angle) * 500;
			double endx = startx + Math.cos(angle) * 500;
			DistancedHit out = b.hitScan(startx, starty, endx, endy);
			if(out.madeContact())
				g.drawLine((int)startx, (int)starty, (int)out.getX(), (int)out.getY());
			else
				g.drawLine((int)startx, (int)starty, (int)endx, (int)endy); d.repaint();
		}
	}
}
