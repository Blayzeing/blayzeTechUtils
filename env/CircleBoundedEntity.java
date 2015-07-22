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
		double descriminate = getRadius() * getRadius() * distance * distance - determinate * determinate;
		
		// Check for non-contacts and outward pointing lines
		if(discriminate < 0) // The line does not make contact with the circle
			return null; // TODO: Return whole line with no contact
		if(MoarMath.angleBetween(x1,y1,x2-x1,y2-y1) < Math.PI/2 && Math.hypot(x1,y1) > getRadius())// The line is pointing away from the circle 
			return(new DistancedHit(false, ));

		// If not, then there must be an intersection, generate the two points:
		double int1X = (determinate * ydiff + (ydiff<0? -1 : 1) * xdiff * Math.sqrt(discriminate)) / (distance*distance);
		double int1Y = (-determinate * xdiff + Math.abs(ydiff) * Math.sqrt(discriminate)) / (distance*distance);
		double int2X = (determinate * ydiff - (ydiff<0? -1 : 1) * xdiff * Math.sqrt(discriminate)) / (distance*distance);
		double int2Y = (-determinate * xdiff - Math.abs(ydiff) * Math.sqrt(discriminate)) / (distance*distance);
		
		
		/*
		//Check that the line is not starting within the circle
		if(Math.hypot(x1 - this.getX(), y1 - this.getY()) <= this.getRadius())
			return (new DistancedHit(true, x1, y1, 0));

		double xdiff = x2 - x1;
		double ydiff = y2 - y1;
		double distance = Math.hypot(xdiff, ydiff);
			
		//Using this as reference: http://mathworld.wolfram.com/Circle-LineIntersection.html
		//Subtract the center of this circle, so we can perform the operation as if it was a circle at the origin.
		x1 -= this.getX();
		x2 -= this.getX();
		y1 -= this.getY();
		y2 -= this.getY();
		
		//dx and dy are sensorSlope[0] and [1]
		double determinate = x1 * y2 - x2 * y1;
		double discriminate = getRadius()*getRadius()*distance*distance - determinate*determinate;
		if(discriminate < 0) return(new DistancedHit(false, x2,y2, distance)); // No intersection
		
		// If not, then there must be an intersection, generate the two points:
		double int1X = (determinate * ydiff + (ydiff<0? -1 : 1) * xdiff * Math.sqrt(discriminate)) / (distance*distance);
		double int1Y = (-determinate * xdiff + Math.abs(ydiff) * Math.sqrt(discriminate)) / (distance*distance);
		double int2X = (determinate * ydiff - (ydiff<0? -1 : 1) * xdiff * Math.sqrt(discriminate)) / (distance*distance);
		double int2Y = (-determinate * xdiff - Math.abs(ydiff) * Math.sqrt(discriminate)) / (distance*distance);
		
		int1X += this.getX();
		int2X += this.getX();
		int1Y += this.getY();
		int2Y += this.getY();

		// Now, with the two inter sectional points found, we must return the one closest to this agent:
		double[] intPoint = new double[]{int1X, int1Y};
		double intDistance1 = Math.hypot(x1 - int1X, y1 - int1Y);
		double intDistance2 = Math.hypot(x1 - int2X, y1 - int2Y);

		// Make sure the line isn't outside of the circle, first check that the line ends after the intersection,
		// Second check that the line is pointing toward the circle instead of away by finding the angle between
		// origin to P1 and P1 to P2, if >90 then it's toward the circle
		if(intDistance1 > distance && intDistance2 > distance)
		{
			return (new DistancedHit(false, x2, y2, distance));
		}else{
			if (intDistance1 < intDistance2)
				if(MoarMath.angleBetween(xdiff, ydiff, int1X - getX(), int1Y - getY()) < Math.PI/2)
					return (new DistancedHit(true, int1X, int1Y, intDistance1));
				else
					return (new DistancedHit(false, x2, y2, distance));

			else
				if(MoarMath.angleBetween(xdiff, ydiff, int2X - getX(), int2Y - getY()) < Math.PI/2)
					return (new DistancedHit(true, int2X, int2Y, intDistance2));
				else
					return (new DistancedHit(false, x2, y2, distance));

		}
		*/
	}

	public static void main(String[] args) throws InterruptedException
	{
		CircleBoundedEntity c = new CircleBoundedEntity(250,200,50);
		SimpleDisplay d = new SimpleDisplay(550, 400, true, true);
		Graphics2D g = d.getGraphics2D();
		g.setColor(Color.RED);

		while(true)
		{
			Thread.sleep(10);
			double startx = 100;//Math.random() * 550;
			double starty = 100;//Math.random() * 400;
			double angle = Math.random() * Math.PI * 2 - Math.PI;
			double endy = starty + Math.sin(angle) * 500;
			double endx = startx + Math.cos(angle) * 500;
			DistancedHit out = c.hitScan(startx, starty, endx, endy);
			System.out.println(out);
			if(out.madeContact())
				g.drawLine((int)startx, (int)starty, (int)out.getX(), (int)out.getY());
			else
				g.drawLine((int)startx, (int)starty, (int)endx, (int)endy); d.repaint();
		}
	}
}
