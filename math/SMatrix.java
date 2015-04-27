package classes.math;
/**
 * A matrix class for square matrices
 */
public class SMatrix extends NMMatrix{
	
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
		SMatrix m = new SMatrix(width);
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
			SMatrix subMatrix = new SMatrix(size-1);
			
		}
	}

	// FUNCTIONS -- STATIC
}
