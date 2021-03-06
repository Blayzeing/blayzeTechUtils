package blayzeTechUtils.env;

/**
 * An entity that has a polygon as a boundary.
 * This entity cannot be changed in size and rotation, only in location by altering it's x and y coordinates.
 * The Transformable Polygon Entity (TPolygonEntity) must be used to allow for rotation and scale properties
 */

import java.awt.Graphics2D;
import blayzeTechUtils.env.AbstractEntity;
import blayzeTechUtils.math.Point;
import blayzeTechUtils.math.StaticPoint;
import java.util.ArrayList;
import blayzeTechUtils.env.DistancedHit;
import blayzeTechUtils.math.NVector;
import blayzeTechUtils.math.MoarMath;
import blayzeTechUtils.graphics.SimpleDisplay;
import java.awt.Color;

public class PolygonEntity extends AbstractEntity {

	private Double left = Double.POSITIVE_INFINITY;
	private Double right = Double.NEGATIVE_INFINITY;
	private Double bottom = Double.NEGATIVE_INFINITY;
	private Double top = Double.POSITIVE_INFINITY;
	protected ArrayList<Point> vertices = new ArrayList<Point>();
	/** invert allows this entity to act 'inside-out', every point outside the boundary returns as being inside */
	public boolean invert = false;
	
	public PolygonEntity (double x, double y)
	{
		super(x,y);
	}
	public PolygonEntity (double x, double y, StaticPoint[] points)
	{
		this(x,y);
		this.addPoints(points);
	}
	public PolygonEntity (double x, double y, ArrayList<StaticPoint> points)
	{
		this(x,y);
		this.addPoints(points);
	}

	public int getVertexCount()
	{
		return (vertices.size());
	}

	/**
	 * Tests to see if the given coordinates are contained within this polygon.
	 * Performed as per the ray-tracing method described on this wikipedia
     	 * page: https://en.wikipedia.org/wiki/Point_in_polygon
	 */
	public boolean contains (double x, double y)
	{
		Point localPoint = projectLocally(new Point(x,y));
		return (localContains(localPoint.getX(),localPoint.getY()));
	}

	/**
	 * Local containment code.
	 * This code assumes a shape with no translation ability (ie on this shape's local plane), and checks it's containment.
	 * TODO: Instead of using line segment intersection here, an infinitely long line that comes from the left and stops at the point. This might be faster.
	 */
	protected boolean localContains (double x, double y)
	{
		if(vertices.size() < 3)
			return false;
		int counter = 0;
		// Get the leftmost x value
		double startx = Double.POSITIVE_INFINITY;
		for(Point v:vertices)
		{
			if(startx>v.getX())
				startx = v.getX();
		}
		startx -= 1;// Make sure you're far enough left
		Point lastPoint = vertices.get(vertices.size()-1);
		for(int i = 0; i<vertices.size(); i++)
		{
			Point thisPoint = vertices.get(i);
			// The y comparisons at the start basically eliminate the very ends of each edge, avoiding double-collisions.
			if(thisPoint.getY() != y && MoarMath.lineSegmentIntersect(startx, y, x, y, lastPoint.getX(),lastPoint.getY(),thisPoint.getX(),thisPoint.getY()) != null)
				counter ++;
			// Special case for if trying to intersect with a horizontal line:
			else if(thisPoint.getY() == lastPoint.getY() && thisPoint.getY() == y && x > Math.min(lastPoint.getX(), thisPoint.getX())/* THE RAY MUST BE INTERSECTING WITH THE LINE IN 1D*/)
				counter ++;
			lastPoint = thisPoint;
		}
		if((counter%2 == 0) ^ invert)// Even (inversion in there for outer-loops)
			return false;
		else// Odd
			return true;
	}
	/**
	 * Tests to see how far a ray would go if sent between (x1,y2) and (x2,y2).
	 */
	//TODO: This and all hitscan things should return a Hit object, that should be cast to an appropriate type.
	public DistancedHit hitScan(double x1, double y1, double x2, double y2)
	{
		//First, localise the given points
		Point[] localPoints = projectLocally(new Point[]{new Point(x1,y1), new Point(x2,y2)});
		//Then perform the hitscan
		Hit hit = localHitScan(localPoints[0].getX(), localPoints[0].getY(), localPoints[1].getX(), localPoints[1].getY());
		//Then finally delocalise the returned hitscan and return it
		hit.projectToWorldUsing(this);
		return (DistancedHit)hit;
	}

