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
		if(lastHit.madeContact())
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

	public static void main(String[] args) throws InterruptedException
	{
		SimpleDisplay d = new SimpleDisplay(800,400,"Parented Ray Casting",true,true);
		Graphics2D g = d.getGraphics2D();
		StaticPoint[] shape = new StaticPoint[]{new StaticPoint(35,0), new StaticPoint(0,9), new StaticPoint(0,-9)};
		TPPolygonEntity[] bones = new TPPolygonEntity[30];
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
		PRay ray1 = new PRay(new Point(36,0), new Point(400,0), bones[bones.length-1]);
		PRay ray2 = new PRay(new Point(36,0), new Point(400,-80), bones[bones.length-1]);
		PRay ray3 = new PRay(new Point(36,0), new Point(400,80), bones[bones.length-1]);

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
			ray1.performHitScanOn(env);
			ray2.performHitScanOn(env);
			ray3.performHitScanOn(env);
			//Drawing
			System.out.println(ray1.getLastHit().getDistance());
			d.fill(Color.WHITE);
			env.draw(g);
			ray1.draw(g);
			ray2.draw(g);
			ray3.draw(g);
			d.repaint();
			Thread.sleep(50);
		}
	}	
}
