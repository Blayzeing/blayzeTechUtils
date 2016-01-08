package classes.math;

/**
 * A useful class for storing more mathematical functions.
 * Written by Blayze Millward
 */
 import classes.math.NVector;

public class MoarMath
{
	/**
	 * Returns the Logit, 1/(1 + e^-x)
	 */
	public static double logit (double x)
	{
		return(1.0/(1 + Math.exp(x * -1.0)));
	}
	/**
	 * Returns the sigmoid, what I previously thought was called the logit.
	 */
	 public static double sigmoid (double x)
	{
		return(1.0/(1 + Math.exp(x * -1.0)));
	}
	/**
	 * Returns the angle between two vectors [x1,y1] & [x2,y2]; they are taken as direction vectors.
	 */
	public static double angleBetween(double x1, double y1, double x2, double y2)
	{
		double dot = x1 * x2 + y1 * y2;
		return (Math.acos(dot/(Math.hypot(x1,y1)*Math.hypot(x2,y2))));
	}
	/**
	 * Rotates a given point about the origin.
	 * @param	a	the angle to rotate the point by.
	 */
	 public static Point rotate(Point p, double a)
	{
		return(new Point(p.getX() * Math.cos(a) + p.getY() * -1 * Math.sin(a), p.getX() * Math.sin(a) + p.getY() * Math.cos(a)));
	}
	/**
	 * Rotates a given point about the origin.
	 * @param	a	the angle to rotate the point by;
	 */
	 public static double[] rotate(double x, double y, double a)
	{
		return(new double[]{x * Math.cos(a) + y * -1 * Math.sin(a), x * Math.sin(a) + y * Math.cos(a)});
	}
	/**
	 * Calculates the intersection of two line segments.
	 * Based on methods described here: http://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
	 * p1 -> p2 & p3 -> p4
	 * @return	double[]	the point of intersection between the two lines, null if this does not occur. If they are overlapping, XY1 is returned.
	*/
	public static double[] lineSegmentIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4)
	{
		// Make sure the lines' bounds actually intersect
		if(x1 < x3 && x2 <x3 && x1 < x4 && x2 < x4 || y1 < y3 && y2 <y3 && y1 < y4 && y2 < y4)
			return null;

		//p & q = xy1 & xy3 respectively.
		double[] r = new double[]{x2-x1, y2-y1};
		double[] s = new double[]{x4-x3, y4-y3};
		double[] qMinusp = new double[]{x3-x1, y3-y1};
		double rsCross = r[0] * s[1] - r[1] * s[0];
		double qprCross = qMinusp[0] * r[1] - qMinusp[1] * r[0];
		
		if(rsCross == 0)// Some kinda parallel going on here..
			return null;
		
		double t = (qMinusp[0] * s[1] - qMinusp[1] * s[0])/rsCross;
		double u = qprCross / rsCross;
		if(t<= 1 && t >= 0 && u <= 1 && u >= 0)
			return (new double[]{x1 + t*r[0], y1 + t*r[1]});
		return null;
	}
	/**
	 * Returns the intersection point of two lines defined by two points on each line; calculation
	 * done using method shown here: https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
	 * Note that this method does not check for line skewness.
	 */
	public static double[] lineIntersectNoSkew(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4)
	{
		double x = ((x1*y2 - y1*x2)*(x3 - x4) - (x1 - x2)*(x3*y4 - y3*x4))/((x1 - x2)*(y3 - y4) - (y1 -y2)*(x3 - x4));
		double y = ((x1*y2 - y1*x2)*(y3 - y4) - (y1 - y2)*(x3*y4 - y3*x4))/((x1 - x2)*(y3 - y4) - (y1 -y2)*(x3 - x4));
		return (new double[]{x,y});
	}
	/**
	 * A skew-protected variant of lineIntersectNoSkew, returns null if the lines are parallel.
	 * @see	lineIntersectNoSkew
	 */
	public static double[] lineIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4)
	{
		if((x1 - x2)*(y3 - y4) - (y1 -y2)*(x3 - x4) != 0)
			return(lineIntersectNoSkew(x1,y1,x2,y2,x3,y3,x4,y4));
		else
			return null;
	}
	
	public static void main(String[] args)
	{
		System.out.println("\n    TEST FOR MOARMATH CLASS:\n");
		System.out.println("Logit of -5: " + logit(-5.0));
		System.out.println("Logit of 0:  " + logit(0.0));
		System.out.println("Logit of 5:  " + logit(5.0) + "\n");
		System.out.println("[1] Intersect point of line (-5, -1) -> (5,1) and (-5,0) -> (5,0):");
		double[] out = lineIntersectNoSkew(-5,-1,5,1,-5,0,5,0);
		System.out.println("("+out[0]+","+out[1]+")");
		System.out.println("[2] Intersect point of line (-5, -1) -> (5,1) and (-5,0) -> (5,0) using line segments:");
		out = lineSegmentIntersect(-5,-1,5,1,-5,0,5,0);
		System.out.println("("+out[0]+","+out[1]+")");
		out = lineSegmentIntersect(0,0,5,1,-5,0,5,0);
		System.out.println("[3] Intersect point of line (0,0) -> (5,1) and (-5,0) -> (5,0) using line segments:");
		System.out.println("("+out[0]+","+out[1]+")");
		System.out.println("[4] Intersect point of line (-4,0) -> (6,-2) and (-3,0) -> (5,1) using line segments:");
		System.out.println(lineSegmentIntersect(-4,0,6,-2,-3,0,5,1));
		System.out.println("[5] Intersect point of line (-5,0) -> (5,0) and (-5,-1) -> (5,-1) using line segments:");
		System.out.println( lineSegmentIntersect(-5,0,5,0,-5,-1,5,-1));
	}
}
