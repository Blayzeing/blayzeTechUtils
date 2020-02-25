package blayzeTechUtils.math;
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
	/**
	 * Returns the determinate of this matrix.
	 * @return	double	This matrix's determinate
	 */
	public double getDet()
	{
		// To break recursion
		if(size == 1)
			return elements[0][0];
		// Optimisation for 2x2 matrices:
		// Note: This optimisation has had little testing under limited environments.
		if(size == 2)
			return elements[0][0]*elements[1][1] - elements[0][1] * elements[1][0];
		double output = 0;
		for(int i = 0; i<size; i++)
		{
			double multiplier = (i%2)*-2+1;//calculate multiplier coefficient
			//Create new subMatrix
			int subSize = size-1;
			SMatrix subMatrix = new SMatrix(subSize);
			//Iterate through the subMatrix, getting variables from the superMatrix
			for(int row = 0; row<subSize; row++)
			{
				int subCol = 0;// Store a separate value to be able to skip over the current column.
				for(int col = 0; col<subSize; col++)
				{
					if(col == i)//Skip the column if it's at i (vertical skip)
						subCol ++;
					subMatrix.setElement(col,row,elements[row+1][subCol]);
					subCol++;
				}
			}
			output = output + multiplier * elements[0][i] * subMatrix.getDet();
		}
		return output;
	}
	/**
	 * Returns the inverse of any 2x2 matrix.
	 * This is optimised for 2x2 matrices.
	 * @return	SMatrix	Thhis matrix's inverse, null if no inverse is preasent.
	 */
	public SMatrix inverse2x2()
	{
		double mult = (elements[0][0]*elements[1][1] - elements[0][1] * elements[1][0]);
		if (mult == 0)
			return null;
		mult = 1.0/mult;
		NMatrix out = new NMatrix(new double[][]{new double[]{elements[1][1], -1 * elements[0][1]}, new double[]{-1 * elements[1][0], elements[0][0]}});
		return(out.scale(mult).toSquare());
	}

	// FUNCTIONS -- STATIC
	public static SMatrix identity(int size)
	{
		SMatrix out = new SMatrix(size);
		out.setIdentity();
		return out;
	}
	public static double getDet(SMatrix m)
	{
		return (m.getDet());
	}
	public static SMatrix inverse2x2(SMatrix m)
	{
		return (m.inverse2x2());
	}
	public SMatrix copy()
	{
		SMatrix out = new SMatrix(this.getWidth());
		out.setElements(this.to2DArray());
		return(out);
	}
	
	// TEST RIG
	public static void main (String[] args)
	{
		SMatrix m = new SMatrix(2);
		m.randomizeInt(9,0);
		System.out.println("Finding the determinant of matrix: " + m);
		System.out.println("Det(m) = " + m.getDet() + "\n");
		SMatrix m4 = new SMatrix(10);
		m4.randomizeInt(20,-20);
		System.out.println("Finding the determinant of matrix: " + m4);
		System.out.println("Det(m) = " + m4.getDet());
	}
}
