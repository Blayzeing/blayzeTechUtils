package classes.agents;

import classes.agents.AbstractCircleBoundedEntity;
import classes.math.MoarMath;

public class CircleBoundedEntity extends AbstractCircleBoundedEntity
{
	public CircleBoundedEntity(double x, double y, double radius)
	{
		super(x,y,radius);
	}

	public DistancedHit hitScan(double x1, double y1, double x2, double y2)
	{
		//Using this as reference: http://mathworld.wolfram.com/Circle-LineIntersection.html
		//Subtract the center of this circle, so we can perform the operation as if it was a circle at the origin.
		x1 -= this.getX();
		x2 -= this.getX();
		y1 -= this.getY();
		y2 -= this.getY();
			
		double xdiff = x2 - x1;
		double ydiff = y2 - y1;
		double distance = Math.hypot(xdiff,ydiff);
		
		//Check that the line is not starting within the circle
		if(Math.hypot(x1,y2) <= this.getRadius())
			return (new DistancedHit(true, x1, x2, 0));

		//dx and dy are sensorSlope[0] and [1]
		double distance = Math.hypot(xdiff, ydiff);
		double determinate = x1 * y2 - x2 * y1;
		double discriminate = getRadius()*getRadius()*distance*distance - determinate*determinate;
		if(discriminate < 0) return null; // No intersection
		
		// If not, then there must be an intersection, generate the two points:
		double int1X = (determinate * ydiff + (ydiff<0? -1 : 1) * xdiff * Math.sqrt(discriminate)) / (distance*distance);
		double int1Y = (-determinate * xdiff + Math.abs(ydiff) * Math.sqrt(discriminate)) / (distance*distance);
		double int2X = (determinate * ydiff - (ydiff<0? -1 : 1) * xdiff * Math.sqrt(discriminate)) / (distance*distance);
		double int2Y = (-determinate * xdiff - Math.abs(ydiff) * Math.sqrt(discriminate)) / (distance*distance);
		
		// Check that the sensor being triggered is, in fact, inside the circle and not on the opposite side by checking that the line is within the circle bounds
		// Do this by angle between O to P1 and P1 to P2 (sensorSlope), if >90 then It's toward the circle
		boolean away = MoarMath.angleBetween(x1, y1, xdiff, ydiff) < Math.PI/2;
		if (away && Math.hypot(x1, y1) > getRadius())
			return(new DistancedHit(false, x2,y2, distance));
		
		// Now, with the two inter sectional points found, we must return the one closest to this agent:
		return Math.min(Math.hypot(x1 - int1X, y1 - int1Y), Math.hypot(x1 - int2X, y1 - int2Y));
		double[] intPoint = new double[]{int1X, int1Y};
		double intDistance1 = Math.hypot(x1 - int1X, y1 - int1Y);
		double intDistance2 = Math.hypot(x1 - int2X
	}
}
