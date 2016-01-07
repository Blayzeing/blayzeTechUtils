package classes.blayzeNet.nuclei;

/**
 * A fast, array-based network of sigmoid neurons.
 * This has editable weights, but no learning algorithms of any kind, and was intended for use with evolutionary genetic networks.
 * The network is modelled as a fully-connected nxm network of sigmoid neurons.
 */
 
 import classes.math.MoarMath;
 import java.util.Arrays;
 import classes.graphics.SimpleDisplay;
 import java.awt.Graphics2D;
 import java.awt.Color;
 import classes.blayzeNet.nuclei.CrossoverMode;

public class FullyConnectedSigmoidLattice {

	private double[][] weights, biases;
	private double[] inputWeights, outputWeights, outputBiases;
	private int layerWidth, inputCount, outputCount, networkDepth;
	
	/**
	 * Class constructor.
	 * One time use to construct the lattice.
	 * @param	numberOfInputs		number of inputs this lattice will have
	 * @param	width						how wide each layer of this network will be (sometimes called height)
	 * @param	depth						how many layers deep will this network go
	 * @param	numberOfOutputs	number of outputs this lattice will have
	 */
	public FullyConnectedSigmoidLattice (int numberOfInputs, int width, int depth, int numberOfOutputs)
	{
		layerWidth = width;
		inputCount = numberOfInputs;
		outputCount = numberOfOutputs;
		networkDepth = depth;
		weights = new double[networkDepth][layerWidth*layerWidth];
		inputWeights = new double[inputCount * layerWidth];
		outputWeights = new double[outputCount * layerWidth];
		biases = new double[networkDepth + 1][layerWidth];
		outputBiases = new double[outputCount];
	}

	/**
	 * Randomizes the weights for this lattice.
	 * @param	min	the minimum value any given weight can be
	 * @param	max	the maximum value any given weight can be
	 */
	public void randomizeWeights(double min, double max)
	{
		for(int i = 0; i<weights.length; i++)
			for(int o = 0; o<weights[i].length; o++)
				weights[i][o] = Math.random() * (max-min) + min;
		for(int i = 0; i<inputWeights.length; i++)
			inputWeights[i] = Math.random() * (max-min) + min;
		for(int i = 0; i<outputWeights.length; i++)
			outputWeights[i] = Math.random() * (max-min) + min;
		for(int i = 0; i<biases.length; i++)
			for(int o = 0; o<biases[i].length; o++)
				biases[i][o] = Math.random() * (max-min) + min;
		for(int i = 0; i<outputBiases.length; i++)
			outputBiases[i] = Math.random() * (max-min) + min;
	}
	/**
	 * Randomizes the weights for this lattice between -1 and 1.
	 */
	public void randomizeWeights()
	{
		randomizeWeights(-1,1);
	}

	/**
	 * Returns the entire weight set for this lattice.
	 * @return	double[]	every weight in this lattice as one double[] in the form [inputWeights | weights depth 0, 1, 2, 3..., n | internal biases depth 0, 1, 2..., n | outputWeights | output biases]
	 */
	public double[] getWeights()
	{
		double[] output = new double[inputCount * layerWidth + layerWidth * layerWidth * networkDepth + outputCount * layerWidth + biases.length * layerWidth + outputCount];
		System.arraycopy(inputWeights, 0, output, 0, inputWeights.length);
		for( int i = 0; i<weights.length; i++)
			System.arraycopy(weights[i], 0, output, inputWeights.length + i * weights[0].length, weights[i].length);
		for( int i = 0; i<biases.length; i++)
			System.arraycopy(biases[i], 0, output, inputWeights.length + weights.length * weights[0].length + i * biases[0].length, biases[i].length);
		System.arraycopy(outputWeights, 0, output, inputWeights.length + weights.length * weights[0].length + biases.length * biases[0].length, outputWeights.length);
		System.arraycopy(outputBiases, 0, output,  inputWeights.length + weights.length * weights[0].length + biases.length * biases[0].length + outputWeights.length, outputBiases.length);
		return output;
	}
	/**
	 * Allows specific weight values to be set.
	 * @param	newWeights	An array in the same format as that seen in 'getWeights()' <br> NOTE: This must be the correct size.
	 */
	public void setWeights(double[] newWeights)
	{
		System.arraycopy(newWeights, 0, inputWeights, 0, inputWeights.length);
		for(int i = 0; i<weights.length; i++)
			System.arraycopy(newWeights, inputWeights.length + i * weights[i].length, weights[i], 0, weights[i].length);
		for(int i = 0; i<biases.length; i++)
			System.arraycopy(newWeights, inputWeights.length + weights.length * weights[0].length + i * biases[i].length, biases[i], 0, biases[i].length);
		System.arraycopy(newWeights,  inputWeights.length + weights.length * weights[0].length + biases.length * biases[0].length , outputWeights, 0, outputWeights.length);
		System.arraycopy(newWeights,  inputWeights.length + weights.length * weights[0].length + biases.length * biases[0].length + outputWeights.length, outputBiases, 0, outputBiases.length);
	}
	public FullyConnectedSigmoidLattice cloneWithoutWeights()
	{
		return (new FullyConnectedSigmoidLattice(inputCount, layerWidth, networkDepth, outputCount));
	}
	public FullyConnectedSigmoidLattice clone()
	{
		FullyConnectedSigmoidLattice out = cloneWithoutWeights();
		out.setWeights(this.getWeights());
		return out;
	}