	/**
	 * Runs a hitscan test within the local environment of the object.
	 * This is protected so that classes that inherit from this class can override it to add aditional functionality without having to re-write the hitScan methods.
	 */
	protected DistancedHit localHitScan(double x1, double y1, double x2, double y2)
	{
		// If the shape has no area
		if(vertices.size() < 1)
			return (new DistancedHit(false, x2, y2, Math.hypot(x1-x2, y1-y2)));
		if(vertices.size() < 2)
		{
			NVector scan = new NVector(new double[]{x2 - x1, y2 - y1});
			NVector point = new NVector(new double[]{vertices.get(0).getX() - x1, vertices.get(0).getY() - y1});
			if(scan.normalize().equals(point.normalize()))// the single vertex of this shape is on the hitScan line.
				return (new DistancedHit(true, vertices.get(0).getX(), vertices.get(0).getY(), Math.hypot(x1-vertices.get(0).getX(), y1-vertices.get(0).getY())));
			else
				return (new DistancedHit(false, x2, y2, Math.hypot(x1-x2, y1-y2)));
		}
		// If the shape is actually an object...
		if(localContains(x1,y1))// First check if the start point is inside the polygon, if so return a 0-length hitScan
			return(new DistancedHit(true, x1, y1, 0));
		double dx = x1 - x2;
		double dy = y1 - y2;
		double shortestDistance = dx*dx+dy*dy;
		double[] closestPoint = new double[]{x2,y2};
		boolean hit = false;
		Point lastPoint = vertices.get(vertices.size()-1);
		for(int i = 0; i<vertices.size(); i++)
		{
			Point thisPoint = vertices.get(i);
			double[] collision = MoarMath.lineSegmentIntersect(x1,y1,x2,y2,lastPoint.getX(),lastPoint.getY(),thisPoint.getX(),thisPoint.getY());
			if(collision != null)
			{
				dx = x1-collision[0];
				dy = y1-collision[1];
				double distance = dx*dx+dy*dy;
				if(distance < shortestDistance)
				{
					shortestDistance = distance;
					closestPoint = collision;
					hit = true;
				}
			}
			lastPoint = thisPoint;
		}
		shortestDistance = Math.sqrt(shortestDistance);
		return(new DistancedHit(hit, closestPoint[0], closestPoint[1], shortestDistance));
	}
	public DistancedHit hitScan (Point p1, Point p2) { return hitScan(p1.getX(), p1.getY(), p2.getX(), p2.getY()); }
	public DistancedHit hitScan (StaticPoint p1, StaticPoint p2) { return hitScan(p1.getX(), p1.getY(), p2.getX(), p2.getY()); }

	// Projection Code
	/**
	 * Takes a point in worldspace and projects it to the local coordinate system, including this shape's transformation.
	 */
	public Point projectLocally(Point p)
	{
		return (new Point(p.getX() - this.getX(), p.getY() - this.getY()));
	}
	/**
	 * Takes an array of points in worldspace and projects them to the local coordinate system, including this shape's transformation.
	 */
	public Point[] projectLocally(Point[] points)
	{
		Point[] out = new Point[points.length];
		for(int i = 0; i<points.length; i++)
			out[i] = new Point(points[i].getX() - this.getX(), points[i].getY() - this.getY());
		return out;
	}
	/**
	 * Takes a scalar in worlspace and projects it into the local coordinate system, including this shape's transformation.
	 */
	public double projectLocally(double scalar)
	{
		//PolygonEntity does no scaling, so the scalar is unchanged.
		return scalar;
	}
	/**
	 * Takes a point as if it were in this object's euclidian plane and projects it to the global plane.
	 */
	public Point projectToWorld(Point point)
	{
		return (new Point(point.getX() + this.getX(), point.getY() + this.getY()));
	}
	/**
	 * Takes an array of points as if they were in this object's euclidian plane and projects them to the global plane.
	 */
	public Point[] projectToWorld(Point[] points)
	{
		Point out[] = new Point[points.length];
		for(int i = 0; i<points.length; i++)
			out[i] = new Point(points[i].getX() + this.getX(), points[i].getY() + this.getY());
		return out;
	}
	/**
	 * Takes a scalar as if it were in this object's euclidian plane and projects it to the global plane.
	 */
	public double projectToWorld(double scalar)
	{
		// There's no scaling done here, so just return the scalar.
		return scalar;
	}

