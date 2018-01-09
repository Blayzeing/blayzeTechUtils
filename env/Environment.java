package classes.env;
import classes.env.*;
import classes.math.Point;
import classes.env.nonpolyshapes.*;
import classes.math.StaticPoint;
import java.awt.Graphics2D;
import java.util.*;
import classes.graphics.SimpleDisplay;
import java.awt.Color;

public class Environment implements HitScannable {
		
		public ArrayList<AbstractEntity> entities; //Perhaps use binary space partitioning to divide up this list for a faster load time on larger envs?

		public Environment (AbstractEntity[] e)
		{
			entities = new ArrayList<AbstractEntity>();
			for(AbstractEntity ent:e)
				entities.add(ent);
		}
		public Environment (ArrayList<AbstractEntity> e)
		{
			entities = e;
		}
		public Environment ()
		{
			this(new ArrayList<AbstractEntity>());
		}

		/**
		 * Performs a hitScan on this environment bar a list of entities to clip.
		 * Performs a hitScan on each element in this environment, and returns the shortest.
		 * @param	x1	the x-component of the start position of the ray
		 * @param	y1	the y-component of the start position of the ray
		 * @param	x2	the x-component of the end position of the ray
		 * @param	y2	the y-component of the end position of the ray
		 * @param	clip	a list of objects for the ray to clip through when scanning the environment
		 */
		public DistancedHit hitScan(double x1, double y1, double x2, double y2, ArrayList<AbstractEntity> clip)
		{
			//TODO: Finish this
			return (new DistancedHit(false, 0, 0, 0));
		}
		/**
		 * Performs a hitScan on this environment bar an Entity to clip.
		 * Performs a hitScan on each element in this environment, and returns the shortest.
		 * @param	x1	the x-component of the start position of the ray
		 * @param	y1	the y-component of the start position of the ray
		 * @param	x2	the x-component of the end position of the ray
		 * @param	y2	the y-component of the end position of the ray
		 */
		public DistancedHit hitScan(double x1, double y1, double x2, double y2, AbstractEntity clip)
		{
			DistancedHit closest = new DistancedHit(false, x2, y2, Math.hypot(x1-x2,y1-y2));
			for(AbstractEntity e : entities)
			{
				if(e != clip)//TODO: Perhaps make this use equals? Actually yes.(? maybe. I mean, what if two identical size on topo of each other)
				{
					DistancedHit h = e.hitScan(x1,y1,x2,y2);
					if(h.getDistance() < closest.getDistance())
						closest = h;
				}
			}
			return closest;
		}
		/**
		 * Performs a hitScan on this environment.
		 * Performs a hitScan on each element in this environment, and returns the shortest.
		 * @param	x1	the x-component of the start position of the ray
		 * @param	y1	the y-component of the start position of the ray
		 * @param	x2	the x-component of the end position of the ray
		 * @param	y2	the y-component of the end position of the ray
		 */
		public DistancedHit hitScan(double x1, double y1, double x2, double y2)
		{
			DistancedHit closest = new DistancedHit(false, x2, y2, Math.hypot(x1-x2,y1-y2));
			for(AbstractEntity e : entities)
			{
				DistancedHit h = e.hitScan(x1,y1,x2,y2);
				if(h.getDistance() < closest.getDistance())
					closest = h;
			}
			return closest;
		}
		/**
		 * Performs a hitScan on this environment.
		 * Performs a hitScan on each element in this environment, and returns the shortest.
		 * @param	p1	the start position of the ray
		 * @param	p2	the end position of the ray
		 */
		public DistancedHit hitScan(StaticPoint p1, StaticPoint p2)
		{
			return(hitScan(p1.getX(),p1.getY(),p2.getX(),p2.getY()));
		}

