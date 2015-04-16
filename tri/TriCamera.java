package classes.tri;
public class TriCamera {
	
	public TriVector position, rotation, lens;
	
	public TriCamera (TriVector position, TriVector rotation, TriVector lens)
	{
		this.position = position;
		this.rotation = rotation;
		this.lens = lens;
	}
	public TriCamera (TriVector position, TriVector rotation)
	{
		this(position, rotation, new TriVector(0,0,1));
	}
	public TriCamera (TriVector position)
	{
		this(position, new TriVector());
	}
	
	public void renderTo(Graphics2D g)
	{
		
	}
}