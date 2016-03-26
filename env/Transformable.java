package classes.env;

public interface Transformable {
	public abstract double getRotation();
	public abstract double getXscale();
	public abstract double getYscale();
	public abstract void setRotation(double r);
	public abstract void setXscale(double x);
	public abstract void setYscale(double y);
	public abstract void resetTransformation();
	public abstract void resetRotation();
	public abstract void resetScale();
}
