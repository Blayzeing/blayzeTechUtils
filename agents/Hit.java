package classes.agents;

public class Hit {
	private boolean contact = false;
	private double x,y;
	
	public Hit(boolean madeContact, double x, double y)
	{
		contact = madeContact;
		this.x = x;
		this.y = y;
	}
	
	public boolean hadContact()
	{
		return contact;
	}
}