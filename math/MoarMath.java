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
	
	public static void main (String[] args)
	{
		System.out.println("\n    TEST FOR MOARMATH CLASS:\n");
		System.out.println("Logit of -5: " + logit(-5.0));
		System.out.println("Logit of 0:  " + logit(0.0));
		System.out.println("Logit of 5:  " + logit(5.0));
	}
}