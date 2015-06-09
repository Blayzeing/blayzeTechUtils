package classes.graphics;

public class SimpleDisplay extends JFrame{
	
	private static int counter = 0;
	
	private DrawingPanel canvas;
	
	public SimpleDisplay(int w, int h)
	{
		setTitle("Display"+(counter>0?" " + counter : ""));
		
	}
}