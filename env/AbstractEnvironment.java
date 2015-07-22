package classes.env;

import classes.env.DistancedHit;
import classes.math.StaticPoint;

public abstract class AbstractEnvironment {
		
		public ArrayList<AbstractEntity> entities = new ArrayList<AbstractEntity>();
		
		public DistancedHit hitScan(double x1, double y1, double x2, double y2)
		{
			DistancedHit closest = new DistancedHit(false, x2, y2, Math.hypot(x1-x2,y1-y2));
			for(AbstractEntity e : entities)
			{
				DistancedHit h = e.hitScan(x1,y1,x2,y2);
				if(h.getDistance() > closest.getDistance())
					closest = h;
			}
			return closest;
		}
		public double DistanceMeasure(double x1, double y1, double x2, double y2)
		{
			double closest = Math.POSITIVE_INFINITY;
			for(AbstractEntity e : entities)
			{
				DistancedHit h = e.hitScan(x1,y1,x2,y2);
				if(h.hadContact())
					closest = Math.min(closest, h.getDistance());
			}
			return closest;
		}
		public double DistanceMeasure(StaticPoint p1, StaticPoint p2)
		{
			return DistanceMeasure(p1.getX(), p1.getY(), p2.getX(), p2.getY());
		}
}