package classes.agents;

public class Hit {
	private boolean contact = false;
	public double distance;
	
	public Hit(boolean madeContact, double distanceFromOrigin)
	{
		contact = madeContact;
		distance = distanceFromOrigin;
	}
	
	public boolean hadContact()
	{
		return contact;
	}
	public double distance()
	{
		return distance;
	}
}