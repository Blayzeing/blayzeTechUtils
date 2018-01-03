package classes.blayzeNet.neurons;

import classes.math.*;

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
	
	public abstract void learn (double inputs[], double target);

	// GETTERS AND SETTERS
	public void setLearningRate(double r)
	{
		if(r<=0)
		{
			System.out.println("WARNING: " + r + " is below zero. The learning rate will be set to 0.1.");
			learningRate = 0.1;
		}else{
			learningRate = r;
		}
	}
	public double getLearningRate()
	{
		return(learningRate);
	}
	//public abstract void setInput (int 
}