	/**
	 * Propagates the given inputs through the network.
	 * @return	double[]	an array that corresponds to the output values generated by this array
	 */
	public double[] forwardPropagate (double[] inputs)
	{
		//Set up temporary storage of neuron values per weight level
		double[] currentLayerValues = new double[layerWidth];
		for(int i = 0; i<layerWidth; i++)
		{
			double sum = 0;// Sum for this neuron of it's weights times it's inputs
			for(int o = 0; o<inputCount; o++)
			{
				sum = sum + inputWeights[inputCount* i + o] * inputs[o] ;
			}
			sum = sum + biases[0][i];
			currentLayerValues[i] = MoarMath.logit(sum);
		}
		//Loop through each layer of the lattice, calculating the next layer values
		for(int layer = 0; layer<networkDepth; layer++)
		{
			// Create a copy of the previous layer for use in the next
			double[] previousValues = Arrays.copyOf(currentLayerValues, layerWidth);
			for(int i = 0; i<layerWidth; i++)
			{
				double sum = 0;// Sum of each neuron in the last layer multiplied by this neuron's weights
				for(int o = 0; o < layerWidth; o++)
				{
					sum = sum + weights[layer][layerWidth * i + o] * previousValues[o];
				}
				sum = sum + biases[layer+1][i];
				currentLayerValues[i] = MoarMath.logit(sum);
			}
		}
		// Finally, loop through each element in currentLayerValues (the last full layer of neurons)
		// and multiply each layer with it's value in outputWeights, sum and logit them.
		double[] output = new double[outputCount];
		for(int i = 0; i < outputCount; i++)
		{
			double sum = 0;
			for(int o = 0; o<layerWidth; o++)
			{
				sum = sum + currentLayerValues[o] * outputWeights[layerWidth * i + o];
			}
			sum = sum + outputBiases[i];
			output[i] = MoarMath.logit(sum);
		}
		return(output);
	}
	public FullyConnectedSigmoidLattice crossover(FullyConnectedSigmoidLattice otherLattice, CrossoverMode cm)
	{
		return (crossover(otherLattice, cm, new double[]{0.5, 0.01, 1}));
	}
	/**
	 * Crosses this lattice with another.
	 * @param	otherLattice	the lattice to cross this one with
	 * @param	cm				the mode to use when crossing this lattice with the other
	 * @param	info				additional information needed by each mode goes here (uniform = [this:other ratio], uniform mutation = [this:other ratio, mutation chance, random multiplier max/min])
	 * @return	a new network that is the offspring of the network calling this function and otherLattice
	 */
	public FullyConnectedSigmoidLattice crossover(FullyConnectedSigmoidLattice otherLattice, CrossoverMode cm, double[] info)
	{
		double[] thisGenome = this.getWeights();
		double[] otherGenome = otherLattice.getWeights();
		double[] newGenome = new double[thisGenome.length];
		switch(cm)
		{
			case ONE_POINT:
				//TODO: Finish me.
			break;
			case TWO_POINT:
				//TODO: Finish me.
			break;
			case UNIFORM:
				for(int i = 0; i<newGenome.length; i++)
					if(Math.random() >= info[0])
						newGenome[i] = otherGenome[i];
					else
						newGenome[i] = thisGenome[i];
			break;
			case UNIFORM_MUTATION:
				for(int i = 0; i<newGenome.length; i++)
				{
					if(Math.random() >= info[0])
						newGenome[i] = otherGenome[i];
					else
						newGenome[i] = thisGenome[i];
					if(Math.random() <= info[1])
						newGenome[i] = newGenome[i] * info[2];
				}
			break;
		}
		FullyConnectedSigmoidLattice out = this.cloneWithoutWeights();
		out.setWeights(newGenome);
		return out;
	}