	// Point reference retrieval code
	public Point getPointReferenceByIndex(int i)
	{
		return (vertices.get(i));
	}
	public Point getPointReferenceByPosition(double x, double y)
	{
		for(Point p : vertices)
			if(p.getX() == x && p.getY() == y)
				return p;
		return null;
	}

	/////// RUN AWAY FROM THE POINT CONTROL CODE BELOW: ///////
	/**
	 * Add the given point to the shape.
	 * Note that this added as if it came after the current points.
	 */
	public void addPoint(double x, double y)
	{
		vertices.add(new Point(x,y));
		adjustEdge(x,y);
	}
	/**
	 * Add the given points to the shape.
	 * Note that these are added as if they came after the current points.
	 */
	public void addPoints(ArrayList<StaticPoint> points)
	{
		for(StaticPoint p : points)
			addPoint(p.getX(), p.getY());
	}
	/**
	 * Add the given points to the shape.
	 * Note that these are added as if they came after the current points.
	 */
	public void addPoints(StaticPoint[] points)
	{
		for(StaticPoint p : points)
			addPoint(p.getX(), p.getY());
	}
	/**
	 * Add the given points to the shape.
	 * Note that these are added as if they came after the current points.
	 */
	public void addArrayPoints(ArrayList<double[]> points)
	{
		for(double[] p : points)
			addPoint(p[0],p[1]);
	}
	/**
	 * Add the given points to the shape.
	 * Note that these are added as if they came after the current points.
	 */
	public void addArrayPoints(double[][] points)
	{
		for(double[] p : points)
			addPoint(p[0],p[1]);
	}
	/**
	 * Delete all points and make given points new shape.
	 */
	public void setPoints(ArrayList<StaticPoint> points)
	{
		clearPoints();
		addPoints(points);
	}
	/**
	 * Delete all points and make given points new shape.
	 */
	public void setPoints(StaticPoint[] points)
	{
		clearPoints();
		addPoints(points);
	}
	/**
	 * Delete all points and make given points new shape.
	 */
	public void setArrayPoints(ArrayList<double[]> points)
	{
		clearPoints();
		addArrayPoints(points);
	}
	/**
	 * Delete all points and make given points new shape.
	 */
	public void setArrayPoints(double[][] points)
	{
		clearPoints();
		addArrayPoints(points);
	}
	/**
	 * Delete all points.
	 */
	public void clearPoints()
	{
		vertices.clear();
		left = Double.POSITIVE_INFINITY;
		right = Double.NEGATIVE_INFINITY;
		bottom = Double.NEGATIVE_INFINITY;
		top = Double.POSITIVE_INFINITY;
	}
	/**
	 * Must be called after a point has been changed outside of this object by reference.
	 */
	public void readjustEdges()
	{
		left = Double.POSITIVE_INFINITY;
		right = Double.NEGATIVE_INFINITY;
		bottom = Double.NEGATIVE_INFINITY;
		top = Double.POSITIVE_INFINITY;
		for(Point p : vertices)
			adjustEdge(p.getX(), p.getY());
	}

	public Point getTopLeftCorner()
	{
		return (new Point(left + getX(), top + getY()));
	}
	public Point getTopRightCorner()
	{
		return (new Point(right + getX(), top + getY()));
	}
	public Point getBottomRightCorner()
	{
		return (new Point(right + getX(), bottom + getY()));
	}
	public Point getBottomLeftCorner()
	{
		return (new Point(left + getX(), bottom + getY()));
	}
	public double getWidth()
	{
		return (right - left);
	}
	public double getHeight()
	{
		return (bottom - top);
	}
	public Point getGlobalPoint(int i)
	{
		return(projectToWorld(vertices.get(i)));
	}
	public ArrayList<Point> getAllGlobalPoints()
	{
		ArrayList<Point> out = new ArrayList<Point>();
		for(Point v:vertices)
			out.add(projectToWorld(v));
		return out;
	}
	public Point[] getAllGlobalPointsAsArray()
	{
		Point[] out = new Point[vertices.size()];
		int i = 0;
		for(Point v:vertices)
		{
			out[i] = projectToWorld(v);
			i++;
		}
		return out;
	}
	public void draw(Graphics2D g)
	{
		if(vertices.size() < 2)
			return;
		Point last = getGlobalPoint(vertices.size()-1);
		for (int i = 0; i<vertices.size(); i++)
		{
			Point thisPoint = getGlobalPoint(i);
			g.drawLine((int)last.getX(), (int)last.getY(), (int)thisPoint.getX(), (int)thisPoint.getY());
			last = thisPoint;
		}
	}

