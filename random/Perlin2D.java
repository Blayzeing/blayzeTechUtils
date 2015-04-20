package classes.random;
import classes.math.*;
import java.util.Random;
import java.util.Hashtable;

/**
 * An implementation of Perlin noise, as per wikipedia ( http://en.wikipedia.org/wiki/Perlin_noise ), Angel code ( http://www.angelcode.com/dev/perlin/perlin.html ),
 * and this paper by Ken Perlin: http://mrl.nyu.edu/~perlin/paper445.pdf (also worth a read for improving smoothness: http://webstaff.itn.liu.se/~stegu/TNM022-2005/perlinnoiselinks/perlin-noise-math-faq.html ).
 * This implementation was written by Blayze Millward, and is intended for educational purposes, please contact Blayze if you intend on using this outside of education ( http://squaresanimations.co.uk/pages/contact.html ).
 *
 * Changes to the algorithm outlined by Wikipedia include:
 * - Use of a seed and hashing to generate weight vectors on-the-fly, rather than in a grid in order to allow for 'infinite' generation
 * - Use of a Hashtable to store generated weight vectors based on their hashed seed.
 * - This implmentation uses a self-made NVector class in order to do vector mathematics.
 */

public class Perlin2D
{
	private long seed;
	private Hashtable<Long,NVector> weights = new Hashtable<Long,NVector>();// hash table to store already generated NVectors for optimisation
	
	public Perlin2D (long seed)
	{
		this.seed = seed;
	}
	public Perlin2D ()
	{
		this(new Random().nextLong());
	}
	
	/**
	 * Gets the noise at the specified x and y coordinates - remember this is raw, so gradients can only be seen when looking between integers
	 * @param x	The x-coordinate you want to find the noise at.
	 * @param y	The y-coordinate you want to find the noise at.
	 * @return	A noise at the specified coordinate, as a double between -1 and 1.
	 */
	public double getNoiseAt(double x, double y)
	{
		 // Determine grid cell coordinates
		 int x0 = (x > 0.0 ? (int)x : (int)x - 1);
		 int x1 = x0 + 1;
		 int y0 = (y > 0.0 ? (int)y : (int)y - 1);
		 int y1 = y0 + 1;
	 
		 // Determine interpolation weights
		 // Could also use higher order polynomial/s-curve here
		 double sx = x - (double)x0;
		 double sy = y - (double)y0;
	 
		 // Interpolate between grid point gradients
		 
		 double n0, n1, ix0, ix1, value;
		 n0 = dotGridGradient(x0, y0, x, y);
		 n1 = dotGridGradient(x1, y0, x, y);
		 ix0 = lerp(n0, n1, sx);// (3 - 2*sx)*sx*sx);// Apply cubic interpolation (Uncomment and replace with sx for potentially quadratic?
		 n0 = dotGridGradient(x0, y1, x, y);
		 n1 = dotGridGradient(x1, y1, x, y);
		 ix1 = lerp(n0, n1, sx);//  (3 - 2*sx)*sx*sx);// Apply cubic interpolation
		 value = lerp(ix0, ix1, sy);//  (3 - 2*sy)*sy*sy);// Apply cubic interpolation
	 
		 //return value;
		 return(value);
	}
	private double dotGridGradient(int ix, int iy, double x, double y)
	{
		// Compute the distance vector
		NVector distance = new NVector(2);
		distance.setElement(0, x - (double)ix);
		distance.setElement(1, y - (double)iy);
		
		// Generate a new random vector using the seed as a start point and adding a (fairly) unique number to get to a new seed to generate based on.
		long hash = positionHash(ix,iy);
		NVector rw = weights.get(hash);//See if the NVector has already been made, if it has no need to make again or make a new random object.
		if(rw == null)
		{
			rw = new NVector(2);
			Random ran = new Random(hash);// Generate a new set of random numbers based on the has.
			rw.setElement(0,ran.nextDouble() * 2 - 1);
			rw.setElement(1,ran.nextDouble() * 2 - 1);
			rw.normalize();
			weights.put(hash, rw);
		}
		 
		 // Compute the dot-product
		 return (distance.dot(rw));
	}
	private long positionHash(int x, int y)
	{
		// Returning long as opposed to int here for EVEN MORE RANDOMNESS.
		// Results in a longer calculation of getNoiseAt() by ~2.5x10^-5ms
		return (Hashing.hash64shift(seed+Hashing.hash32shift(x+Hashing.hash32shift(y))));
	}
	
	// UTILITY METHODS
	public String toString()
	{
		String charGradient = "-+*s?S$%@#";
		String out = "";
		for(int i = 0; i<30; i++)
		{
			out = out + ' ';
			for(int o = 0; o<70; o++)
			{
				char tile = charGradient.charAt((int)Math.floor(((this.getNoiseAt(i * 0.12, o * 0.12) + 1)/2.0) * 10));
				out = out + tile;
			}
			out = out + "\n";
		}
		return(out);
	}
	
	// STATIC METHODS
	private static double lerp (double a, double b,  double w)
	{
		return((1.0-(3 - 2*w)*w*w) * a + (3 - 2*w)*w*w * b);
	}
	
	public static void main (String[] args)
	{
		System.out.println("\n    SeededPerlin2D Class test:\n\n");
		Perlin2D p = new Perlin2D();
		System.out.println(p);
	}
}
