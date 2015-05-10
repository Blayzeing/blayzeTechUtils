package classes.math;
/**
 * A matrix class for square matrices
 */
public class SMatrix extends NMatrix{
	
	private int size;
	
	public SMatrix (int size)
	{
		super(size,size);
		this.size = size;
	}
	
	// FUNCTIONS -- DYNAMIC
	public void setIdentity ()
	{
		zero();
		for(int i = 0; i<size; i++)
			elements[i][i] = 1;
	}
	public SMatrix getIdentity ()
	{
		SMatrix m = new SMatrix(getWidth());
		m.setIdentity();
		return m;
	}
	public double getDet()
	{
		// To break recursion
		if(size == 1)
			return elements[0][0];
		double output = 0;
		for(int i = 0; i<size; i++)
		{
			double multiplier = (i%2)*-2+1;
			int subSize = size-1;
			SMatrix subMatrix = new SMatrix(subSize);
			int subCol = 0;
			for(int col = 0; col<subSize; col++)
			{
				for(int o = 0; o<subSize; o++)
					subMatrix.setElement(col,o,elements[o+1][subCol]);
				if(col+1 == i)
					subCol += 2;
				else
					subCol++;
			}
			output = output + multiplier * subMatrix.getDet();
		}
		return output;
	}

	// FUNCTIONS -- STATIC
	
	// TEST RIG
	public static void main (String[] args)
	{
		SMatrix m = new SMatrix(2);
		m.setElements(new double[][]{new double[]{5,2},
					     new double[]{3,1}});
		System.out.println("Finding the determinant of m: " + m);
		System.out.println("Det(m) = " + m.getDet());
	}
}
