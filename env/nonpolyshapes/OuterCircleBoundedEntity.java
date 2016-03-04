package classes.nonpolyshapes.env;

import classes.env.AbstractCircleBoundedEntity;
import classes.math.MoarMath;
import classes.env.DistancedHit;
import classes.graphics.SimpleDisplay;
import java.awt.Color;
import java.awt.Graphics2D;

public class OuterCircleBoundedEntity extends AbstractCircleBoundedEntity {

	public OuterCircleBoundedEntity(double x, double y, double radius)
	{
		super(x,y,radius);
	}

	public DistancedHit hitScan(double x1, double y1, double x2, double y2)
	{
		x1 -= this.getX();
		x2 -= this.getX();
		y1 -= this.getY();
		y2 -= this.getY();
		
		double xdiff = x2 - x1;
		double ydiff = y2 - y1;
		double distance = Math.hypot(xdiff, ydiff);
		double determinate = x1*y2 - x2*y1;
		double discriminate = getRadius() * getRadius() * distance * distance - determinate * determinate;
		
		// Check for non-contacts and outward pointing lines
		double startingPointDistance = Math.hypot(x1, y1);
		if(startingPointDistance > getRadius())// The line starts outside the circle
			return(new DistancedHit(true, x1 + this.getX(), y1+ this.getY(), 0));
		
		// Everything below this point will have two intersects with the circle.
		
		// Calculate two intersect points:
		double int1X = (determinate * ydiff + (ydiff<0? -1 : 1) * xdiff * Math.sqrt(discriminate)) / (distance*distance);
		double int1Y = (-determinate * xdiff + Math.abs(ydiff) * Math.sqrt(discriminate)) / (distance*distance);
		double int2X = (determinate * ydiff - (ydiff<0? -1 : 1) * xdiff * Math.sqrt(discriminate)) / (distance*distance);
		double int2Y = (-determinate * xdiff - Math.abs(ydiff) * Math.sqrt(discriminate)) / (distance*distance);
		
		//Choose the correct intersect point by choosing the one whos angle between Center of circle to intersection and point1 to point2 is least
		//  '--> Check that each reading is not more than the distance between the points.
		double int1distance = Math.hypot(int1X - x1, int1Y - y1);
		double int2distance = Math.hypot(int2X - x1, int2Y - y1);
		if(MoarMath.angleBetween(int1X,int1Y,x2-x1,y2-y1) < MoarMath.angleBetween(int2X, int2Y,x2-x1,y2-y1) )
		{
			if(int1distance < distance)
				return (new DistancedHit(true, int1X + getX(), int1Y + getY(), int1distance));
			else
				return (new DistancedHit(false, x2 + this.getX(), y2 + this.getY(), distance));
		}else{
			if(int2distance < distance)
				return (new DistancedHit(true, int2X + getX(), int2Y + getY(), int2distance));
			else
				return (new DistancedHit(false, x2 + this.getX(), y2 + this.getY(), distance));
		}
	}

	public static void main(String[] args) throws InterruptedException
	{
		OuterCircleBoundedEntity c = new OuterCircleBoundedEntity(250,200,150);
		SimpleDisplay d = new SimpleDisplay(550, 400, true, true);
		Graphics2D g = d.getGraphics2D();
		c.draw(g);
		d.repaint();
		g.setColor(Color.RED);

		while(true)
		{
			Thread.sleep(10);
			double startx = Math.random() * 550;
			double starty = Math.random() * 400;
			double angle = Math.random() * Math.PI * 2 - Math.PI;
			double endy = starty + Math.sin(angle) * 70;
			double endx = startx + Math.cos(angle) * 70;
			DistancedHit out = c.hitScan(startx, starty, endx, endy);
			g.drawLine((int)startx, (int)starty, (int)out.getX(), (int)out.getY());
			d.repaint();
		}
	}
}
