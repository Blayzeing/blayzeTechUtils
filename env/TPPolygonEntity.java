package classes.env;

/**
 * A transformable *parentabl* polygon entity, it can have parents assigned to it.
 */

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import classes.graphics.SimpleDisplay;
import classes.math.*;
// Notes about this class.
// I believe that it should have a parent, and that it should have a method to transform a set of coordinates and return them transformed to this entity's
// euclidian plane. Then, for drawing, we could run a transform of it's vertices that would subsequently transform it's vertices based on it's transformation
// matrix and position, then send those transformed vertices to it's parent for further transformation if it has a parent, finally returning a list of fully
// transformed vertices ready for drawing globally.
// The same would have to be done for contains and hitScan methods, although this would have to be done in reverse first, to get the contains and hitscan
// points on the local plane and then forwards again in the case of hitscanning. Potential candidate method names would be projectToWorld and projectLocally,
// both would take a list/array (the latter being more efficient) of Points and would do as described.
//
// This system may require a comprehensive two-way link to be made between two TPPolygonEntitys

public class TPPolygonEntity extends TPolygonEntity {
	// ArrayList of this TPPolygonEntity's children
	private TPPolygonEntity parent = null;

	public TPPolygonEntity (double x, double y) { super(x,y); }
	public TPPolygonEntity (double x, double y, TPPolygonEntity p) { super(x,y); parent = p; }
	public TPPolygonEntity (double x, double y, StaticPoint[] ps) { super(x,y,ps); }
	public TPPolygonEntity (double x, double y, ArrayList<StaticPoint> ps) { super(x,y,ps); }
	public TPPolygonEntity (double x, double y, StaticPoint[] ps, TPPolygonEntity p) { super(x,y,ps); parent = p; }
	public TPPolygonEntity (double x, double y, ArrayList<StaticPoint> ps, TPPolygonEntity p) { super(x,y,ps); parent = p; }

	public void setParent(TPPolygonEntity e)
	{
		if(e != this)
			parent = e;
		else
			System.out.println("WARNING: A TPPolygonEntity was trying to be set to be a child of itself.");
	}
	public void removeParent()
	{
		parent = null;
	}

	/**
	 * Takes an array of points as if they were in this objects euclidian plane and projects them (paying respect to parent entities) to the global plane.
	 */
	public Point[] projectToWorld(Point[] points)
	{
		Point[] out = new Point[points.length];
		for(int i = 0; i<points.length; i++)
		{
			NMatrix m = transform.multiply(points[i].toVertMatrix());
			out[i] = new Point(m.getElement(0,0) + this.getX(), m.getElement(0,1) + this.getY());
		}
		if(parent == null)
			return out;
		else
			return (parent.projectToWorld(out));
	}

	//These methods should just wrap projectToWorld using different inputs
	@Override
	public Point getGlobalPoint (int i)
	{
		return(projectToWorld(new Point[]{vertices.get(i)})[0]);
	}
	@Override
	public Point[] getAllGlobalPointsAsArray()
	{
		Point[] out = new Point[vertices.size()];
		out = vertices.toArray(out);
		return(projectToWorld(out));
	}
	@Override
	public ArrayList<Point> getAllGlobalPoints()
	{
		Point[] outA = getAllGlobalPointsAsArray();
		ArrayList<Point> out = new ArrayList<Point>();
		for(Point p:outA)
			out.add(p);
		return (out);
	}

	public static void main(String[] args) throws InterruptedException
	{
		SimpleDisplay d = new SimpleDisplay(400,400,"Drawing",true,true);
		Graphics2D g = d.getGraphics2D();
		// Create the group object
		TPPolygonEntity parentEntity = new TPPolygonEntity(0,0, new StaticPoint[]{new StaticPoint(-2,-2), new StaticPoint(2,-2), new StaticPoint(2,2), new StaticPoint(-2,2)});
		// Now add an entity to orbit this center
		TPPolygonEntity p1 = new TPPolygonEntity(30,0, new StaticPoint[]{new StaticPoint(0,-5), new StaticPoint(-5,5), new StaticPoint(5,5)});
		p1.setParent(parentEntity);
		// Now add a new entity to show rotation (note the parent entity on the end)
		TPPolygonEntity p2 = new TPPolygonEntity(45,20, new StaticPoint[]{new StaticPoint(0,-5), new StaticPoint(-5,5), new StaticPoint(5,5)}, parentEntity);
		// Now lets add one more entity for fun rotation times!
		TPPolygonEntity p3 = new TPPolygonEntity(0,0, new StaticPoint[]{new StaticPoint(70,0), new StaticPoint(65,2), new StaticPoint(65,-2)}, parentEntity);
		// And now for some major small rotations
		TPPolygonEntity p4 = new TPPolygonEntity(0,0, new StaticPoint[]{new StaticPoint(-10,0), new StaticPoint(0,-10), new StaticPoint(10,0), new StaticPoint(0,10)});
		p4.setParent(p3);

		g.setColor(Color.BLACK);
		double angle1 = 0, angle2 = 0;
		while(true)
		{
			angle1 += Math.PI/20;
			angle2 += Math.PI/50;
			parentEntity.setX(200 + Math.cos(angle2)*100);
			parentEntity.setY(200 + Math.sin(angle2)*100);
			parentEntity.setRotation(parentEntity.getRotation() + Math.PI/1000);
			p1.setX(Math.cos(angle1)*30);
			p1.setY(Math.sin(angle1)*30);
			p2.setRotation(p2.getRotation() + Math.PI/15);
			p3.setRotation(p3.getRotation() - Math.PI/22);

			// Now draw each item
			d.fill(Color.WHITE);
			parentEntity.draw(g);
			p1.draw(g);
			p2.draw(g);
			p3.draw(g);
			p4.draw(g); // TODO: Work out why this is not over p3.
			d.repaint();
			Thread.sleep(50);
			// TODO: Check that this works ok with different X&Y scales
		}
	}
}
