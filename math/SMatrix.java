package classes.math;
/**
 * A matrix class for square matrices
 */
public class SMatrix extends NMMatrix{
	
	public SMatrix (int size)
	{
		super(size,size);
	}
	public SMatrix (double[][] elements)
	{
		
	
	public void setIdentity ()
	{
		/*if(rows != columns) return();
		for(int i = 0; i<rows; i++)
			elements[i][i] = 1; */
	}

	public static NMMatrix getIdentity ()
	{
		NMMatrix m = new NMMatrix();
		m.setIdentity();
		return m;
	}
}
