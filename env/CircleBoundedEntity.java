package classes.env;

import classes.env.AbstractCircleBoundedEntity;
import classes.math.MoarMath;
import classes.env.DistancedHit;
import classes.graphics.SimpleDisplay;
import java.awt.Color;
import java.awt.Graphics2D;

public class CircleBoundedEntity extends AbstractCircleBoundedEntity {

	public CircleBoundedEntity(double x, double y, double radius)
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
		if(startingPointDistance <= getRadius())// The line starts within the circle it is hit testing.
			return(new DistancedHit(true, x1 + this.getX(), y1+ this.getY(), 0));
		if(discriminate < 0) // The line does not make contact with the circle
			return (new DistancedHit(false, x2 + this.getX(), y2 + this.getY(), distance));
		if(MoarMath.angleBetween(x1,y1,x2-x1,y2-y1) < Math.PI/2 && startingPointDistance > getRadius())// The line is pointing away from the circle and starts outside of it.
			return(new DistancedHit(false, x2 + this.getX(), y2 + this.getY(), distance));

		// If not, then there must be an intersection, generate the two points:
		double int1X = (determinate * ydiff + (ydiff<0? -1 : 1) * xdiff * Math.sqrt(discriminate)) / (distance*distance);
		double int1Y = (-determinate * xdiff + Math.abs(ydiff) * Math.sqrt(discriminate)) / (distance*distance);
		double int2X = (determinate * ydiff - (ydiff<0? -1 : 1) * xdiff * Math.sqrt(discriminate)) / (distance*distance);
		double int2Y = (-determinate * xdiff - Math.abs(ydiff) * Math.sqrt(discriminate)) / (distance*distance);
		
		// Get the closest of the two points:
		double int1distance = Math.hypot(int1X - x1, int1Y - y1);
		double int2distance = Math.hypot(int2X - x1, int2Y - y1);
		if( int1distance <= int2distance && int1distance <= distance)
			return (new DistancedHit(true, int1X + getX(), int1Y + getY(), int1distance));
		else if(int2distance <= distance)
			return (new DistancedHit(true, int2X + getX(), int2Y + getY(), int2distance));
		else
			return (new DistancedHit(false, x2 + this.getX(), y2 + this.getY(), distance));// If both the intersections were too far away, return the original line.
	}

	public static void main(String[] args) throws InterruptedException
	{
		CircleBoundedEntity c = new CircleBoundedEntity(250,200,50);
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
			double angle = /*Math.random() * Math.PI/16 + Math.PI/2 - Math.PI/32;*/Math.random() * Math.PI * 2 - Math.PI;
			double endy = starty + Math.sin(angle) * 500;
			double endx = startx + Math.cos(angle) * 500;
			DistancedHit out = c.hitScan(startx, starty, endx, endy);
			//System.out.println(out);
			if(out.madeContact())
				g.drawLine((int)startx, (int)starty, (int)out.getX(), (int)out.getY());
			else
				g.drawLine((int)startx, (int)starty, (int)endx, (int)endy); d.repaint();
		}
	}
}
