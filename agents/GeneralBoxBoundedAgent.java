package classes.agents;

/**
 * A general class to construct an agent from that is based on a box-bounded entity.
 * Should be able to be created with a width, height and list of node points to act as sensors
 * Should have a 'duplicate' method that allows one to be made and defined, then duplicated many times.
 */

public class GeneralBoxBoundedAgent extends BoxBoundedEntity {
	
	/**
	 * An array list to contain the sensor definitions, in the from of [startx,starty, endx, endy].
	 */
	public ArrayList<double[]> sensors;
	
}