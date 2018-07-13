package classes.env;

import classes.env.PolygonEntity;
import classes.math.Point;
import classes.graphics.SimpleDisplay;
import classes.math.StaticPoint;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Color;

/**
 * An environmental class that allows for the grouping of PolygonEntities.
 * Note: This class does not allow for rotation or scaling, that is implemented in TPolygonGroup.
 * Note: I don't think this class is used anymore. It's probably been superceded by the parentable objects.
 */
public class PolygonGroup extends AbstractEntity {

	private ArrayList<PolygonEntity> polygonEntities = new ArrayList<PolygonEntity>();
	private ArrayList<PolygonGroup> subgroups = new ArrayList<PolygonGroup>();

	public PolygonGroup (double x, double y)
	{
		super(x,y);
	}
	public PolygonGroup (double x, double y, AbstractEntity[]  entities)
	{
		super(x,y);
		this.addEntities(entities);
	}
	public PolygonGroup (double x, double y, ArrayList<AbstractEntity> entities)
	{
		super(x,y);
		this.addEntities(entities);
	}

	public DistancedHit hitScan (double x1, double y1, double x2, double y2)
	{
		return (new DistancedHit(false, 0, 0, 0));
	}
	public boolean contains (double x, double y)
	{
		return false;
	}
	public void draw (Graphics2D g)
	{
		for(PolygonEntity e:polygonEntities)
		{
			Point[] verts = e.getAllGlobalPointsAsArray();
			int len = verts.length;
			for(int i = 0; i<len; i++)
			{
				g.drawLine((int)(verts[i].getX()+this.getX()),(int)(verts[i].getY()+this.getY()), (int)(verts[(i+1)%len].getX()+this.getX()),(int)(verts[(i+1)%len].getY()+this.getY()));
			}
		}
		for(PolygonGroup pg:subgroups)
		{
			// We handle the subgroups here.
		}
	}

	public void addEntity(AbstractEntity e)
	{
		if(PolygonEntity.class.isInstance(e))//If the entity is a PolygonEntity, add it to the list of Polygons
			addPolygonEntity((PolygonEntity)e);
		else if(PolygonGroup.class.isInstance(e))//But if it's a PolygonGroup, add it to the list of subgroups
			addSubgroup((PolygonGroup)e);
		else// Finally if it's neither, then throw up a warning
			System.out.println("WARNING: Attempt to add non-polygon, non-Group object to this group.");
	}
	public void addEntities(ArrayList<AbstractEntity> ents)
	{
		for(AbstractEntity e : ents)
			addEntity(e);
	}
	public void addEntities(AbstractEntity[] ents)
	{
		for(AbstractEntity e : ents)
			addEntity(e);
	}

	public void addSubgroup(PolygonGroup g)
	{
		if(g != this)
			subgroups.add(g);
		else
			System.out.println("WARNING: Attempt to add this group to itself averted:\n" + this);
	}

	public void addPolygonEntity(PolygonEntity e)
	{
		polygonEntities.add(e);
	}
	public void addPolygonEntities(ArrayList<PolygonEntity> ents)
	{
		for(PolygonEntity e : ents)
			addEntity(e);
	}
	public void addPolygonEntities(PolygonEntity[] ents)
	{
		for(PolygonEntity e : ents)
			addEntity(e);
	}

	@Override
	public String toString()
	{
		return ("[Polygon Group] Object Count: "+(polygonEntities.size()+subgroups.size())+"\n'-> "+super.toString());
	}

	public static void main(String[] args) throws InterruptedException
	{
		SimpleDisplay d = new SimpleDisplay(300,300,"Drawing",true,true);
		Graphics2D g = d.getGraphics2D();
		// Create the group object
		PolygonGroup pg = new PolygonGroup(50,150);
		// Add an entity to it's center
		PolygonEntity p1 = new PolygonEntity(0,0, new StaticPoint[]{new StaticPoint(-2,-2), new StaticPoint(2,-2), new StaticPoint(2,2), new StaticPoint(-2,2)});
		pg.addEntity(p1);
		// Now add an entity to orbit this center
		PolygonEntity p2 = new PolygonEntity(30,0, new StaticPoint[]{new StaticPoint(0,-5), new StaticPoint(-5,5), new StaticPoint(5,5)});
		pg.addEntity(p2);
		g.setColor(Color.BLACK);
		pg.draw(g);
		double angle1 = 0, angle2 = 0;
		while(true)
		{
			angle1 += Math.PI/20;
			angle2 += Math.PI/50;
			pg.setX(150 + Math.cos(angle2)*100);
			pg.setY(150 + Math.sin(angle2)*100);
			p2.setX(Math.cos(angle1)*30);
			p2.setY(Math.sin(angle1)*30);
			d.fill(Color.WHITE);
			pg.draw(g);
			d.repaint();
			Thread.sleep(50);
		}
	}
}
