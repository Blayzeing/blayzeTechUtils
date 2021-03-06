package blayzeTechUtils.env.nonpolyshapes;

import blayzeTechUtils.env.nonpolyshapes.AbstractBoxBoundedEntity;
import blayzeTechUtils.env.AbstractEntity;
import blayzeTechUtils.env.DistancedHit;
import blayzeTechUtils.math.Point;
import blayzeTechUtils.math.MoarMath;
import blayzeTechUtils.graphics.SimpleDisplay;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class BoxBoundedEntity extends AbstractBoxBoundedEntity{
		
	public BoxBoundedEntity (double x, double y, double width, double height)
	{
		super(x,y,width,height);
	}
	public BoxBoundedEntity (Rectangle bounds)
	{
		this(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
	}

	public DistancedHit hitScan(double x1, double y1, double x2, double y2)
	{
		double closest = Math.hypot(x1-x2, y1-y2);
		//Check that the origin is not inside the bound
		if(x1 > getX() && x1 < getX() + getWidth() && y1 > getY() && y1 < getY() + getHeight())
			return (new DistancedHit(true, x1, y1, 0));
		double[] intPoint;
		double distance;
		double[] closestIntPoint = new double[]{x2,y2};
		boolean contact = false;
		//Scan left edge
		if(x1 < getX() && x2 > getX())
		{
			contact = true;
			intPoint = MoarMath.lineIntersectNoSkew(getX(), getY(), getX(), getY() + getHeight(), x1,y1,x2,y2);
			distance = Math.hypot(x1-intPoint[0], y1-intPoint[1]);
			if(intPoint[1] > getY() && intPoint[1] < getY() + getHeight() && distance < closest)
			{
				closest = distance;
				closestIntPoint = intPoint;
			}
		}
		//Scan up edge
		if(y1 < getY() && y2 > getY())
		{
			contact = true;
			intPoint = MoarMath.lineIntersectNoSkew(getX(), getY(), getX() + getWidth(), getY(), x1,y1,x2,y2);
			distance = Math.hypot(x1-intPoint[0], y1-intPoint[1]);
			if(intPoint[0] > getX() && intPoint[0] < getX() + getWidth() && distance < closest)
			{
				closest = distance;
				closestIntPoint = intPoint;
			}
		}
		//Scan right edge
		if(x1 > getX() + getWidth() && x2 < getX() + getWidth())
		{
			contact = true;
			intPoint = MoarMath.lineIntersectNoSkew(getX() + getWidth(), getY(), getX() + getWidth(), getY() + getHeight(), x1,y1,x2,y2);
			distance = Math.hypot(x1-intPoint[0], y1-intPoint[1]);
			if(intPoint[1] > getY() && intPoint[1] < getY() + getHeight() && distance < closest)
			{
				closest = distance;
				closestIntPoint = intPoint;
			}
		}
		//Scan down edge
		if(y1 > getY() + getHeight() && y2 < getY() + getHeight())
		{
			contact = true;
			intPoint = MoarMath.lineIntersectNoSkew(getX(), getY() + getHeight(), getX() + getWidth(), getY() + getHeight(), x1,y1,x2,y2);
			distance = Math.hypot(x1-intPoint[0], y1-intPoint[1]);
			if(intPoint[0] > getX() && intPoint[0] < getX() + getWidth() && distance < closest)
			{
				closest = distance;
				closestIntPoint = intPoint;
			}
		}
		return(new DistancedHit(contact, closestIntPoint[0], closestIntPoint[1], closest));
	}

	public static void main(String[] args) throws InterruptedException
	{
		BoxBoundedEntity b = new BoxBoundedEntity(130, 90, 100, 100);
		SimpleDisplay d = new SimpleDisplay(550,400, true, true);
		Graphics2D g = d.getGraphics2D();
		b.draw(g);
		d.repaint();
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
			g.drawLine((int)startx, (int)starty, (int)out.getX(), (int)out.getY());
			d.repaint();
		}
	}
}