	public String toString()
	{
		return("[Polygon Entity] Verticies: " + vertices.size() + /* print points here? */"\n'-> " + super.toString());
	}

	private void adjustEdge(double x, double y)
	{
		if(x>right)
			right = x;
		if(x<left)
			left = x;
		if(y>bottom)
			bottom = y;
		if(y<top)
			top = y;
	}

	public static void main(String[] args) throws InterruptedException
	{
		// Making variables
		PolygonEntity p;
		//p = new PolygonEntity(50,50, new StaticPoint[]{new StaticPoint(-20,-20),new StaticPoint(30,-15), new StaticPoint (60,70), new StaticPoint(5,5)});
		//p = new PolygonEntity(100,100, new StaticPoint[]{new StaticPoint(-20,-20), new StaticPoint(20, -20), new StaticPoint(20,20), new StaticPoint(-20,20), new StaticPoint(0,1), new StaticPoint(0,-1)});
		int pCount = (int)(Math.random() * 7 + 3);
		StaticPoint[] points = new StaticPoint[pCount];
		for (int i = 0; i < pCount; i++)
			points[i] = new StaticPoint(Math.random() * 200 - 100, Math.random() * 200 - 100);
		p = new PolygonEntity(100,100,points);
		//p.invert = !p.invert;
		 
		// CONTAINTMENT TESTS:
		SimpleDisplay d = new SimpleDisplay(200,200,"Containment", true, true);
		Graphics2D g = d.getGraphics2D();
		g.setColor(Color.BLACK);
		g.fillRect(0,0,200,200);
		d.repaint();
		for(int x = 0; x < 201; x++)
		{
			for(int y = 0; y<201; y++)
			{
			      if(p.contains(x,y))
			      	g.setColor(Color.RED);
			      else
			      	g.setColor(Color.BLACK);
			      g.drawLine(x,y,x,y);
			      d.repaint();
			}
		}
		g.setColor(Color.WHITE);
		p.draw(g);
		d.repaint();

		// HITSCAN TESTS:
		SimpleDisplay d2 = new SimpleDisplay(200,200,"Hitscan", true, true);
		g = d2.getGraphics2D();
		g.setColor(Color.BLACK);
		g.fillRect(0,0,200,200);
		g.setColor(Color.WHITE);
		p.draw(g);
		d.repaint();
		g.setColor(Color.RED);
		for(int i = 0; i<100; i++)
		{
			int x1 = (int)(Math.random() * 200);
			int y1 = (int)(Math.random() * 200);
			int x2 = (int)(Math.random() * 200);
			int y2 = (int)(Math.random() * 200);
			DistancedHit hit = p.hitScan(x1,y1,x2,y2);
			double outX = x2;
			double outY = y2;
			if(hit.madeContact())
			{
				outX = hit.getX();
				outY = hit.getY();
			}
			g.drawLine(x1, y1, (int)outX, (int)outY);
			g.setColor(Color.BLUE);
			g.drawLine(x1,y1,x1,y1);
			g.setColor(Color.RED);
		}

		// TRANSLATION TESTS:
		SimpleDisplay d3 = new SimpleDisplay(200,200,"Translation", true, true);
		g = d3.getGraphics2D();

		g.setColor(Color.BLACK);
		g.fillRect(0,0,200,200);
		PolygonEntity mvEnt = new PolygonEntity(50,100);
		mvEnt.addPoints(new StaticPoint[]{new StaticPoint(20,20), new StaticPoint(-20,20), new StaticPoint(-20,-20), new StaticPoint(20,-20)});
		double angle = 0.0;
		while (true)
		{
			mvEnt.setX(100 + Math.cos(angle) * 60);
			mvEnt.setY(100 + Math.sin(angle) * 60);
			DistancedHit hit = mvEnt.hitScan(10,50,180,120);
			DistancedHit hit2 = mvEnt.hitScan(30,190,50,10);
			g.setColor(Color.BLACK);
			g.fillRect(0,0,200,200);
			g.setColor(Color.WHITE);
			mvEnt.draw(g);
			g.setColor(Color.RED);
			if(hit.madeContact())
				g.drawLine(10,50,(int)hit.getX(),(int)hit.getY());
			else
				g.drawLine(10,50,180,120);
			if(hit2.madeContact())
				g.drawLine(30,190,(int)hit2.getX(),(int)hit2.getY());
			else
				g.drawLine(30,190,50,10);
			d3.repaint();
			Thread.sleep(100);
			angle += Math.PI/64;
		}
	}
}
