package classes.agents;

import classes.agents.AbstractEntity;
import classes.agents.DistancedHit;
import classes.math.MoarMath;
import classes.graphics.SimpleDisplay;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A mid-way class to hold all methods used by circle-based entities with a radius
 */

public abstract class AbstractCircleBoundedEntity extends AbstractEntity
{
	private double radius;
	
	public AbstractCircleBoundedEntity (double x, double y, double r)
	{
		super(x,y);
		radius = r;
	}
	
	public abstract DistancedHit hitScan(double x1, double y1, double x2, double y2);

	public void setRadius(double r)
	{
		radius = Math.abs(r);
	}
	public double getRadius()
	{
		return radius;
	}
	
	public static void main(String[] args)
	{
		System.out.println("Test...");
	}
}
