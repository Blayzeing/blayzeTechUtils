package classes.env;

/**
 * An entity that has rotation and scale properties, it's *transformable*.
 */

import classes.env.*;
import classes.math.*;
import java.util.ArrayList;
import classes.graphics.SimpleDisplay;
import java.awt.Color;
import java.awt.Graphics2D;

public class TPolygonEntity extends PolygonEntity {
	//Below store the transformation information:
	private double rotation = 0;
	private double xScale = 1;
	private double yScale = 1;
	private SMatrix transform = SMatrix.identity(2), transformInverse = SMatrix.identity(2);// This stores the actual transform matrix.

	public TPolygonEntity (double x, double y) { super(x,y); }
	public TPolygonEntity (double x, double y, StaticPoint[] p) { super(x,y,p); }
	public TPolygonEntity (double x, double y, ArrayList<StaticPoint> p) { super(x,y,p); }

	public double getRotation() { return rotation; }
	public double getXscale() { return xScale; }
	public double getYscale() { return yScale; }

	public void setRotation (double r)
	{
		rotation = r;
		createTransformMatrices();
	}
	public void setXscale (double x)
	{
		xScale = x;
		createTransformMatrices();
	}
	public void setYscale (double y)
	{
		yScale = y;
		createTransformMatrices();
	}
	public void resetTransformation ()
	{
		yScale = 1;
		xScale = 1;
		rotation = 0;
		transform = SMatrix.identity(2);
	}
	public void resetRotation () { setRotation(0); }
	public void resetScale () { setXscale(1); setYscale(1); }

	public void createTransformMatrices ()
	{
		NMatrix scaleM = new NMatrix(new double[][]{new double[]{xScale, 0}, new double[]{0, yScale}});
		NMatrix rotationM = new NMatrix(new double[][]{new double[]{Math.cos(rotation), -1*Math.sin(rotation)}, new double[]{Math.sin(rotation), Math.cos(rotation)}});
		
		/*
		System.out.println(scaleM);
		System.out.println(vertices.get(0).toVertMatrix());
		System.out.println(scaleM.multiply(vertices.get(0).toVertMatrix()));
		System.out.println(scaleM.multiply(rotationM));
		System.out.println(scaleM.multiply(rotationM).multiply(vertices.get(0).toVertMatrix()));
		System.out.println(rotationM.multiply(scaleM).toSquare());
		*/

		transform = rotationM.multiply(scaleM).toSquare();
		transformInverse = transform.inverse2x2();
		//System.out.println(transform.inverse2x2());
	}

	/** NOPE. Don't use inverted matrix. It has major problems yo. Okay, maybe do. But don't do the general inversion algorithm in SMatrix. Just don't. And make sure you properly handle matrices where det = 0.
	// Note for the below: please check that the contains code for points and static points still works using the new contains code as below
	@Override
	public boolean contains (double x, double y)
	{
		//Use the inverseTransform to transform x and y to a position as if the normal contains would be occuring
		return (contains(x,y));
	}
	*/
	// Returns the point in a global format, with transformations applied
	@Override
	public Point getGlobalPoint (int i)
	{
		NMatrix m = transform.multiply(vertices.get(i).toVertMatrix());
		return (new Point(m.getElement(0,0) + this.getX(), m.getElement(0,1) + this.getY()));
	}

	public static void main (String[] args) throws InterruptedException
	{
		/*TPolygonEntity p = new TPolygonEntity(100,100, new StaticPoint[]{new StaticPoint(,3)});
		p.setXscale(20);
		p.setRotation(Math.PI/2);
		*/

		SimpleDisplay d = new SimpleDisplay(200,200,"Draw Test", true, true);
		Graphics2D g = d.getGraphics2D();
		g.setColor(Color.BLACK);
		g.fillRect(0,0,200,200);
		TPolygonEntity p = new TPolygonEntity(50,100);
		p.addPoints(new StaticPoint[]{new StaticPoint(20,20), new StaticPoint(-20,20), new StaticPoint(-10,-20), new StaticPoint(10,-20)});
		double angle = 0.0, a2 = 0.0, a3 = 0.0;
		while (true)
		{
			p.setX(100 + Math.cos(angle) * 60);
			p.setY(100 + Math.sin(angle) * 60);
			p.setRotation(-angle * 0.4);
			p.setXscale(1+Math.cos(a2));
			p.setYscale(1+Math.sin(a2));
			/*DistancedHit hit = p.hitScan(10,50,180,120);
			DistancedHit hit2 = p.hitScan(30,190,50,10);
			*/
			g.setColor(Color.BLACK);
			g.fillRect(0,0,200,200);
			g.setColor(Color.WHITE);
			p.draw(g);
			/*g.setColor(Color.RED);
			if(hit.madeContact())
				g.drawLine(10,50,(int)hit.getX(),(int)hit.getY());
			else
				g.drawLine(10,50,180,120);
			if(hit2.madeContact())
				g.drawLine(30,190,(int)hit2.getX(),(int)hit2.getY());
			else
				g.drawLine(30,190,50,10);
			*/
			d.repaint();
			Thread.sleep(50);
			angle += Math.PI/64;
			a2 += Math.PI/50;
		}
	}

}
