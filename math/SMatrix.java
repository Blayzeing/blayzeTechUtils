package classes.math;
/**
 * A matrix class for square matrices
 */
public class SMatrix extends NMMatrix{
	
	public SMatrix (int size)
	{
		super(size,size);
	}
	
	// FUNCTIONS -- DYNAMIC
	public void setIdentity ()
	{
		zero();
		for(int i = 0; i<width; i++)
			elements[i][i] = 1;
	}
	public static NMMatrix getIdentity ()
	{
		NMMatrix m = new NMMatrix();
		m.setIdentity();
		return m;
	}

	// FUNCTIONS -- STATIC
}
