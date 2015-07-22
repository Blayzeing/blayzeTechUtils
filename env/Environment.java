package classes.env;

import classes.env.AbstractEntity;
import classes.env.DistancedHit;
import classes.math.StaticPoint;
import java.awt.Graphics2D;

public class Environment {
		
		public ArrayList<AbstractEntity> entities; //Perhaps use binary space partitioning to divide up this list for a faster load time on larger envs?

		public Environment (ArrayList<AbstractEntity> e)
		{
			entities = e;
		}
		public Environment ()
		{
			this(new ArrayList<AbstractEntity>());
		}

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

		public void draw(Graphics2D g)
		{
			for(AbstractEntity e : entities)
			{
				e.draw(g);
			}
		}

		public static void main(String[] args)
		{
			Environment env = new Environment()
		}
}