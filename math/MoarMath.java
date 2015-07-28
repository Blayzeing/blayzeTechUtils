package classes.math;

/**
 * A useful class for storing more mathematical functions.
 * Written by Blayze Millward
 */

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
	/**
	 * Returns the intersection point of two lines (in vector form [x,y]+r[xdiff,ydiff])
	 */
	public static double[] vectorIntersect(double x1, double y1, double xdiff1, double ydiff1, double x2, double y2, double xdiff2, double ydiff2)
	{
		//TODO: Maths here that works out the intersect of the two lines
		return (new double[]{0,0});
	}
	
	public static void main(String[] args)
	{
		System.out.println("\n    TEST FOR MOARMATH CLASS:\n");
		System.out.println("Logit of -5: " + logit(-5.0));
		System.out.println("Logit of 0:  " + logit(0.0));
		System.out.println("Logit of 5:  " + logit(5.0) + "\n");
		System.out.println("Intersect point of line (-5, -1) -> (5,1) and (-5,0) -> (5,0):");
		double[] out = lineIntersectNoSkew(-5,-1,5,1,-5,0,5,0);
		System.out.println("("+out[0]+","+out[1]+")");
	}
}