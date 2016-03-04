package classes.env;

package classes.env;

/**
 * A class for spawning various common polygon entities.
 */
public static class PolygonEntities {

	public static PolygonEntity rectangle(double width, double height, boolean center)
	{
		offsetX = center?width/2:0;
		offsetY = center?height/2:0;
		return(new PolygonEntity(new StaticPoint[]{new StaticPoint(0-offsetX, 0-offsetY), new StaticPoint(width-offsetX, 0-offsetY),
							   new StaticPoint(width-offsetX, height-offsetY), new StaticPoint(0-offsetX, height-offsetY)}));
	}
}
