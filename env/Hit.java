package classes.env;

import classes.math.StaticPoint;
import classes.math.Point;

/**
 * A class to store data created when a hit test of some kind is required to give feedback. Implementations of methods that return this
 * class should use the etiquettes outlined in the constructor method (see constructor)
 */

public class Hit extends StaticPoint {
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

	/**
	 * Allows this hit to be transfromed into another euclidian worlspace via a given PolygonEntity's transformation methods.
	 */
	public void projectToWorldUsing(PolygonEntity e)
	{
		// This hit has no world-based features other than hit coordinates, so only transform them.
		//Point projected = e.projectToWorld((StaticPoint)this);
		Point projected = e.projectToWorld(new Point(this.getX(), this.getY()));// This really shouldn't be a Point. Really every projectToWorld function should take a StaticPoint, TODO.
		this.x = projected.getX();
		this.y = projected.getY();
	}

	public String toString()
	{
		return ("[Hit] Contact?: " + contact + "  x: " + getX() + "   y: " + getY());
	}
}
