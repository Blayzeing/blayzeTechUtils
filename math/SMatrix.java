package classes.math;
/**
 * A matrix class for square matrices
 */
public class SMatrix extends NMMatrix{
	
	public SMatrix (int size)
	{
		super(size,size);
	}
	
	public void setIdentity ()
	{
		if(rows != columns) return();
		for(int i = 0; i<rows; i++)
			elements[i][i] = 1; 
	}

	public static NMatrix getIdentity ()
	{
		NMatrix m = new NMatrix();
		m.setIdentity();
		return m;
	}
}
