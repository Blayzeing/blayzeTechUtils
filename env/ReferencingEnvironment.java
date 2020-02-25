package blayzeTechUtils.env;

import blayzeTechUtils.env.Environment;

public class ReferencingEnvironment extends Environment {
	public ReferencingEnvironment()
	{
	}

	//This class should act like environment but hitScan methods return a ReferencedDistancedHit (RDHit?) that contains a reference to the entity hit
	// ReferencedDistancedHit also needs to be made -- Perhaps this shouldn't be overriding hitScan but instead be a new method?
	// 
	// This class should also have a new method called getEntitiesAt(x,y) that returns all entities that contain (x,y)
	// 
	// NOTE TO FUTURE BLAYZE: PERHAPS THIS SHOULD JUST BE IN THE ENVIRONMENT CLASS???
}
