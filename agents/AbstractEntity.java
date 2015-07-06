package classes.agents;

import classes.agents.Point;
import classes.agents.Hit;

public abstract class AbstractEntity extends Point{
	
	public AbstractEntity(double x, double y)
	{
		super(x,y);
	}
	
	abstract public Hit hitScan(double x1, double y1, double x2, double y2);
}