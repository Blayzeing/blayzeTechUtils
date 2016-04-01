package classes.env;

import classes.math.StaticPoint;

public interface HitScannable {
	public abstract DistancedHit hitScan(double x1, double y1, double x2, double y2);
	//public abstract DistancedHit hitScan(Point p1, Point p2);
	public abstract DistancedHit hitScan(StaticPoint p1, StaticPoint p2);
}
