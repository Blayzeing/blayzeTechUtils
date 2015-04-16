package classes.random;
import classes.random.Perlin2D;

/**
 * Extends the capabilities of the Perlin2D class to add easy scaling both vertically and horizontally.
 * Written by Blayze Millward
 */

public class FlexiPerlin2D extends Perlin2D
{
	private double size;
	private double intensity = 1.0;
	
	public FlexiPerlin2D (double sizeScale, double intensityScale, long seed)
	{
		super(seed);
		this.size = sizeScale;
		this.intensity = intensityScale;
	}
	public FlexiPerlin2D (double sizeScale, double intensityScale)
	{
		super();
		this.size = sizeScale;
		this.intensity = intensityScale;
	}
	public FlexiPerlin2D (double sizeScale)
	{
		super();
		this.size = sizeScale;
		this.intensity = 1.0;
	}
	
	@Override
	public double getNoiseAt(double x, double y)
	{
		return(super.getNoiseAt(x * size, y * size) * intensity);
	}
}