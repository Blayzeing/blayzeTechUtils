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
		transform = rotationM.multiply(scaleM).toSquare();
		transformInverse = transform.inverse2x2();
	}

	/** NOPE. Don't use inverted matrix. It has major problems yo. Okay, maybe do. But don't do the general inversion algorithm in SMatrix. Just don't. And make sure you properly handle matrices where det = 0.
	 */
	// Note for the below: please check that the contains code for points and static points still works using the new contains code as below
	@Override
	public boolean contains (double x, double y)
	{
		// If there is no inverse, then this shape must be infintesimally small, as such only a point directly on this shape's location would intersect with it.
		if (transformInverse == null)
		{
			if(x == this.getX() && y == this.getY())
				return true;
			else
				return false;
		}
		//Use the inverseTransform to transform x and y to a position as if the normal contains would be occuring
		NMatrix transformed = transformInverse.multiply(new NMatrix(new double[][]{new double[]{x-this.getX()}, new double[]{y-this.getY()}}));
		transformed = transformed.add(new NMatrix(new double[][]{new double[]{this.getX()}, new double[]{this.getY()}}));
		return (super.contains(transformed.getElement(0,0),transformed.getElement(0,1)));
	}
	@Override
	public DistancedHit hitScan (double x1, double y1, double x2, double y2)
	{
		// If there is no inverse, then this shape must be infintesimally small, as such only a point directly on this shape's location would intersect with it.
		if (transformInverse == null)
		{
			if(x1 == this.getX() && y1 == this.getY())
				return (new DistancedHit(true, getX(), getY(), Math.hypot(x1-getX(), y1-getY())));
			else
				return (new DistancedHit(false, x2, y2, Math.hypot(x1-x2, y1-y2)));
		}
		//Use the inverseTransform to transform x and y to a position as if the normal contains would be occuring
		NMatrix transformed = transformInverse.multiply(new NMatrix(new double[][]{new double[]{x1-this.getX(), x2-this.getX()}, new double[]{y1-this.getY(), y2-this.getY()}}));
		transformed = transformed.add(new NMatrix(new double[][]{new double[]{this.getX(), this.getX()}, new double[]{this.getY(), this.getY()}}));
		// After using that, `localRes` stores the hit within the local space. Transform it back to worldspace.
		DistancedHit localRes = super.hitScan(transformed.getElement(0,0),transformed.getElement(0,1),transformed.getElement(1,0),transformed.getElement(1,1));
		NMatrix offset = new NMatrix(new double[][]{new double[]{this.getX()}, new double[]{this.getY()}});
		NMatrix finalContact = transform.multiply(localRes.toVertMatrix().subtract(offset)).add(offset);
		return (new DistancedHit(localRes.madeContact(), finalContact.getElement(0,0), finalContact.getElement(0,1), Math.hypot(x1-finalContact.getElement(0,0), y1-finalContact.getElement(0,1))));
	}
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
		SimpleDisplay d0 = new SimpleDisplay(200,200,"Containment",true,true);
		Graphics2D g = d0.getGraphics2D();
		g.setColor(Color.BLACK);
		g.fillRect(0,0,200,200);
		TPolygonEntity p0 = new TPolygonEntity(100,100);
		p0.addPoints(new StaticPoint[]{new StaticPoint(20,20), new StaticPoint(-20,20), new StaticPoint(-10,-20), new StaticPoint(10,-20)});
		p0.setRotation(Math.PI * 2 * Math.random() - Math.PI);
		p0.setXscale(4 * Math.random());
		p0.setYscale(4 * Math.random());
		for(int x = 0; x<200; x++)
			for(int y = 0; y<200; y++)
			{
				if(p0.contains(new Point(x,y)))
					g.setColor(Color.RED);
				else
					g.setColor(Color.BLACK);
				g.drawLine(x,y,x,y);
				d0.repaint();
			}
		g.setColor(Color.WHITE);
		p0.draw(g);
		d0.repaint();


		SimpleDisplay d = new SimpleDisplay(200,200,"Draw Test", true, true);
		g = d.getGraphics2D();
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
			DistancedHit hit = p.hitScan(10,50,180,120);
			DistancedHit hit2 = p.hitScan(30,190,50,10);
			g.setColor(Color.BLACK);
			g.fillRect(0,0,200,200);
			g.setColor(Color.WHITE);
			p.draw(g);
			g.setColor(Color.RED);
			if(hit.madeContact())
				g.drawLine(10,50,(int)hit.getX(),(int)hit.getY());
			else
				g.drawLine(10,50,180,120);
			if(hit2.madeContact())
				g.drawLine(30,190,(int)hit2.getX(),(int)hit2.getY());
			else
				g.drawLine(30,190,50,10);
			d.repaint();
			Thread.sleep(50);
			angle += Math.PI/64;
			a2 += Math.PI/50;
		}
	}

}
