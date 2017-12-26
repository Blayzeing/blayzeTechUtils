package classes.env;

/**
 * A class to store ray casting information for hitScans.
 * TODO:
 * Implement hitScan methods that explicitly take Environments and run with exclusion objects and lists, also implement exclusion lists in Environment.
 */

import classes.math.Point;
import classes.env.AbstractEntity;
import classes.env.DistancedHit;
import classes.graphics.Drawable;
import java.awt.Color;
import java.awt.Graphics2D;

public class Ray implements Drawable {

	public Point start, end;
	protected DistancedHit lastHit = null;

	public Ray (Point start, Point end)
	{
		this.start = start;
		this.end = end;
	}
	public Ray ()
	{
		this(null,null);
	}

	public DistancedHit getLastHit()
	{
		return lastHit;
	}

	public DistancedHit performHitScanOn(HitScannable e)
	{
		lastHit = e.hitScan(start.getX(), start.getY(), end.getX(), end.getY());
		return lastHit;
	}

	public void draw(Graphics2D g)
	{
		Color bc = g.getColor();
		if((lastHit != null) && lastHit.madeContact())
		{
			g.setColor(Color.RED);
			g.drawLine((int)start.getX(), (int)start.getY(), (int)lastHit.getX(), (int)lastHit.getY());
			g.setColor(Color.GRAY);
			g.drawLine((int)lastHit.getX(), (int)lastHit.getY(), (int)end.getX(), (int)end.getY());
		}else{
			g.setColor(Color.RED);
			g.drawLine((int)start.getX(), (int)start.getY(), (int)end.getX(), (int)end.getY());
		}
		g.setColor(bc);
	}
}
