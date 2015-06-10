package classes.graphics;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.*;

public class BufferedDrawingPanel extends JPanel{
	
	private BufferedImage buffer = null;

	public BufferedDrawingPanel (int w, int h)
	{
		super();
		buffer = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		((Graphics2D)g).drawImage(buffer,0,0,getWidth(),getHeight(),null);
		System.out.println("GOT HERE");
	}

	public BufferedImage getImageReference()
	{
		return buffer;
	}
}