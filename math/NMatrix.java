package classes.math;
public class NMatrix {
	
	private double[][] elements;// [row][column]
	private int rows, columns;
	
	public NMatrix (int rows, int columns)
	{
		elements = new double[rows][columns];
		this.rows = rows;
		this.columns = columns;
	}
	
	// Getters
	public double getElement (int i, int j)
	{
		if(i>= 0 && i <=rows && j>= 0 && j<=columns)
		{
			return(elements[i][j]);
		}else{
			System.out.println("ERROR: An element index of [" + i + "," + j + "] is out of bounds, returning NaN.");
			return(Double.NaN);
		}
	}
	
	// Setters
	public void setElement (int i, int j, double value)
	{
		if(i>= 0 && i <=rows && j>= 0 && j<=columns)
		{
			elements[i][j] = value;
		}
	}
	
	public void setIdentity ()
	{
		if(rows != columns) return();
		for(int i = 0; i<rows; i++)
			elements[i][i] = 1; 
	}
	public void setZero ()
	{
		for(int i = 0; i<rows; i++)
		{
			for(int o = 0; o<columns; o++)
			{
				elements[i][j] = 0;
			}
		}
	}

	public static NMatrix getIdentity ()
	{
		NMatrix m = new NMatrix();
		m.setIdentity();
		return m;
	}
	public static NMatrix getZero ()
	{
		NMatrix m = new NMatrix();
		return (new NMatrix());
	}	
	public void 
}
