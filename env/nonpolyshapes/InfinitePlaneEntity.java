package blayzeTechUtils.env.nonpolyshapes;

import blayzeTechUtils.env.AbstractEntity;
import blayzeTechUtils.env.DistancedHit;
import blayzeTechUtils.math.NVector;
import blayzeTechUtils.math.MoarMath;

import blayzeTechUtils.graphics.SimpleDisplay;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * An entity that is just a single infinitely-long, infinitely-thin plane.
 * Can be hit-test from either side, only points directly on the line
 * are considered "inside". For single-sided plane entities see (or make)
 * "OneSidedInfinitePlaneEntity".
 */


public class InfinitePlaneEntity extends AbstractEntity {

  private NVector heading = new NVector(new double[]{1,0}); // This must always be normalised
  private NVector normal = new NVector(new double[]{0,1});
  private boolean solid = false; // If this is "solid" then the half which the normal points away from is solid.

  // point + vector description
  public InfinitePlaneEntity (double x, double y, double xdiff, double ydiff, boolean solid)
  {
    super(x,y);
    this.setHeading(xdiff, ydiff);
    this.solid = solid;
  }
  public InfinitePlaneEntity (double x, double y, double xdiff, double ydiff)
  {
    this(x,y,xdiff,ydiff,false);
  }

  // point + slope description
  public InfinitePlaneEntity (double x, double y, double m)
  {
    this(x,y,1,m,false);
  }
  public InfinitePlaneEntity (double x, double y, double m, boolean solid)
  {
    this(x,y,1,m,solid);
  }

  public void setHeading (double xdiff, double ydiff)
  {
    heading.setElement(0, xdiff);
    heading.setElement(1, ydiff);
    heading = heading.normalize();
    normal.setElement(0, -heading.getY());
    normal.setElement(1, heading.getX());
  }

  public DistancedHit hitScan (double x1, double y1, double x2, double y2)
  {
    // TODO: There's an optimisation here, if we had "sidedness" be a defined part of 3 sub-classes
    //       of InfinitePlaneEntity then we wouldn't have to do this check. Of course, the sensible
    //       way to do this would be to implement a generic on this class that takes a sidedness
    //       but I'm not sure if that's sensible at this time.
    //       ...on the flip side, this method does let us flip it arbitrarily in-situe, and since
    //       this isn't a hyper-optimised library or anything maybe that's more useful?
    //

    
    // First we put everything into the frame of the the plane by subtracting the ray start from the 
    // plane's center

    NVector rayStart = new NVector(new double[]{x1-this.x, y1-this.y});
    NVector rayEnd = new NVector(new double[]{x2-this.x, y2-this.y});
    NVector rayDir = rayEnd.subtract(rayStart);

    // Project the ray against the normal
    double rayStartN = rayStart.dot(this.normal);
    double rayDirN = rayDir.dot(this.normal);

    // Check the sidedness of the thing - basically if the normal is positive and ray start is to etc
    if((this.solid && rayStartN >= 0) || rayStartN == 0)
      return new DistancedHit(true, x1, y1, 0); // The ray starts in the object (either below if solid or in the line)

    // If the ray is parallel to the plane or pointing away from it, then just return the whole thing
    double rayLen = rayDir.getMagnitude();
    if(rayDirN == 0 || (rayStartN>0 && rayDirN>0) || (rayStartN<0 && rayDirN<0))
      return new DistancedHit(false, x2, y2, rayLen);

    //// Finally, in this case the ray is not parallel and pointing toward the plane
    // ...it's either too far from the plane:
    double multiplierToPlane = rayStartN/(-rayDirN);
    if(multiplierToPlane > 1)
      return new DistancedHit(false, x2, y2, rayLen);
    
    // ... or, it's cloes enough to hit:
    NVector newDir = rayDir.scale(multiplierToPlane);
    return new DistancedHit(true, x1 + newDir.getX(), y1 + newDir.getY(), rayLen*multiplierToPlane);
  }

  public String toString()
  {
    return("[Infinite Plane Entity] heading vector: " + heading + " solid?: " + (solid?"yes":"no") + "\n'-> " + super.toString());
  }

  public void draw(Graphics2D g)
  {
    // Urgh. we basically need to draw only the bits within the draw window.
    // Eh. We'll just draw the point and arrow
    int r = 5;
    int l = 20;
    Color bc = g.getColor();
    g.setColor(Color.BLACK);
    g.drawOval((int)(this.getX()-r), (int)(this.getY()-r), (int)r*2, (int)r*2);// Draw center
    g.drawLine((int)this.getX(), (int)this.getY(), (int)(this.getX() + l*this.heading.getX()), (int)(this.getY() + l*this.heading.getY()));
    g.setColor(bc);
  }

	public static void main(String[] args) throws InterruptedException
	{
    InfinitePlaneEntity ip = new InfinitePlaneEntity(100,100, 0.75, true);
    System.out.println(ip);
		SimpleDisplay d = new SimpleDisplay(550,400, true, true);
		Graphics2D g = d.getGraphics2D();
		ip.draw(g);
		d.repaint();
		g.setColor(Color.RED);

    //double startx,starty,endx,endy;
    //boolean hoz = true;
    //for(int i = 0; i<550; i+=4)
    //{
    //  if(hoz)
    //  {
    //    startx = i;
    //    starty = 300;
    //    endy = 0;
    //    endx = i+40;
    //  }else{
    //    startx = 0;
    //    starty = i;
    //    endy = i;
    //    endx = 400;
    //  }
    //  DistancedHit out = ip.hitScan(startx, starty, endx, endy);
    //  g.drawLine((int)startx, (int)starty, (int)out.getX(), (int)out.getY());
    //}
    //d.repaint();
		
		while(true)
		{
			Thread.sleep(10);
			double startx = Math.random() * 550;
			double starty = Math.random() * 400;
			double angle = Math.random() * Math.PI * 2 - Math.PI;
			double endy = starty + Math.sin(angle) * 500;
			double endx = startx + Math.cos(angle) * 500;
			DistancedHit out = ip.hitScan(startx, starty, endx, endy);
			g.drawLine((int)startx, (int)starty, (int)out.getX(), (int)out.getY());
			d.repaint();
		}
	}
}
