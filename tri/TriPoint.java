package classes.tri;
import classes.math.NVector;
public class TriPoint extends NVector{
	
	public Color colour;
	
	public TriPoint(double x, double y, double z, Color c)
	{
		super(3);
		colour = c;
	}
	
	// Useful wrapper functions, for old time's sake ^^
	public void setX(double x){setElement(0,x);}
	public void setY(double y){setElement(1,y);}
	public void setZ(double z){setElement(2,z);}
	public double getX(){getElement(0);}
	public double getY(){getElement(1);}
	public double getZ(){getElement(2);}
}