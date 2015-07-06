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
	 * Returns the angle between two vectors [x1,y1] & [x2,y2]; they are taken as direction vectors.
	 */
	public static double angleBetween(double x1, double y1, double x2, double y2)
	{
		double dot = x1 * x2 + y1 * y2;
		return (Math.acos(dot/(Math.hypot(x1,y1)*Math.hypot(x2,y2))));
	}
	/**
	 * Returns the intersection point of two lines (in vector form [x,y]+r[xdiff,ydiff])
	 */
	public static double[] vectorIntersect(double x1, double y1, double xdiff1, double ydiff1, double x2, double y2, double xdiff2, double ydiff2)
	{
		//TODO: Maths here that works out the interesect of the two lines
		return (new double[]{0,0});
	}
	
	public static void main (String[] args)
	{
		System.out.println("\n    TEST FOR MOARMATH CLASS:\n");
		System.out.println("Logit of -5: " + logit(-5.0));
		System.out.println("Logit of 0:  " + logit(0.0));
		System.out.println("Logit of 5:  " + logit(5.0));
	}
}