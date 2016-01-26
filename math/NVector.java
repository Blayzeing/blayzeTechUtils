package classes.math;
import java.util.Arrays;
import classes.math.NMatrix;

/**
 * A useful class for handling N-dimensional mathematical vectors, developed for use in neural networking by Blayze Millward.
 */

public class NVector
{
	private double[] elements;
	private int length;
	
	public NVector (double[] elements)
	{
		this.elements = elements;
		length = elements.length;
	}
	public NVector (int len)
	{
		this(new double[len]);
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
	public void randomize(double max, double min)
	{
		for(int i = 0; i<length; i++)
		{
			elements[i] = Math.random() * (max - min) + min;
		}
	}
	public void randomize(double max)
	{
		randomize(max,0.0);
	}
	public void randomize()
	{
		randomize(1.0,0.0);
	}
	public void randomizeInt(int max, int min)
	{
		for(int i = 0; i<length; i++)
		{
			elements[i] = Math.round(Math.random() * (max - min) + min);
		}
	}
	public void randomizeInt(int max)
	{
		randomize(max,0);
	}
	public void randomizeInt()
	{
		randomize(1,0);
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
	public NMatrix toHozMatrix()
	{
		return (new NMatrix(new double[][]{this.toArray()}));
	}
	public NMatrix toVertMatrix()
	{
		double[][] matrixContents = new double[elements.length][1];
		for (int i = 0; i<elements.length; i++)
			matrixContents[i] = new double[]{elements[i]};
		return (new NMatrix(matrixContents));
	}
	public boolean equals(Object obj)
	{
		if (obj == null || getClass() != obj.getClass() || ((NVector)obj).getLength() != getLength()) {
			return false;
		}
		for(int i = 0; i < length; i++)
			if(elements[i] != ((NVector)obj).getElement(i))
				return false;
		return true;
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
	public static NMatrix toHozMatrix(NVector v)
	{
		return(v.toHozMatrix());
	}
	public static NMatrix toVertMatrix(NVector v)
	{
		return(v.toVertMatrix());
	}
	public static boolean equals(NVector a, NVector b)
	{
		return (a.equals(b));
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
		a.randomizeInt(5,-5);
		b.randomizeInt(5,-5);
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
		c.randomizeInt(5,-5);
		System.out.println("Vector C: \n" + c);
		System.out.println("C's magnitude: " + c.getMagnitude());
		System.out.println("C's magnitude squared: " + c.getMagnitudeSquared());
		System.out.println("C normalized: \n" + c.normalize());
		System.out.println("C normalized's magnitude: " + c.normalize().getMagnitude());
		NVector d = new NVector(4);
		d.setElementsTo(new double[]{1,2,3,4});
		NVector e = new NVector(4);
		e.setElementsTo(new double[]{1,2,3,4});
		System.out.println("Vector D and E:\n" + c + "\n"  + d);
		System.out.println("D.equals(E): " + d.equals(e));
		System.out.println("E.equals(D): " + e.equals(d));
		System.out.println("D.equals(a): " + d.equals(a));
		System.out.println("D.equals(null): " + d.equals(null));
	}
}
