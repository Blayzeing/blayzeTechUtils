package classes.agents;

public abstract class AbstractEnvironment {
		
		//public ArrayList<AbstractAgent> agents = new ArrayList<AbstractAgent>();
		public ArrayList<BoundedEntity> entities = new ArrayList<AbstractEntity>();
		
		public double distanceScan(double x1, double y1, double x2, double y2)
		{
			double closest = Math.POSITIVE_INFINITY;
			for(AbstractEntity e : entities)
			{
				Hit h = e.hitScan(x1,y1,x2,y2);
				if(h.hadContact())
					closest = Math.min(closest, h.getDistance());
			}
			return closest;
		}
		public double distanceScan(Point p1, Point p2)
		{
			return distanceScan(p1.getX(), p1.getY(), p2.getX(), p2.getY());
		}
}