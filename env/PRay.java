package classes.env;

/**
 * A *parentable* Ray, it can have a PolygonEntity parent assigned to it.
 */

import classes.env.*;
import classes.math.*;
import classes.graphics.*;
import java.awt.Color;
import java.awt.Graphics2D;

public class PRay extends Ray {

	private PolygonEntity parent = null;

	public PRay (Point start, Point end, PolygonEntity parent)
	{
		super(start, end);
		this.parent = parent;
	}
	public PRay (Point start, Point end)
	{
		super(start, end);
	}
	public PRay ()
	{
		super();
	}

	public void setParent(PolygonEntity p)
	{
		parent = p;
	}

	@Override
	/**
	 * Performs a hitScan on the given HitScannable object.
	 * NOTE: This returns WORLD coordinates
	 */
	public DistancedHit performHitScanOn(HitScannable e)
	{
		if(parent == null)
			return super.performHitScanOn(e);
		else{
			Point[] projected = parent.projectToWorld(new Point[]{start, end});
			lastHit = e.hitScan(projected[0],projected[1]);
			return lastHit;
		}
	}
	// TODO here: performLocalHitScan

	@Override
	public void draw(Graphics2D g)
	{
		Point[] projected = parent.projectToWorld(new Point[]{start, end});
		Color bc = g.getColor();
		if((lastHit != null) && lastHit.madeContact())
		{
			g.setColor(Color.RED);
			g.drawLine((int)projected[0].getX(), (int)projected[0].getY(), (int)lastHit.getX(), (int)lastHit.getY());
			g.setColor(Color.GRAY);
			g.drawLine((int)lastHit.getX(), (int)lastHit.getY(), (int)projected[1].getX(), (int)projected[1].getY());
		}else{
			g.setColor(Color.RED);
			g.drawLine((int)projected[0].getX(), (int)projected[0].getY(), (int)projected[1].getX(), (int)projected[1].getY());
		}
		g.setColor(bc);
	}

	/**
	 * Returns an array of PRays arcing evenly between two angles.
	 */
	public static PRay[] makeSensorArray(int rayCount, Point center, double rayDistance, double startAngle, double endAngle, PolygonEntity parent)
	{
		PRay[] output = new PRay[Math.max(rayCount,0)];
		if(rayCount>1)
		{
			double stepAmount = (endAngle-startAngle)/(rayCount-1);
			for(int i = 0; i<rayCount; i++)
			{
				double currentAngle = startAngle + stepAmount*i;
				output[i] = new PRay(center, new Point(center.getX() + Math.cos(currentAngle)*rayDistance, center.getY() + Math.sin(currentAngle)*rayDistance), parent);
			}
		}else if(rayCount == 1){
			double midAngle = (startAngle + endAngle) * 0.5;
			output[0] = new PRay(center, new Point(center.getX() + Math.cos(midAngle)*rayDistance, center.getY() + Math.sin(midAngle)*rayDistance), parent);
		}
		return output;
	}
	public static PRay[] makeSensorArray(int rayCount, Point center, double rayDistance, double startAngle, double endAngle)
	{
		return makeSensorArray(rayCount, center, rayDistance, startAngle, endAngle, null);
	}

	public static void main(String[] args) throws InterruptedException
	{
		SimpleDisplay d = new SimpleDisplay(800,400,"Parented Ray Casting",true,true);
		Graphics2D g = d.getGraphics2D();
		StaticPoint[] shape = new StaticPoint[]{new StaticPoint(35,0), new StaticPoint(0,9), new StaticPoint(0,-9)};
		TPPolygonEntity[] bones = new TPPolygonEntity[10];
		for(int i = 0; i<bones.length; i++)
		{
			bones[i] = new TPPolygonEntity(shape[0].getX(),0,shape);
			if(i-1>=0)
				bones[i].setParent(bones[i-1]);
			bones[i].setXscale(0.9);
			bones[i].setYscale(0.9);
		}
		bones[0].setX(400);
		bones[0].setY(400);
		bones[0].setRotation(-Math.PI/2);
		Environment env = new Environment(bones);
		PRay[] rays = PRay.makeSensorArray(3, new Point(36,0), 364, -Math.PI/6, Math.PI/6, bones[bones.length-1]);

		// Loop
		g.setColor(Color.BLACK);
		int flip = 1;
		while(true)
		{
			//Moving
			for(int i = 1; i<bones.length; i++)
				bones[i].setRotation(Math.min(Math.max(-Math.PI/3,  bones[i].getRotation()+flip*Math.PI/500  ),Math.PI/3));
			if(bones[1].getRotation() == Math.PI/3 || bones[1].getRotation() == -Math.PI/3)
				flip *= -1;
			for(PRay r:rays)
				r.performHitScanOn(env);
			//Drawing
			System.out.println(rays[0].getLastHit().getDistance());
			d.fill(Color.WHITE);
			env.draw(g);
			for(PRay r:rays)
				r.draw(g);
			d.repaint();
			Thread.sleep(50);
		}
	}	
}
