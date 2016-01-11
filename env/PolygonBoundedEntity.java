package classes.env;

/**
 * An entity that has a polygon as a boundary.
 *
 * ===========================================
 * NOTE: THIS IS NOW DEPRECATED CODE, PLEASE
 * USE POLYGONENTITY.
 * ===========================================
 */

import java.awt.Graphics2D;
import classes.env.AbstractEntity;
import classes.math.Point;
import classes.math.StaticPoint;
import java.util.ArrayList;
import classes.env.DistancedHit;
import classes.math.NVector;
import classes.math.MoarMath;
import classes.graphics.SimpleDisplay;
import java.awt.Color;

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

	/* MESSAGE IN REPLY TO SELF:
	 * Okay, I hear where you're coming from Blayze, but I really think that the benefit of having a single rotation value and calculating the new point coordinates
	 * (relative to the centre) on the fly (if rotation is non-zero [I'd suggest a bool to keep track of if setRotation() has been called and having a resetRotation()])
	 * This will allow for much more maleability compared to what you're suggesting. I mean, seriously? The shape will break down due to rounding errors eventually!
	 */

	/* NOTE:
	 * The above will need a counter-rotation to be performed in the hitscan and contains code
	 */
	
	private ArrayList<Point> vertices = new ArrayList<Point>();
	
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
		double startx = getTopLeftCorner().getX();
		startx -= 1;// Make sure you're far enough left
		Point lastPoint = vertices.get(vertices.size()-1);
		lastPoint = new Point(lastPoint.getX() + getX(), lastPoint.getY() + getY());
		for(int i = 0; i<vertices.size(); i++)
		{
			Point thisPoint = vertices.get(i);
			thisPoint = new Point(thisPoint.getX() + getX(), thisPoint.getY() + getY());
			// The y comparisons at the start basically eliminate the very ends of each edge, avoiding double-collisions.
			if(thisPoint.getY() != y && MoarMath.lineSegmentIntersect(startx, y, x, y, lastPoint.getX(),lastPoint.getY(),thisPoint.getX(),thisPoint.getY()) != null)
				counter ++;
			// Special case for if trying to intersect with a horizontal line:
			else if(thisPoint.getY() == lastPoint.getY() && thisPoint.getY() == y && x > Math.min(lastPoint.getX(), thisPoint.getX())/* THE RAY MUST BE INTERSECTING WITH THE LINE IN 1D*/)
				counter ++;
			lastPoint = thisPoint;
		}
		if(counter == 3)
			System.out.print(counter);
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
			NVector point = new NVector(new double[]{vertices.get(0).getX() + getX() - x1, vertices.get(0).getY() + getY() - y1});
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
		
		readjustEdges();
	}
	public void rotatePoints(double r)
	{
		rotatePoints(r,0,0);
	}
	This is rather confusing, but basically, this rotates the shape's points around it's centre and updates the shape's 
	public void rotate(double r)
	*/
	// Translation can be done using get and set X&Y.
	/**
	 * Scales this shape by the given scale vector from it's center.
	 */
	/*public void scale(double xs, double ys)
	{
		for (
		readjustEdges();
	}*/
	/**
	 * Scales this shape by the given scale from it's center.
	 */
	/*public void scale(double s)
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
	}*/

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
		return( new Point(vertices.get(i).getX() + this.getX(), vertices.get(i).getY() + this.getY()));
	}
	public void draw(Graphics2D g)
	{
		Point last = getGlobalPoint(vertices.size()-1);
		for (int i = 0; i<vertices.size(); i++)
		{
			Point thisPoint = getGlobalPoint(i);
			g.drawLine((int)last.getX(), (int)last.getY(), (int)thisPoint.getX(), (int)thisPoint.getY());
			last = thisPoint;
		}
			//g.drawLine(vertices.get(i).getX(), vertices.get(i).getY(), vertices.get((i+1)%vertices.size()).getX(), vertices.get((i+1)%vertices.size()));
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

	public static void main(String[] args) throws InterruptedException
	{
		PolygonBoundedEntity p = new PolygonBoundedEntity(50,50, new StaticPoint[]{new StaticPoint(-20,-20),new StaticPoint(30,-15), new StaticPoint (60,70), new StaticPoint(5,5)});
		//p = new PolygonBoundedEntity(100,100, new StaticPoint[]{new StaticPoint(-20,-20), new StaticPoint(20, -20), new StaticPoint(20,20), new StaticPoint(-20,20)});
		//p = new PolygonBoundedEntity(100,100, new StaticPoint[]{new StaticPoint(-20,-20), new StaticPoint(20, -20), new StaticPoint(20,20), new StaticPoint(-20,20), new StaticPoint(0,0)});
		p = new PolygonBoundedEntity(100,100, new StaticPoint[]{new StaticPoint(-20,-20), new StaticPoint(20, -20), new StaticPoint(20,20), new StaticPoint(-20,20), new StaticPoint(0,1), new StaticPoint(0,-1)});
		SimpleDisplay d = new SimpleDisplay(200,200,"Containment", true, true);
		Graphics2D g = d.getGraphics2D();
		g.setColor(Color.BLACK);
		g.fillRect(0,0,200,200);
		p.draw(g);
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
		//	Thread.sleep(20);
		}
		g.setColor(Color.WHITE);
		p.draw(g);
		d.repaint();
	}
}
