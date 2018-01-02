package classes.env;

/**
 * An entity that has rotation and scale properties, it's *transformable*.
 *
 * On the subject of 0 X or Y scales (thus causing the transform to have no inverse):
 * If there is no inverse, then this shape must be infintesimally small, as such only a point directly on this shape's location would intersect with it.
 * So if so, then this can't contain it. Of course, this could be argued either way as it is a derivative of a self-containing set paradox, and it
 * certainly would be helpful in the case of hitscanning an object with 0 for one of the X or Y scales, however, for convinience, I shall not be
 * implementing that.
 */

import classes.env.*;
import classes.math.*;
import java.util.ArrayList;
import classes.graphics.SimpleDisplay;
import java.awt.Color;
import java.awt.Graphics2D;

public class TPolygonEntity extends PolygonEntity implements Transformable {
	//Below store the transformation information:
	private double rotation = 0;
	private double xScale = 1;
	private double yScale = 1;
	protected SMatrix transform = SMatrix.identity(2), transformInverse = SMatrix.identity(2);// This stores the actual transform matrix.

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
	public SMatrix getTransformationMatrix ()
	{
		return (transform.copy());
	}

	private void createTransformMatrices ()
	{
		NMatrix scaleM = new NMatrix(new double[][]{new double[]{xScale, 0}, new double[]{0, yScale}});
		NMatrix rotationM = new NMatrix(new double[][]{new double[]{Math.cos(rotation), -1*Math.sin(rotation)}, new double[]{Math.sin(rotation), Math.cos(rotation)}});
		transform = rotationM.multiply(scaleM).toSquare();
		transformInverse = transform.inverse2x2();
	}

	// Handling of replacement code:
	@Override
	public boolean contains (double x, double y)
	{
		if (transformInverse == null)
				return false;
		return(super.contains(x,y));
	}
	@Override
	public DistancedHit hitScan (double x1, double y1, double x2, double y2)
	{
		// If there is no inverse, then this shape must be infintesimally small, as such only a point directly on this shape's location would intersect with it.
		if (transformInverse == null)
		{
			return (new DistancedHit(false, x2, y2, Math.hypot(x1-x2, y1-y2)));
		}
		return(super.hitScan(x1,y1,x2,y2));
	}

	// NOTE TO SELF: Yes, those `super.x` calls below could be caluclated from within here directly, and maybe that's more efficient. But at the moment, this modular approach seems better.
	// (One has such an alternative written above it)
	@Override
	public Point projectLocally(Point p)
	{
		if(transformInverse == null)
		{
			double xdiff = p.getX() - this.getX();
			double ydiff = p.getY() - this.getY();
			return (new Point(xdiff==0?0:xdiff*Double.POSITIVE_INFINITY, ydiff==0?0:ydiff*Double.POSITIVE_INFINITY));
		}
		//NMatrix transformed = transformInverse.multiply(new NMatrix(new double[][]{new double[]{p.getX()-this.getX()}, new double[]{p.getY()-this.getY()}}));
		NMatrix transformed = transformInverse.multiply(super.projectLocally(p).toVertMatrix());
		return (new Point(transformed.getElement(0,0), transformed.getElement(0,1)));
	}
	@Override
	public Point[] projectLocally(Point[] points)
	{
		Point[] out = new Point[points.length];
		if(transformInverse == null)
		{
			for(int i = 0; i<points.length; i++)
			{
				double xdiff = points[i].getX() - this.getX();
				double ydiff = points[i].getY() - this.getY();
				out[i] = new Point(xdiff==0?0:xdiff*Double.POSITIVE_INFINITY, ydiff==0?0:ydiff*Double.POSITIVE_INFINITY);
			}
			return out;
		}
		NMatrix transformed = null;
		Point[] newPoints = super.projectLocally(points);
		for(int i = 0; i<points.length; i++)
		{
			transformed = transformInverse.multiply(newPoints[i].toVertMatrix());
			out[i] = new Point(transformed.getElement(0,0), transformed.getElement(0,1));
		}
		return out;
	}
	/**
	 * Projects the scalar locally. WARNING: This method is untested, I feel like the transform should be used instead of the inverse, but all other code uses the inverse and it's been too long since I wrote it to remember why. TODO.
	 */
	@Override
	public double projectLocally(double scalar)
	{
		return scalar * transformInverse.getDet();
	}

