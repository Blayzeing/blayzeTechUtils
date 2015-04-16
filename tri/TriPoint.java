package classes.tri;
import classes.math.NVector;
import java.awt.Color;
public class TriPoint extends TriVector{
	
	public Color colour;
	
	public TriPoint(double x, double y, double z, Color c)
	{
		super(x,y,z);
		colour = c;
	}
	public TriPoint(TriVector v, Color c)
	{
		this(v.getX(), v.getY(), v.getZ(), c);
	}
}