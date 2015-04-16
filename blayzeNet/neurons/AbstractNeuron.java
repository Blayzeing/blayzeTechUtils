package classes.blayzeNet.neurons;

public abstract class AbstractNeuron {
	
	private NVector inputs;
	private NVector weights;
	private double learningRate;
	
	// LEARNING AND PREDICTING ALGORITHMS
	/**
	 * Returns       ____1____
	 *               1+ e^(-z)
	 * Where z = b + SUM(xy)
	 */
	public double predict ()
	{
		return(1.0/(1.0 + Math.exp(inputs.dot(weights) * -1)));
	}
	
	public abstract 
}