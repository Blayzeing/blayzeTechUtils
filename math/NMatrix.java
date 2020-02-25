package blayzeTechUtils.math;
import java.util.Arrays;
import blayzeTechUtils.math.NVector;
/**
 * A Matrix class for N-by-M matrices
 */

public class NMatrix {
	
	protected double[][] elements;// Using protected for speed in SMatrix
	private int width, height;
	
	public NMatrix (double[][] elements)
	{
		if(checkSensible(elements))
		{
			this.elements = elements;
			height = elements.length;
			width = elements[0].length;
		}
	}
	public NMatrix (int width, int height)
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
	public NMatrix getSubMatrix (int x, int y, int width, int height)//TODO: TEST
	{
		if(x+width > this.width)
			width = this.width - x;
		if(y+height > this.height)
			height = this.height - y;
		NMatrix out = new NMatrix(width,height);
		for(int i = x; i<x+width; i++)
			for(int o = y; o<y+height; i++)
			{
				out.setElement(i-x, o-y, elements[o][i]);
			}
		return out;
	}
	public NMatrix getSubMatrixFromPoints (int x1, int y1, int x2, int y2)//TODO: TEST
	{
		return(getSubMatrix(x1,y1,x2-x1,y2-y1));
	}
	
	// Setters
	/**
	 * Sets a particular element of the matrix to a given double value
	 * @param	x	int	The x-coordinate to set
	 * @param	y	int	The y-coordinate to set
	 * @param	value	double	The value to set the element to
	 */
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
	//		In-place methods
	/** Method to zero every element in-place */
	public void zero()
	{
		int len = width * height;
		for(int i = 0; i<len; i++)
			elements[(int)(i/height)][i%width] = 0;
	}
	/**
	 * Method to randomize every element in-place between max and min
	 * @param	max	double The maximum value
	 * @param	min double The minimum value
	 */
	public void randomize(double max, double min)
	{
		for(int i = 0; i<height; i++)
			for(int o = 0; o<width; o++)
				elements[i][o] = Math.random() * (max-min) + min;
	}
	/**
	 * Method to randomize every element in-place between max and 0
	 * @param	max	double The maximum value
	 */
	public void randomize(double max)
	{
		randomize(max,0.0);
	}
	/** Method to randomize every element in-place between 1 and 0 */
	public void randomize()
	{
		randomize(1.0);
	}
	/**
	 * Method to randomize every element in-place between max and min, rounded to the nearest int
	 * @param	max	double The maximum value
	 * @param	min double The minimum value
	 */
	public void randomizeInt(int max, int min)
	{
		for(int i = 0; i<height; i++)
			for(int o = 0; o<width; o++)
				elements[i][o] = Math.round(Math.random() * (max-min) + min);
	}
	/**
	 * Method to randomize every element in-place between max and 0, rounded to the nearest int
	 * @param	max	double The maximum value
	 */
	public void randomizeInt(int max)
	{
		randomizeInt(max,0);
	}
	/** Method to randomize every element in-place between 1 and 0, rounded to the nearest int */
	public void randomizeInt()
	{
		randomizeInt(1);
	}
	
	//		Returning methods
	public NMatrix returnZeroed()
	{
		return (new NMatrix(width,height));
	}
	public NMatrix scale(double s)
	{
		NMatrix out = new NMatrix(width, height);
		for(int i = 0; i<height; i++)
			for(int o = 0; o<width; o++)
				out.setElement(o,i,elements[i][o] * s);
		return out;
	}
	public boolean isSameSize(NMatrix m)
	{
		return (width == m.getWidth() && height == m.getHeight());
	}
	public boolean equals(NMatrix m)
	{
		if(!isSameSize(m))
			return false;
		for(int i = 0; i<height; i++)
			for(int o = 0; o<width; o++)
				if(m.getElement(o,i) != elements[i][o])
					return false;
		return true;
	}
	/**
	 * Returns a new NMatrix that is the summation of a given matrix and this.
	 * @return	NMatrix	The summation of the given matrix and this one. Null if not the same size.
	 */
	public NMatrix add(NMatrix m)
	{
		NMatrix out = new NMatrix(width, height);
		if(isSameSize(m))
		{
			for(int i = 0; i<height; i++)
				for(int o = 0; o<width; o++)
					out.setElement(o,i,elements[i][o] + m.getElement(o,i));
		}else{
			return null;
		}
		return out;
	}
	/**
	 * Returns a new NMatrix that is the subtraction of a given matrix and this.
	 * @return	NMatrix	This matrix minus the given. Null if not the same size.
	 */
	public NMatrix subtract(NMatrix m)
	{
		NMatrix out = new NMatrix(width, height);
		if(isSameSize(m))
		{
			for(int i = 0; i<height; i++)
				for(int o = 0; o<width; o++)
					out.setElement(o,i,elements[i][o] - m.getElement(o,i));
		}else{
			return null;
		}
		return out;
	}
	/**
	 * Multiplies this matrix with a given matrix.
	 * Faster implementation than taking each row and column as an NVector and dot producting.
	 * @return	NMatrix This matrix multiplied by the given, null if the matricies are noncompatable.
	 */
	public NMatrix multiply(NMatrix m)
	{
		if(this.width != m.getHeight())
			return null;
		double[][] contents = new double[this.height][m.getWidth()];
		double sum;
		for(int i = 0; i<m.getWidth(); i++)
			for(int o = 0; o<this.height; o++)
			{
				sum = 0.0;
				for(int p = 0; p<this.width; p++)
					sum += elements[o][p] * m.getElement(i,p);
				contents[o][i] = sum;
			}
		return (new NMatrix(contents));
	}

