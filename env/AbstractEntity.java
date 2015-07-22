package classes.env;

import classes.math.Point;
import classes.env.Hit;

public abstract class AbstractEntity extends Point{
	
	public AbstractEntity(double x, double y)
	{
		super(x,y);
	}
	
	abstract public Hit hitScan(double x1, double y1, double x2, double y2);
}