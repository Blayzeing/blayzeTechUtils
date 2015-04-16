package classes.math;
import java.util.Arrays;

/**
 * A useful class for handling N-dimensional mathematical vectors, developed for use in neural networking by Blayze Millward.
 */

public class NVector
{
	private double[] elements;
	private int length;
	
	public NVector (int len)
	{
		elements = new double[len];
		length = len;
	}
	
	// GETTERS AND SETTERS
	/**
	 * Sets the specified element to the given value using zero-based index, providing the index is within range
	 */
	public void setElement(int index, double value)
	{
		if(index < length && index >= 0)
		{
			elements[index] = value;
		}
	}
	public double getElement(int index)
	{
		if(index < length && index >= 0)
		{
			return(elements[index]);
		}else{
			System.out.println("ERROR: An element index of " + String.valueOf(index) + " is out of bounds, returning NaN.");
			return(Double.NaN);
		}
	}
	public int getLength()
	{
		return(length);
	}
	public void setElementsTo(double[] values)
	{
		int len = (int)Math.min(values.length, elements.length);
		for(int i = 0; i<len; i++)
		{
			elements[i] = values[i];
		}
	}
	public void setAllElementsTo(double value)
	{
		for(int i = 0; i<elements.length; i++)
		{
			elements[i] = value;
		}
	}
	
	// INSTANCE METHODS
	public NVector add(NVector v)
	{
		NVector out = new NVector(length);
		for(int i = 0; i<length; i++)
		{
			out.setElement(i, elements[i] + v.getElement(i));
		}
		return(out);
	}
	public NVector subtract(NVector v)
	{
		NVector out = new NVector(length);
		for(int i = 0; i<length; i++)
		{
			out.setElement(i, elements[i] - v.getElement(i));
		}
		return(out);
	}
	public NVector scale(double s)
	{
		NVector out = new NVector(length);
		for(int i = 0; i<length; i++)
		{
			out.setElement(i, elements[i] * s);
		}
		return(out);
	}
	public double dot(NVector v)
	{
		double out = 0;
		for(int i = 0; i<length; i++)
		{
			out = out + elements[i] * v.getElement(i);
		}
		return(out);
	}
	public void randomize()
	{
		for(int i = 0; i<length; i++)
		{
			elements[i] = Math.random();
		}
	}
	public void randomize(double max)
	{
		for(int i = 0; i<length; i++)
		{
			elements[i] = Math.random() * max;
		}
	}
	public void randomize(double max, double min)
	{
		for(int i = 0; i<length; i++)
		{
			elements[i] = Math.random() * (max - min) + min;
		}
	}
	public double getMagnitude()
	{
		return(Math.sqrt(getMagnitudeSquared()));
	}
	public double getMagnitudeSquared()
	{
		double out = 0;
		for(int i = 0; i<length; i++)
		{
			out = out + elements[i] * elements[i];
		}
		return(out);
	}
	public NVector normalize()
	{
		NVector out = new NVector(length);
		double mag = this.getMagnitude();
		for(int i = 0; i<length; i++)
		{
			out.setElement(i, elements[i] / mag);
		}
		return(out);
	}

	// CLASS METHODS
	public static NVector add(NVector a, NVector b)
	{
		return(a.add(b));
	}
	public static NVector subtract(NVector a, NVector b)
	{
		return(a.subtract(b));
	}
	public static NVector scale(NVector a, double s)
	{
		return(a.scale(s));
	}
	public static double dot(NVector a, NVector b)
	{
		return(a.dot(b));
	}
	public static NVector normalize(NVector v)
	{
		return(v.normalize());
	}
	
	// UTILITIES
	/**
	 * Returns a deep copy of the NVector.
	 */
	public NVector copy()
	{
		NVector out = new NVector(length);
		for(int i = 0; i < length; i++)
		{
			out.setElement(i, this.getElement(i));
		}
		return(out);
	}
	public String toString()
	{
		return(Arrays.toString(elements));
	}
	/**
	 * Returns a deep copy of the NVector as an array.
	 */
	public double[] toArray()
	{
		return(elements.clone());
	}
	
	// TESTING
	public static void main(String[] args)
	{
		int ran = (int)Math.floor(Math.random() * 100);
		NVector a = new NVector(ran);
		NVector b = new NVector(ran);
		a.randomize(5,-5);
		b.randomize(5,-5);
		System.out.println("VECTOR CLASS FUNCTIONALITY TEST:\n");
		System.out.println("Vector A: \n" + a);
		System.out.println("Vector B: \n" + b);
		System.out.println("Sum (A + B):\n" + a.add(b));
		System.out.println("Subtract (A - B): \n" + a.subtract(b));
		double scalar = Math.random();
		System.out.println("Vector A scaled by " + String.valueOf(scalar) + ":");
		System.out.println(a.scale(scalar));
		System.out.println("Dot product: " + a.dot(b));
		NVector c = new NVector((int)Math.floor(Math.random() * 10));
		c.randomize(5,-5);
		System.out.println("Vector C: \n" + c);
		System.out.println("C's magnitude: " + c.getMagnitude());
		System.out.println("C's magnitude squared: " + c.getMagnitudeSquared());
		System.out.println("C normalized: \n" + c.normalize());
		System.out.println("C normalized's magnitude: " + c.normalize().getMagnitude());
	}
}
