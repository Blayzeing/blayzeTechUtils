package classes.env;
import classes.env.*;
import classes.math.*;
import java.util.ArrayList;

public class TPolygonEntity extends PolygonEntity {
	//Below store the transformation information:
	private double rotation = 0;
	private double xScale = 1;
	private double yScale = 1;
	private SMatrix inverseTransform = SMatrix.identity(2), transform = SMatrix.identity(2);// This stores the actual transform matrcies

	public TPolygonEntity (double x, double y) { super(x,y); }
	public TPolygonEntity (double x, double y, StaticPoint[] p) { super(x,y,p); }
	public TPolygonEntity (double x, double y, ArrayList<StaticPoint> p) { super(x,y,p); }

	@Override
	public boolean contains (double x, double y)
	{
		//Use the inverseTransform to transform x and y to a position as if the normal contains would be occuring
		return (contains(x,y));
	}
}
