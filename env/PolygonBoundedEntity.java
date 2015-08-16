package classes.env;

/**
 * An entity that has uses a polygon as a boundary.
 */

import classes.env.AbstractEntity;
import classes.math.Point;
import classes.math.StaticPoint;
import java.util.ArrayList;
import classes.env.DistancedHit;
import classes.math.NVector;
import classes.math.MoarMath;

public class PolygonBoundedEntity extends AbstractEntity {

	private Double left = Double.POSITIVE_INFINITY;
	private Double right = Double.NEGATIVE_INFINITY;
	private Double bottom = Double.NEGATIVE_INFINITY;
	private Double top = Double.POSITIVE_INFINITY;
	
	/*  NOTE TO SELF:
	 * Okay Blayze. So, in this class you are not going to bake the coordinates on each change, and rotation of the object as a whole
	 * will not be an option. Only rotaton of the relative points. This will be a hybrid system. This is done so that the points don't have
	 * to be rebaked every time an object moves, shuffling around arrays each time.
	 */
	
	private ArrayList<Point> vertices;
	
	public PolygonBoundedEntity (double x, double y)
	{
		super(x,y);
	}
	public PolygonBoundedEntity (double x, double y, StaticPoint[] points)
	{
		this(x,y);
		this.addPoints(points);
	}
	public PolygonBoundedEntity (double x, double y, ArrayList<StaticPoint> points)
	{
		this(x,y);
		this.addPoints(points);
	}

	/**
	 * Tests to see if the given coordinates are contained within this polygon.
	 * Performed as per the ray-tracing method described on this wikipedia
     * page: https://en.wikipedia.org/wiki/Point_in_polygon
	 */
	public boolean contains (double x, double y)
	{
		if(vertices.size() < 3)
			return false;
		int counter = 0;
		double startx = getTopLeftCorner.getX();
		Point lastPoint = vertices.get(vertices.size()-1);
		for(int i = 0; i<vertices.size(); i++)
		{
			Point thisPoint = vertices.get(i);
			if(MoarMath.lineSegmentIntersect(startx, y, x, y, lastPoint.getX(),lastPoint.getY(),thisPoint.getX(),thisPoint.getY()) != null)
				counter ++;
		}
		if(counter%2 == 0)// Even
			return false;
		else// Odd
			return true;
	}
	public DistancedHit hitScan(double x1, double y1, double x2, double y2)
	{
		// If the shape has no area
		if(vertices.size() < 1)
			return (new DistancedHit(false, x2, y2, Math.hypot(x1-x2, y1-y2)));
		if(vertices.size() < 2)
		{
			NVector scan = new NVector(new double[]{x2 - x1, y2 - y1});
			NVector point = new NVector(new double[]{vertices.get(o).getX() + getX() - x1, vertices.get(o).getY() + getY() - y1});
			if(scan.normalize().equals(point.normalize()))// the single vertex of this shape is on the hitScan line.
				return (new DistancedHit(true, getX(), getY(), Math.hypot(x1-getX(), y1-getY())));
			else
				return (new DistancedHit(false, x2, y2, Math.hypot(x1-x2, y1-y2)));
		}
		// If the shape is actually an object
		if(contains(x1,y1))// First check if the start point is inside the polygon, if so return a 0-length hitScan
			return(new DistancedHit(false, x2, y2, 0));
		x1 -= getX();
		x2 -= getX();
		y1 -= getY();
		y2 -= getY();
		Double shortestDistance = Math.hypot(x1-x2,y1-y2);
		double[] closestPoint = new double[]{x2,y2};
		boolean hit = false;
		Point lastPoint = vertices.get(vertices.size()-1);//bakedVerts[bakedVerts.length-1];
		for(int i = 0; i<vertices.size(); i++)
		{
			Point thisPoint = vertices.get(i);
			double[] collision = MoarMath.lineSegmentIntersect(x1,y1,x2,y2,lastPoint.getX(),lastPoint.getY(),thisPoint.getX(),thisPoint.getY());
			if(collision != null)
			{
				double distance = Math.hypot(x1-collision[0], y1-collision[1]);
				if(distance < shortestDistance)
				{
					shortestDistance = distance;
					closestPoint = collision;
					hit = true;
				}
			}
			lastPoint = thisPoint;
		}
		return(new DistancedHit(hit, closestPoint[0]+getX(), closestPoint[1]+getY(), shortestDistance));
	}

	/**
	 * Rotates the points for this shape around the given point relative to the center of the shape.
	 * @param	r	radii to rotate this shape by
	 */
	/*public void rotatePoints(double r, double x, double y)
	{
		
	}
	public void rotatePoints(double r)
	{
		rotatePoints(r,0,0);
	}*/
	// Translation can be done using get and set X&Y.
	/**
	 * Scales this shape by the given scale vector from it's center.
	 */
	public void scale(double xs, double ys)
	{
		
	}
	/**
	 * Scales this shape by the given scale from it's center.
	 */
	public void scale(double s)
	{
		scale(s,s);
	}
	public double getRotation()
	{
		return (rotation);
	}
	public void setRotation(double r)
	{
		rotation = r;
	}

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
	public void addPoints(ArrayList<double[]> points)
	{
		for(double[] p : points)
			addPoint(p[0],p[1]);
	}
	/**
	 * Add the given points to the shape.
	 * Note that these are added as if they came after the current points.
	 */
	public void addPoints(double[][] points)
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
	public void setPoints(ArrayList<double[]> points)
	{
		clearPoints();
		addPoints(points);
	}
	/**
	 * Delete all points and make given points new shape.
	 */
	public void setPoints(double[][] points)
	{
		clearPoints();
		addPoints(points);
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
	public void reAdjustEdges()
	{
		left = Double.POSITIVE_INFINITY;
		right = Double.NEGATIVE_INFINITY;
		bottom = Double.NEGATIVE_INFINITY;
		top = Double.POSITIVE_INFINITY;
		for(Point p : vertices)
			adjustEdge(p.getX(), p.getY());

	public Point getTopTopLeftCorner()
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
		return (bottomn - top);
	}
	public void draw(Graphics2D g)
	{
		for ( Point p : vertices)
			//// MEEEH. Just draw it.
	}

	public String toString()
	{
		return("Polygon Bounded Entity] Verticies: " + vertices.size() + /* print points here */"\n'-> " + super.toString());
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
}