	//	Static
	public static void zero (NMatrix m)
	{
		m.zero();
	}
	public static boolean isSameSize (NMatrix a, NMatrix b)
	{
		return(a.isSameSize(b));
	}
	public static boolean equals (NMatrix a, NMatrix b)
	{
		return(a.equals(b));
	}
	public static NMatrix scale (NMatrix m, double s)
	{
		return(m.copy().scale(s));
	}
	public static NMatrix add (NMatrix a, NMatrix b)
	{
		return(a.add(b));
	}
	public static NMatrix subtract (NMatrix a, NMatrix b)
	{
		return(a.subtract(b));
	}
	public static NMatrix multiply (NMatrix a, NMatrix b)
	{
		return(a.multiply(b));
	}
	
	// Utilities
	/**
	 * Returns a deep copy of this matrix
	 */
	public NMatrix copy()
	{
		return(new NMatrix(this.to2DArray()));
	}
	public String toString()
	{
		String out = "\n";
		for (double[] a : elements)
			out += Arrays.toString(a) + "\n";
		return out;
	}
	/** Returns this matrix in a format that can be copy/pasted into wolframAlpha */
	public String toStringWolfram()
	{
		String out = "\n[";
		for (int i = 0; i<elements.length; i++)
			out += Arrays.toString(elements[i]) + (i != elements.length-1 ? "," : "");
		out += "]";
		return out;
	}
	/** Returns a 2D array of the elements of this matrix (deep) */
	public double[][] to2DArray ()
	{
		double[][] out = new double[height][width];
		for (int i = 0; i<height; i++)
			out[i] = elements[i].clone();
		return out;
	}
	public SMatrix toSquare ()
	{
		int size = Math.min(elements.length, elements[0].length);
		SMatrix out = new SMatrix(size);
		out.setElements(elements);
		return (out);
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
		System.out.println("\nNMatrix Test (see source):\n");
		double[][] rectTest1 = {{1,2,3,4,5},
								{1,2,3,4},
								{1,2,3,4,5}};
		double[][] rectTest2 = {{1,2,3,4,5},
								{1,2,3,4,5},
								{1,2,3,4,5}};
		System.out.println("rectTest1 test: " + checkRect(rectTest1) + "  [should be false]");
		System.out.println("rectTest2 test: " + checkRect(rectTest2) + "   [should be true]");
		NMatrix m = new NMatrix(rectTest2);
		System.out.println("toString():" + m);
		System.out.println("Deep copy of Array:");
		System.out.println("Original's element's identity hash:   " + System.identityHashCode(m.elements));
		System.out.println("Original.to2DArray()'s identity hash: " + System.identityHashCode(m.to2DArray()));
		System.out.println("Deep copy of Matrix:");
		System.out.println("Original's identity hash: " + System.identityHashCode(m));
		System.out.println("Copy's identity hash:     " + System.identityHashCode(m.copy()));
		System.out.println("setElementsTo():");
		NMatrix a = new NMatrix(new double[][]{new double[]{8,3},new double[]{2,6}});
		NMatrix b = new NMatrix(new double[][]{new double[]{7,9}});
		System.out.println("Setting" + a + " with " + b + " gives");
		a.setElements(b.to2DArray());
		System.out.println(a);
		System.out.println("Setting" + a + " with " + m + " gives");
		a.setElements(m.to2DArray());
		System.out.println(a);
		System.out.println("Scaling" + a + "by 2:");
		a = a.scale(2);
		System.out.println(a);
		//TODO: randomize
		double[][] ca = {{5,7,2},{1,6,3}};
		double[][] da = {{1,2,9},{8,4,3}};
		NMatrix c = new NMatrix(ca);
		NMatrix d = new NMatrix(da);
		System.out.println("Getting leftmost columnn from" + c);
		System.out.println(c.getColumn(0));
		System.out.println("Getting topmost row...");
		System.out.println(c.getRow(0));
		System.out.println("Adding"+c+"and"+d+"equals"+c.add(d));
		System.out.println("\nMultiplication:");
		a = new NMatrix(new double[][]{new double[]{1,2,3}});
		b = new NMatrix(new double[][]{new double[]{2},new double[]{1},new double[]{2}});
		System.out.println(a + "multiplied by" + b);
		System.out.println("Equals: " + a.multiply(b));
		System.out.println(b + "multiplied by" + a);
		System.out.println("Equals: " + b.multiply(a));
	}
}
