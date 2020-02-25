package blayzeTechUtils.blayzeNet.neurons;
import blayzeTechUtils.math.NVector;
import java.util.Date;

/* This class differs from the LinearNeuron class as it uses a sigmoid function to wrap the input.
 * Another key difference is that this unit has a bias input to be learnt.
 */

public class RunnableLogisticNeuron implements Runnable
{
	
	private NVector inputs;
	private NVector weights;
	private double learningRate;
	
	// USED TO STORE DATA FOR MULTITHREADING:
	private double[][][] Sdata;// 'S' for Stored
	private int Srepeats;
	private long time;
	
	public RunnableLogisticNeuron (int inputCount, double learningRate)
	{
		inputs = new NVector(inputCount + 1);
		weights = new NVector(inputCount + 1);
		weights.randomize(1,-1);
		setLearningRate(learningRate);
		inputs.setElement(inputs.getLength()-1, 1);
	}
	public RunnableLogisticNeuron (int inputCount)
	{
		this(inputCount, 0.1);
	}
	
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
	
	/** 
	 * Trains this individual neuron based on a given set of inputs and a target output
	 */
	public void learn (double[] inputs, double target)
	{
		setInputs(inputs);
		double y = predict();
		int len = weights.getLength();
		for(int i = 0; i < len; i++)
		{
			double weightDelta = learningRate * this.inputs.getElement(i) * (target - y) * y * (1.0 - y);
			weights.setElement(i, weights.getElement(i) + weightDelta);
			
			//Check if the thread's interrupted.
			if(Thread.interrupted())
			{
				return;
			}
		}
	}
	/**
	 * This function now acts as a setup function to prepare for the run() function being called.
	 */
	public void setupLearnArray(double[][][] data, int repeats)
	{
		Sdata = data;
		Srepeats = repeats;
	}
	
	/**
	 * 
	 */
	public void run()
	{
		/*if(Sdata == null || Srepeats == null)
		{
			return;  TODO
		}*/
		time = System.currentTimeMillis();
		int len = Sdata.length;
		for(int i = 0; i<Srepeats * len; i++)
		{
			int randomIndex = (int)Math.floor(Math.random() * len);
			learn(Sdata[randomIndex][0], Sdata[randomIndex][1][0]);
			
			//Check if the thread's interrupted.
			if(Thread.interrupted())
			{
				return;
			}
		}
		Sdata = null;// Maybe clean up the space a tad?
		System.out.println("> Neuron finished learning in " + (System.currentTimeMillis() - time) + "ms.");
	}
	
	// SETTERS AND GETTERS
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
	public void setInput (int index, double value)
	{
		if(index < inputs.getLength() - 1)
		{
			inputs.setElement(index, value);
		}
	}
	public void setInputs (double[] values)
	{
		int min = Math.min(inputs.getLength(), values.length);
		for(int i = 0; i < min; i++)
		{
			setInput(i, values[i]);
		}
	}
	public void setAllInputs(double value)
	{
		inputs.setAllElementsTo(value);
		inputs.setElement(inputs.getLength()-1, 1);
	}
	public int getNumberOfInputs()
	{
		return(inputs.getLength() - 1);
	}
	public NVector getWeights()
	{
		return(weights.copy());
	}
	public void setWeights(NVector w)
	{
		weights = w.copy();
	}
	
	public static void main (String[] args)
	{
		System.out.println("\n    LOGISTIC NEURON FEATURE TEST:\n");
		int loops = 10;
		double lr = 0.1;
		try{
			loops = Integer.valueOf(args[0]);
			try {
				lr = Double.valueOf(args[1]);
			}catch(Exception e1){
				System.out.println("No argument given for learning rate, using 0.1.");
			}
		}catch(Exception e){
			System.out.println("No argument given for number of times to learn, going with 10.");
		}
		System.out.println("\nThis test sets out to encode a NOT gate.");
		RunnableLogisticNeuron test = new RunnableLogisticNeuron(1, lr);
		System.out.println("Neuron weights, last is bias:");
		System.out.println(test.getWeights());
		System.out.println("Learning...");
		double[][][] trainingSet = {{{0.0},{1.0}},{{1.0},{0.0}}};
		long t = new Date().getTime();
		
		test.setupLearnArray(trainingSet, loops);
		Thread NeuronInstance = new Thread(test);
		NeuronInstance.start();
		try{
			NeuronInstance.join();
		}catch(Exception e){
			System.out.println(".join() falied.");
		}
		
		System.out.println(loops + " learning iterations complete in " + (new Date().getTime() - t) + "ms with a learning rate of " + test.getLearningRate());
		System.out.println("New weights:");
		System.out.println(test.getWeights());
		System.out.println("0 as input:");
		test.setInput(0,0.0);
		System.out.println(test.predict());
		test.setInput(0,1.0);
		System.out.println ("1 as input:");
		System.out.println(test.predict());
	}
}
