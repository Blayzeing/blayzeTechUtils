package classes.tri;
import classes.math.NVector;
public class TriVector extends NVector {
	
	public TriVector(double x, double y, double z)
	{
		super(3);
	}
	public TriVector()
	{
		this(0,0,0);
	}
	
	// Useful wrapper functions, for old time's sake ^^
	public void setX(double x){setElement(0,x);}
	public void setY(double y){setElement(1,y);}
	public void setZ(double z){setElement(2,z);}
	public void set(double x, double y, double z)
	{
		setElement(0,x);
		setElement(1,y);
		setElement(2,z);
	}
	public double getX(){return getElement(0);}
	public double getY(){return getElement(1);}
	public double getZ(){return getElement(2);}
	public double[] get(){return toArray();}
}