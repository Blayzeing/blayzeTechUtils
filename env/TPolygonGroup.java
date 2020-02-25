package blayzeTechUtils.env;

/**
 * The same as PolygonGroup, only *transformable*.
 * TODO: Implement
 */
public class TPolygonGroup extends PolygonGroup implements Transformable {
	// Unimplemented classes:
	public TPolygonGroup(double x, double y){super(x,y);}
	public double getRotation(){return 0;}
	public double getXscale(){return 0;}
	public double getYscale(){return 0;}
	public void setRotation(double r){}
	public void setXscale(double x){}
	public void setYscale(double y){}
	public void resetTransformation(){}
	public void resetRotation(){}
	public void resetScale(){}
}
