package blayzeTechUtils.env;

import blayzeTechUtils.math.StaticPoint;

public interface HitScannable {
  /**
   * Given two double-specified points performs a hitscan.
   * @return DistancedHit the distanced hit object created by this hitscan
   */
	public abstract DistancedHit hitScan(double x1, double y1, double x2, double y2);
	//public abstract DistancedHit hitScan(Point p1, Point p2);
  /**
   * Given two points performs a hitscan.
   * @return DistancedHit the distanced hit object created by this hitscan
   */
	public abstract DistancedHit hitScan(StaticPoint p1, StaticPoint p2);
}
