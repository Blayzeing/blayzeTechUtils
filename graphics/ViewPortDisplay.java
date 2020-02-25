package blayzeTechUtils.graphics;

import blayzeTechUtils.math.Point;

public class ViewPortDisplay extends SimpleDisplay {

	public Point position = new Point(0,0);
	private double scale = 1;

	public ViewPortDisplay(int w, int h, String title)
	{
		super(w,h,title);
	}
	public ViewPortDisplay(int w, int h)
	{
		super(w,h);
	}
	public ViewPortDisplay()
	{
		super();
	}
	public ViewPortDisplay(int w, int h, boolean closeFlag)
	{
		super(w,h,closeFlag);
	}
	public ViewPortDisplay(int w, int h, boolean closeFlag, boolean resizeFlag)
	{
		super(w,h,closeFlag,resizeFlag);
	}
	public ViewPortDisplay(int w, int h, String title, boolean closeFlag, boolean resizeFlag)
	{
		super(w,h,title,closeFlag,resizeFlag);
	}

}