	@Override
	public Point projectToWorld(Point point)
	{
		NMatrix m = transform.multiply(point.toVertMatrix());
		return (super.projectToWorld(new Point(m.getElement(0,0), m.getElement(0,1))));
	}
	@Override
	public Point[] projectToWorld(Point[] points)
	{
		Point[] out = new Point[points.length];
		for(int i = 0; i<points.length; i++)
		{
			NMatrix m = transform.multiply(points[i].toVertMatrix());
			out[i] = super.projectToWorld(new Point(m.getElement(0,0), m.getElement(0,1)));
		}
		return out;
	}
	@Override
	public double projectToWorld(double scalar)
	{
		return scalar * transform.getDet();
	}

	public static void main (String[] args) throws InterruptedException
	{
		SimpleDisplay d0 = new SimpleDisplay(200,200,"Containment",true,true);
		Graphics2D g = d0.getGraphics2D();
		g.setColor(Color.BLACK);
		g.fillRect(0,0,200,200);
		TPolygonEntity p0 = new TPolygonEntity(100,100);
		p0.addPoints(new StaticPoint[]{new StaticPoint(20,20), new StaticPoint(-20,20), new StaticPoint(-10,-20), new StaticPoint(10,-20)});
		p0.setRotation(Math.PI * 2 * Math.random() - Math.PI);
		p0.setXscale(4 * Math.random());
		p0.setYscale(4 * Math.random());
		p0.setYscale(0.0000000000);
		//p0.setXscale(0);
		System.out.println(p0.transformInverse);
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
		//Two rays test:
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
		//Total containment test
		//for(int cc = 0; cc<210; cc++)
		//{
		//	p.setX(100 + Math.cos(angle) * 60);
		//	p.setY(100 + Math.sin(angle) * 60);
		//	p.setRotation(-angle * 0.4);
		//	p.setXscale(1+Math.cos(a2));
		//	p.setYscale(1+Math.sin(a2));
		//	for(int xPos = 0; xPos<200; xPos++)
		//		for(int a = 0; a<200; a++)
		//		{
		//			if(p.contains((double)xPos,(double)a))
		//				g.setColor(Color.RED);
		//			else
		//				g.setColor(Color.BLACK);
		//			g.drawLine(xPos,a,xPos,a);
		//		}
		//	p.draw(g);
		//	d.repaint();
		//	angle += Math.PI/64;
		//	a2 += Math.PI/50;
		//}
		//Top to bottom raycast curtain
		//for (int zzz = 0; zzz<1000; zzz++)
		//{
		//	p.setX(100 + Math.cos(angle) * 60);
		//	p.setY(100 + Math.sin(angle) * 60);
		//	p.setRotation(-angle * 0.4);
		//	p.setXscale(1+Math.cos(a2));
		//	p.setYscale(1+Math.sin(a2));
		//	DistancedHit[] scans = new DistancedHit[200];
		//	for(int i = 0; i<200; i++)
		//		scans[i] = p.hitScan(0,i,200,i);
		//	//DistancedHit hit = p.hitScan(10,50,180,120);
		//	//DistancedHit hit2 = p.hitScan(30,190,50,10);
		//	g.setColor(Color.BLACK);
		//	g.fillRect(0,0,200,200);
		//	g.setColor(Color.WHITE);
		//	p.draw(g);
		//	g.setColor(Color.RED);
		//	for(int i = 0; i<200; i++)
		//	{
		//		if(scans[i].madeContact())
		//			g.drawLine(0,i,(int)scans[i].getX(),(int)scans[i].getY());
		//		else
		//			g.drawLine(0,i,200,i);
		//	}
		//	d.repaint();
		//	Thread.sleep(500);
		//	angle += Math.PI/64;
		//	a2 += Math.PI/50;
		//}
	}

}