		/**
		 * Check if a point is within this environment.
		 * Performs a check to see if the given point is within any of this environment's children.
		 * @param	x	the x-position of the point.
		 * @param	y	the y-position of the point.
		 */
		public boolean contains(double x, double y)
		{
			int entSize = entities.size();
			for(int i = 0; i<entSize; i++)
				if(entities.get(i).contains(x,y))
					return true;
			return false;
		}
		/**
		 * Check if a point is within this environment.
		 * Performs a check to see if the given point is within any of this environment's children.
		 * @param	p	The point
		 */
		public boolean contains( StaticPoint p) { return contains(p.getX(), p.getY()); }
		public boolean contains(Point[] ps)
		{
			for(Point p : ps)
				if(contains(p))
					return true;
			return false;
		}
		public boolean contains(ArrayList<Point> ps)
		{
			for(Point p : ps)
				if(contains(p))
					return true;
			return false;
		}

		/**
		 * Returns an ArrayList of all entities that contain the given point.
		 */
		public ArrayList<AbstractEntity> getEntitiesAt(double x, double y)
		{
			ArrayList<AbstractEntity> ents = new ArrayList<AbstractEntity>();
			int entSize = entities.size();
			for(int i = 0; i<entSize; i++)
			{
				AbstractEntity thisEntity = entities.get(i);
				if(thisEntity.contains(x,y))
					ents.add(thisEntity);
			}
			return ents;
		}
		public ArrayList<AbstractEntity> getEntitiesAt(StaticPoint p)
		{
			return(getEntitiesAt(p.getX(), p.getY()));
		}
		// Might be worth making a 'getEntitiesAt' That takes a list of points

		/**
		 * Returns the first entity found at the given location, null if no entity found.
		 * This is faster than `getEntitiesAt`.
		 */
		public AbstractEntity getEntityAt(double x, double y)
		{
			int entSize = entities.size();
			for(int i = 0; i<entSize; i++)
			{
				AbstractEntity thisEntity = entities.get(i);
				if(thisEntity.contains(x,y))
					return(thisEntity);
			}
			return null;
		}
		public AbstractEntity getEntityAt(StaticPoint p)
		{
			return getEntityAt(p.getX(), p.getY());
		}

		/**
		 * Functions the same as hitScan but also contains a reference to the entity the scan hit.
		 */
		//public ReferencedDistancedHit referencedHitScan(double x1, double y1, double x2, double y2)
		
		public void draw(Graphics2D g)
		{
			for(AbstractEntity e : entities)
			{
				e.draw(g);
			}
		}

		public static void main(String[] args) throws InterruptedException
		{
			SimpleDisplay d = new SimpleDisplay(550,400,true, true);
			Graphics2D g = d.getGraphics2D();
			
			Environment env = new Environment();
			env.entities.add(new OuterBoxBoundedEntity(20,20,510, 360));
			env.entities.add(new BoxBoundedEntity(100, 300, 30, 80));
			env.entities.add(new CircleBoundedEntity(300, 150, 70));
			env.entities.add(new OuterCircleBoundedEntity(200,200,300));
			env.entities.add(new BoxBoundedEntity(460, 126, 200, 100));
			
			double number = Math.random() * 20;
			for(int i = 0; i<number; i++)
				env.entities.add(new BoxBoundedEntity(Math.random() * d.getWidth(), Math.random() * d.getHeight(), Math.random() * 200, Math.random() * 200));
			
			env.draw(g);
			d.repaint();
			g.setColor(Color.RED);
			
			double scanDist = 500;
			
			while(true)
			{
				Thread.sleep(10);
				//int c = (int)(Math.random() * 200);
				//g.setColor(new Color(c,c,c));
				double startx = Math.random() * d.getWidth();
				double starty = Math.random() * d.getHeight();
				double angle = Math.random() * Math.PI * 2 - Math.PI;
				double endy = starty + Math.sin(angle) * scanDist;
				double endx = startx + Math.cos(angle) * scanDist;
				DistancedHit out = env.hitScan(startx, starty, endx, endy);
				g.drawLine((int)startx, (int)starty, (int)out.getX(), (int)out.getY());
				d.repaint();
			}
		}
}