	public String toString()
	{
		String out = "                          ---INPUTS ("+inputCount+")---\n\n";
		out = out + Arrays.toString(inputWeights) + "\n";
		out = out + "\n+-------------------------------("+layerWidth+")-------------------------------+\n";
		out = out + "{ "+Arrays.toString(biases[0])+" }\n";
		out = out + "+-----------------------------------------------------------------+\n\n";
		for(int i = 0; i<weights.length; i++)
		{
			out = out + Arrays.toString(weights[i]) + "\n";
			out = out + "\n+-------------------------------("+layerWidth+")-------------------------------+\n\n";
			out = out + "{ "+Arrays.toString(biases[i+1])+" }\n";
					out = out + "+-----------------------------------------------------------------+\n\n";
		}
		out = out + Arrays.toString(outputWeights) + "\n";
		out = out + "\n                          +---OUTPUTS ("+outputCount+")---+\n";
		out = out + "{ "+Arrays.toString(outputBiases)+" }\n";
		return out;
	}
	public void visualise(int width, int height)
	{
		SimpleDisplay networkVis = new SimpleDisplay(width, height, "Network Visualiser", false, true);
		int layerHeight = height/(weights.length + 3);
		Graphics2D g = networkVis.getGraphics2D();
		double rectWidth = (double)width/(double)inputWeights.length;
		for(int i = 0; i<inputWeights.length; i++)
		{
			int color = (int)(MoarMath.sigmoid(inputWeights[i]) * 255);
			g.setColor(new Color((int)(color*0.8),color,(int)(color*0.8)));
			g.fillRect((int)Math.round(i * rectWidth),0,(int)Math.round(rectWidth),layerHeight);
		}
		rectWidth = (double)width/weights[0].length;
		for(int o = 0; o<weights.length; o++)
			for(int i = 0; i<weights[o].length; i++)
			{
				int color = (int)(MoarMath.sigmoid(weights[o][i]) * 255);
				g.setColor(new Color(color,color,color));
				g.fillRect((int)Math.round(i * rectWidth), (int)(layerHeight * 1.5 + layerHeight * o),(int)Math.round(rectWidth), layerHeight);
			}
		rectWidth = (double)width/outputWeights.length;
		for(int i = 0; i<outputWeights.length; i++)
		{
			int color = (int)(MoarMath.sigmoid(outputWeights[i]) * 255);
			g.setColor(new Color(color, (int)(color*0.8),(int)(color*0.8)));
			g.fillRect((int)Math.round(i * rectWidth),layerHeight * (weights.length + 2),(int)Math.round(rectWidth),layerHeight);
		}
		double[] genome = getWeights();
		int geneDispHeight = Math.max((int)(height * 1.3), genome.length);
		SimpleDisplay genomeVis = new SimpleDisplay(280, geneDispHeight, "Genome Sequence", false, true);
		double geneHeight = geneDispHeight/genome.length;
		g = genomeVis.getGraphics2D();
		g.fillRect(0,0,30,30);
		for(int i = 0; i<genome.length; i++)
		{
			int color = (int)(MoarMath.sigmoid(genome[i]) * 255);
			g.setColor(new Color(color,color,color));
			g.fillRect(0,(int)Math.round(i*geneHeight),280, (int)Math.round(geneHeight));
		}
	}

	public static void main (String[] args)
	{
		FullyConnectedSigmoidLattice latter = new FullyConnectedSigmoidLattice(2,2,1, 1);
		System.out.println("First Creation:");
		System.out.println(latter + "\n");
		System.out.println("After Randomization:");
		latter.randomizeWeights();
		String latterPrint = latter.toString();
		System.out.println(latterPrint);
		System.out.println("Weight Output:");
		System.out.println(Arrays.toString(latter.getWeights()));
		System.out.println("Now showing visual display...");
		latter.visualise(400,400);
		System.out.println("Creating new network with same dimensions...");
		FullyConnectedSigmoidLattice former = new FullyConnectedSigmoidLattice(2,2,1,1);
		System.out.println("New network:");
		System.out.println(former);
		System.out.println("Copying data from previous network to new...");
		former.setWeights(latter.getWeights());
		System.out.println("Copied network:");
		System.out.println(former);
		System.out.println("Network copy status: " + (latterPrint.equals(former.toString()) ? " Success!" :  " Fail"));
		System.out.println("Displaying new network...");
		former.visualise(400,400);
		System.out.println("Creating new test network for forwardProp test...");
		FullyConnectedSigmoidLattice propTest = new FullyConnectedSigmoidLattice(2,2,1,1);
		propTest.setWeights(new double[]{-2,1,3,-0.5,  0, 0.5, 7, -1,-3, 8,7,3,-6,-2,9});
		System.out.println(propTest);
		System.out.println("Calculated output (input of (4,2)) below should be equal to 0.775832167802816:");
		System.out.println(propTest.forwardPropagate(new double[]{4,2})[0]);
		System.out.println("Now crossing the test network with original:");
		System.out.println(latter.crossover(propTest, CrossoverMode.UNIFORM));
	}
}
