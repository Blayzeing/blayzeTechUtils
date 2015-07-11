package classes.agents;

import classes.agents.Hit;


/**
 * This is a class that extends the hit class allowing for storage of a distance from the origin point that the 
 * ray was cast from, this variable is accesible using 'getDistance()'
 */
public class DistancedHit extends Hit {
		
	private double distance = 0;
	
	public DistancedHit (boolean madeContact, double x, double y, double distance)
	{
		super(madeContact, x, y);
		this.distance = distance;
	}
	
	public double getDistance()
	{
		return distance;
	}
}
