package blayzeTechUtils.env;

import blayzeTechUtils.env.*;
import blayzeTechUtils.math.*;

/**
 * A class for spawning various common polygon entities.
 */
public class PolygonEntities {

	public static PolygonEntity rectangle(double width, double height, boolean center)
	{
		double offsetX = center?width/2:0;
		double offsetY = center?height/2:0;
		return(new PolygonEntity(0,0,new StaticPoint[]{new StaticPoint(0-offsetX, 0-offsetY), new StaticPoint(width-offsetX, 0-offsetY),
					   		       new StaticPoint(width-offsetX, height-offsetY), new StaticPoint(0-offsetX, height-offsetY)}));
	}

	public static void addRectangleTo(PolygonEntity e, double width, double height, boolean center)
	{
		double offsetX = center?width/2:0;
		double offsetY = center?height/2:0;
		e.addPoints(new StaticPoint[]{new StaticPoint(0-offsetX, 0-offsetY), new StaticPoint(width-offsetX, 0-offsetY),
					      new StaticPoint(width-offsetX, height-offsetY), new StaticPoint(0-offsetX, height-offsetY)});
	}
}
