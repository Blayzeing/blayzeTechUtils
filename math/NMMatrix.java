package classes.math;
import java.util.Arrays;
/**
 * A Matrix class for N-by-M matrices
 */

public class NMMatrix {
	
	private double[][] elements;
	private int width, height;
	
	public NMMatrix (double[][] elements)
	{
		if(checkSensible(elements))
		{
			this.elements = elements;
			height = elements.length;
			width = elements[0].length;
		}
	}
	public NMMatrix (int width, int height)
	{
		this(new double[height][width]);
	}
	
	// Getters
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public double getElement (int x, int y)
	{
		try{
			return(elements[y][x]);
		}catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("ERROR:\n"+e+"\n Returning Double.NaN");
			return(Double.NaN);
		}
	}
	public NVector getColumn (int c)
	{
		NVector v = new NVector(height);
		for(int i = 0; i<height; i++)
			v.setElement(i, elements[i][c]);
		return v;
	}
	public NVector getRow (int r)
	{
		return (new NVector(elements[r]));
	}
	
	// Setters
	public void setElement (int x, int y, double value)
	{
		try{
			elements[y][x] = value;
		}catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("WARNING:\n"+e);
		}
	}
	/**
	 * Sets the elements in this matrix to those of values, eg:
	 * a = [1,2]  b = [5]  c = [6,7,2]
	 *     [3,4]               [8,9,0]
	 *                         [1,7,3]
	 * a.setElements(b) = [5,2]
	 *                    [3,4]
	 *
	 * a.setElements(c) = [6,7]
	 *                    [8,9]
	 */
	public void setElements(double[][] values)
	{
		if(checkSensible(values))
		{
			int w = Math.min(width, values[0].length);
			int h = Math.min(height, values.length);
			for(int i = 0; i<w; i++)
				for(int o = 0; o<h; o++)
					setElement(i,o, values[o][i]);
		}
	}

	// Maths functions
	//	Standard (instance)
	public void zero()
	{
		int len = width * height;
		for(int i = 0; i<len; i++)
			elements[(int)(i/height)][i%width] = 0;
	}
	public NMMatrix returnZeroed()
	{
		return (new NMMatrix(width,height));
	}
	//	Static
	
	// Utilities
	/**
	 * Returns a deep copy of this matrix
	 */
	public NMMatrix copy()
	{
		return(new NMMatrix(this.to2DArray()));
	}
	public String toString()
	{
		String out = "\n";
		for (double[] a : elements)
			out += Arrays.toString(a) + "\n";
		return out;
	}
	/**
	 * Returns a 2D array of the elements of this matrix (deep)
	 */
	public double[][] to2DArray ()
	{
		double[][] out = new double[height][width];
		for (int i = 0; i<height; i++)
			out[i] = elements[i].clone();
		return out;
	}
	
	/**
	 * This method checks if the given array is rectangular
	 */
	private static boolean checkRect (double[][] array)
	{
		int size = array[0].length;
		for(int i = 0; i<array.length; i++)
			if (array[i].length != size)
				return false;
		return true;
	}
	private static boolean checkSensible(double[][] array)
	{
		return (array != null && array.length != 0 && array[0].length != 0 && checkRect(array));
	}
	
	public static void main (String[] args)
	{
		System.out.println("\nNMMatrix Test (see source):\n");
		double[][] rectTest1 = {{1,2,3,4,5},
								{1,2,3,4},
								{1,2,3,4,5}};
		double[][] rectTest2 = {{1,2,3,4,5},
								{1,2,3,4,5},
								{1,2,3,4,5}};
		System.out.println("rectTest1 test: " + checkRect(rectTest1) + "  [should be false]");
		System.out.println("rectTest2 test: " + checkRect(rectTest2) + "   [should be true]");
		NMMatrix m = new NMMatrix(rectTest2);
		System.out.println("toString():" + m);
		System.out.println("Deep copy of Array:");
		System.out.println("Original's element's identity hash:   " + System.identityHashCode(m.elements));
		System.out.println("Original.to2DArray()'s identity hash: " + System.identityHashCode(m.to2DArray()));
		System.out.println("Deep copy of Matrix:");
		System.out.println("Original's identity hash: " + System.identityHashCode(m));
		System.out.println("Copy's identity hash:     " + System.identityHashCode(m.copy()));
		System.out.println("setElementsTo():");
		NMMatrix a = new NMMatrix(new double[][]{new double[]{8,3},new double[]{2,6}});
		NMMatrix b = new NMMatrix(new double[][]{new double[]{7,9}});
		System.out.println("Setting" + a + " with " + b + " gives");
		a.setElements(b.to2DArray());
		System.out.println(a);
		System.out.println("Setting" + a + " with " + m + " gives");
		a.setElements(m.to2DArray());
		System.out.println(a);
	}
}