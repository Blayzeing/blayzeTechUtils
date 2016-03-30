package classes.env;

/**
 * A transformable *parentabl* polygon entity, it can have parents assigned to it.
 */

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import classes.graphics.SimpleDisplay;
import classes.math.*;
import classes.env.Environment;
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

	// Redefining projection to counter for parenting
	public Point[] projectToWorld(Point[] points)
	{
		/*Point[] out = new Point[points.length];
		for(int i = 0; i<points.length; i++)
		{
			NMatrix m = transform.multiply(points[i].toVertMatrix());
			out[i] = new Point(m.getElement(0,0) + this.getX(), m.getElement(0,1) + this.getY());
		}*/
		Point[] out = super.projectToWorld(points);
		if(parent == null)
			return out;
		else
			return (parent.projectToWorld(out));
	}
	public Point projectToWorld(Point point)
	{
		//NMatrix m = transform.multiply(point.toVertMatrix());
		//Point out = new Point(m.getElement(0,0) + this.getX(), m.getElement(0,1) + this.getY());
		Point out = super.projectToWorld(point);
		if(parent != null)
			return (parent.projectToWorld(out));
		return out;
	}


	//////////////////////// NOTE TO FUTURE BLAYZE: YOU CAN TOTALLY GET RID OF MOST OF THE STUFF ABOVE WITH A CALL TO SUPER.PROJECTTOWORLD.
	/// Dear past Blayze, I've done that now. I'll test the speeds by removing them and adding them and timing the difference at some point. Make sure you do that future Blayze.


	public Point[] projectLocally(Point[] points)
	{
		Point[] out = new Point[points.length];
		for(int i = 0; i<points.length; i++)
			out[i] = points[i].clone();
		if(parent != null)
			out = parent.projectLocally(out);
		return(super.projectLocally(out));
	}
	public Point projectLocally(Point point)
	{
		Point out = point.clone();
		if(parent != null)
			out = parent.projectLocally(out);
		return(super.projectLocally(out));
	}

	public static void main(String[] args) throws InterruptedException
	{
		//initialTest();
		parentingTest();
		//containmentTest();
		//hitscanTest();
	}
	public static void hitscanTest() throws InterruptedException
	{
		SimpleDisplay d = new SimpleDisplay(270,350,"Hitscan",true,true);
		Graphics2D g = d.getGraphics2D();
		StaticPoint[] shape = new StaticPoint[]{new StaticPoint(35,0), new StaticPoint(0,9), new StaticPoint(0,-9)};
		TPPolygonEntity[] bones = new TPPolygonEntity[15];
		Environment env = new Environment();
		for(int i = 0; i<bones.length; i++)
		{
			bones[i] = new TPPolygonEntity(shape[0].getX(),0,shape);
			env.entities.add(bones[i]);
			if(i-1>=0)
				bones[i].setParent(bones[i-1]);
			bones[i].setXscale(0.94);
			bones[i].setYscale(0.9);
		}
		bones[0].setX(10);
		bones[0].setY(350);
		bones[0].setRotation(-Math.PI/2);

		// Loop
		g.setColor(Color.WHITE);
		while(true)
		{
			//Moving
			for(int i = 1; i<bones.length; i++)
				bones[i].setRotation(Math.min(Math.max(-Math.PI/3,  bones[i].getRotation()+Math.PI/500  ),Math.PI/3));
			//Drawing
			d.fill(Color.BLACK);
			DistancedHit d1 = env.hitScan(20,20,180,340);
			if(d1.madeContact())
			{
				g.setColor(Color.RED);
				g.drawLine(20,20,(int)d1.getX(),(int)d1.getY());
				g.setColor(Color.GRAY);
				g.drawLine((int)d1.getX(),(int)d1.getY(),180,340);
			}else{
				g.setColor(Color.GRAY);
				g.drawLine(20,20,180,340);
			}

			g.setColor(Color.WHITE);
			for(TPPolygonEntity b:bones)
				b.draw(g);
			d.repaint();
			Thread.sleep(50);
		}
	}
	public static void containmentTest() throws InterruptedException
	{
		SimpleDisplay d = new SimpleDisplay(270,350,"Containment",true,true);
		Graphics2D g = d.getGraphics2D();
		StaticPoint[] shape = new StaticPoint[]{new StaticPoint(35,0), new StaticPoint(0,9), new StaticPoint(0,-9)};
		TPPolygonEntity[] bones = new TPPolygonEntity[15];
		Environment env = new Environment();
		for(int i = 0; i<bones.length; i++)
		{
			bones[i] = new TPPolygonEntity(shape[0].getX(),0,shape);
			env.entities.add(bones[i]);
			if(i-1>=0)
				bones[i].setParent(bones[i-1]);
			bones[i].setXscale(0.94);
			bones[i].setYscale(0.9);
		}
		bones[0].setX(10);
		bones[0].setY(350);
		bones[0].setRotation(-Math.PI/2);

		// Loop
		g.setColor(Color.WHITE);
		while(true)
		{
			//Moving
			for(int i = 1; i<bones.length; i++)
				bones[i].setRotation(Math.min(Math.max(-Math.PI/3,  bones[i].getRotation()+Math.PI/500  ),Math.PI/3));
			//Drawing
			for(int i = 0; i<270; i++)
				for(int o = 0; o<350; o++)
				{
					if(env.contains(i,o))
						g.setColor(Color.RED);
					else
						g.setColor(Color.BLACK);
					g.drawLine(i,o,i,o);
				}
			/*for(TPPolygonEntity b:bones)
				b.draw(g);
				*/
			d.repaint();
			Thread.sleep(50);
		}
	}
	public static void parentingTest() throws InterruptedException
	{
		SimpleDisplay d = new SimpleDisplay(800,400,"Parenting",true,true);
		Graphics2D g = d.getGraphics2D();
		StaticPoint[] shape = new StaticPoint[]{new StaticPoint(35,0), new StaticPoint(0,9), new StaticPoint(0,-9)};
		TPPolygonEntity[] bones = new TPPolygonEntity[15];
		for(int i = 0; i<bones.length; i++)
		{
			bones[i] = new TPPolygonEntity(shape[0].getX(),0,shape);
			if(i-1>=0)
				bones[i].setParent(bones[i-1]);
			bones[i].setXscale(0.94);
			bones[i].setYscale(0.9);
		}
		bones[0].setX(400);
		bones[0].setY(400);
		bones[0].setRotation(-Math.PI/2);

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
			//Drawing
			d.fill(Color.WHITE);
			for(TPPolygonEntity b:bones)
				b.draw(g);
			d.repaint();
			Thread.sleep(50);
		}
	}
	public static void initialTest() throws InterruptedException
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
		TPPolygonEntity p4 = new TPPolygonEntity(67.5,0, new StaticPoint[]{new StaticPoint(-10,0), new StaticPoint(0,-10), new StaticPoint(10,0), new StaticPoint(0,10)});
		p4.setParent(p3);

		// Loop
		g.setColor(Color.BLACK);
		double angle1 = 0, angle2 = 0;
		double mult = 0.3;
		while(true)
		{
			angle1 += Math.PI/20 * mult;
			angle2 += Math.PI/50 * mult;
			parentEntity.setX(200 + Math.cos(angle2)*100);
			parentEntity.setY(200 + Math.sin(angle2)*100);
			//parentEntity.setRotation(parentEntity.getRotation() + Math.PI/1000);
			//parentEntity.setYscale((Math.sin(angle1)+1)/2);
			p1.setX(Math.cos(angle1)*30);
			p1.setY(Math.sin(angle1)*30);
			p2.setRotation(p2.getRotation() + Math.PI/15 * mult);
			p3.setRotation(p3.getRotation() - Math.PI/22 * mult);
			p4.setRotation(p4.getRotation() + Math.PI/10 * mult);

			// Now draw each item
			d.fill(Color.WHITE);
			parentEntity.draw(g);
			p1.draw(g);
			p2.draw(g);
			p3.draw(g);
			p4.draw(g);
			d.repaint();
			Thread.sleep(50);
		}
	}
}
