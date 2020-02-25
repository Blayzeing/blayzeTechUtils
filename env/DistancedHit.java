package blayzeTechUtils.env;

import blayzeTechUtils.env.Hit;


/**
 * A class that extends the Hit class with a distance variable.
 * This is a class that extends the hit class allowing for storage of a distance from the origin point that the 
 * ray was cast from, this variable is accessible using 'getDistance()'
 * @see	Hit
 */
public class DistancedHit extends Hit {
		
	private double distance = 0;
	
	/**
	 * Class constructor.
	 * @param	madeContact	whether or not the hit made contact with anything
	 * @param	x						the x-coordinate of the point of contact, or if no contact was made, the furthest x-coordinate on the ray
	 * @param 	y						the y-coordinate of the point of contact, or if no contact was made, the furthest y-coordinate on the ray
	 * @param	distance			the distance between the origin of the ray and (x,y). If no contact was made, the ray's longest distance
	 */
	public DistancedHit (boolean madeContact, double x, double y, double distance)
	{
		super(madeContact, x, y);
		this.distance = distance;
	}
	
	public double getDistance()
	{
		return distance;
	}

	// Added to allow for easier manipulation later down the line, conforming with the new method of extedning Hit from Point rather than StaticPoint and allowing it all to be edited
	//public void setDistance(double d)
	//{
	//	distance = d;
	//}

	@Override
	public void projectToWorldUsing(PolygonEntity e)
	{
		super.projectToWorldUsing(e);
		this.distance = e.projectToWorld(this.distance);
	}

	public String toString()
	{
		return ("[DistancedHit] Contact?: " + madeContact() + "  x: " + getX() + "   y: " + getY() + "  d: " + distance);
	}
}
