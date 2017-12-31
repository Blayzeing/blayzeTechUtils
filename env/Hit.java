package classes.env;

import classes.math.Point;

/**
 * A class to store data created when a hit test of some kind is required to give feedback. Implementations of methods that return this
 * class should use the etiquettes outlined in the constructor method (see constructor)
 */

public class Hit extends Point /*StaticPoint*/{
	private boolean contact = false;
	
	/**
	 * Class constructor.
	 * @param	madeContact	whether or not the hit made contact with anything
	 * @param	x		the x-coordinate of the point of contact, or if no contact was made, the furthest x-coordinate on the ray
	 * @param 	y		the y-coordinate of the point of contact, or if no contact was made, the furthest y-coordinate on the ray
	 */
	public Hit(boolean madeContact, double x, double y)
	{
		super(x,y);
		contact = madeContact;
	}

	public boolean madeContact()
	{
		return contact;
	}

	public String toString()
	{
		return ("[Hit] Contact?: " + contact + "  x: " + getX() + "   y: " + getY());
	}
}
