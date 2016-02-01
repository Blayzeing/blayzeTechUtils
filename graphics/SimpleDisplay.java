package classes.graphics;
import javax.swing.JFrame;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.awt.*;

public class SimpleDisplay extends JFrame{
	
	private static int counter = 0;
	
	private BufferedDrawingPanel canvas;
	private int width, height;

	public SimpleDisplay(int w, int h, String title)
	{
		counter ++;
		width = w;
		height = h;
		setTitle(title);
		pack();
		Insets insets = getInsets();
		setSize(w+insets.left+insets.right, h+insets.top+insets.bottom);
		canvas = new BufferedDrawingPanel(w,h);
		getContentPane().add(canvas, "Center");
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
	}
	public SimpleDisplay(int w, int h)
	{
		this(w,h,"Display"+(counter>0?" " + counter : ""));
	}
	public SimpleDisplay()
	{
		this(400,400, true);
	}
	public SimpleDisplay(int w, int h, boolean closeFlag)
	{
		this(w,h);
		if(closeFlag)
			setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public SimpleDisplay(int w, int h, String title, boolean closeFlag)
	{
		this(w,h,title);
		if(closeFlag)
			setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public SimpleDisplay(int w, int h, boolean closeFlag, boolean resizeFlag)
	{
		this(w,h);
		if(closeFlag)
			setDefaultCloseOperation(EXIT_ON_CLOSE);
		if(resizeFlag)
			setResizable(true);
	}
	public SimpleDisplay(int w, int h, String title, boolean closeFlag, boolean resizeFlag)
	{
		this(w,h,title);
		if(closeFlag)
			setDefaultCloseOperation(EXIT_ON_CLOSE);
		if(resizeFlag)
			setResizable(true);
	}

	public Graphics2D getGraphics2D()
	{
		return (Graphics2D)canvas.getImageReference().createGraphics();
	}
	public void fill(Color c)
	{
		Graphics2D g = getGraphics2D();
		g.setColor(c);
		g.fillRect(0,0,width, height);
	}
	public void drawGrid(int spacing, Color lineColor)
	{
		Graphics2D g = getGraphics2D();
		Color oldColor = g.getColor();
		g.setStroke(new BasicStroke(2));
		g.setColor(lineColor);
		for(int i = 1; i < width/spacing+1; i++)
			g.drawLine(i*spacing, 0, i*spacing, height);
		for(int i = 1; i < height/spacing+1; i++)
			g.drawLine(0, i*spacing, width, i*spacing);
		g.setColor(oldColor);
		g.setStroke(new BasicStroke(1));
	}
	public void drawGrid(int spacing)
	{
		drawGrid(spacing, Color.BLACK);
	}

	public static void main(String[] args)
	{
		SimpleDisplay d = new SimpleDisplay();
		Graphics2D g = d.getGraphics2D();
		
		g.setColor(Color.white);
		g.fillRect(0,0,400,400);
	}
}
