package classes.blayzeNet.neurons;
import classes.math.NVector;
import java.text.DecimalFormat;

public class LinearNeuron
{
	
	private NVector inputs;
	private NVector weights;
	private double learningRate;
	
	
	public LinearNeuron (int inputCount, double learningRate)
	{
		inputs = new NVector(inputCount);
		weights = new NVector(inputCount);
		setLearningRate(learningRate);
	}
	public LinearNeuron (int inputCount)
	{
		this(inputCount, 1);
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
			weights.setElement(i, weights.getElement(i) + learningRate * inputs[i] * (target - y));
		}
	}
	public void learnArray(double[][][] data, int repeats)
	{
		int len = data.length;
		for(int i = 0; i<repeats; i++)
		{
			int randomIndex = (int)Math.floor(Math.random() * len);
			learn(data[randomIndex][0], data[randomIndex][1][0]);
		}
	}
	public void setLearningRate(double r)
	{
		if(r<=0)
		{
			System.out.println("WARNING: " + r + " is below zero. The learning rate will be set to 1.");
			learningRate = 1;
		}else{
			learningRate = r;
		}
	}

	public double predict ()
	{
		return(inputs.dot(weights));
	}
	public void setInput (int index, double value)
	{
		inputs.setElement(index, value);
	}
	public void setInputs (double[] values)
	{
		int min = Math.min(inputs.getLength(), values.length);
		for(int i = 0; i < min; i++)
		{
			setInput(i, values[i]);
		}
	}
	public int getNumberOfInputs()
	{
		return(inputs.getLength());
	}
	
	public static void main (String[] args)
	{
		LinearNeuron test = new LinearNeuron(3, 0.001);
		/* Training set consisting of an array of lists, each consisting of an array and a target output.
		 * In this test, the aim is £1.60, £2.30, £2.00.
		 */
		double[][][] trainingSet = {{{3.0,5.0,1.0},{18.3}},
					    {{7.0,1.0,6.0},{25.5}},
					    {{0.0,4.0,2.0},{13.2}},
					    {{2.0,5.0,1.0},{16.7}},
					    {{1.0,2.0,3.0},{12.2}},
					    {{5.0,1.0,6.0},{22.3}}};
		
		int loops = 10;
		try{
			loops = Integer.valueOf(args[0]);
		}catch(Exception e){
			System.out.println("No argument given for number of times to learn, going with 10.");
		}
		test.learnArray(trainingSet, loops); 
		test.setInputs(new double[]{1.0,0.0,0.0});
		System.out.println("First: " + Double.parseDouble(new DecimalFormat("#.##").format(test.predict())));
		test.setInputs(new double[]{0.0,1.0,0.0});
		System.out.println("Second: " + Double.parseDouble(new DecimalFormat("#.##").format(test.predict())));
		test.setInputs(new double[]{0.0,0.0,1.0});
		System.out.println("Last: " + Double.parseDouble(new DecimalFormat("#.##").format(test.predict())));	
	}
}
