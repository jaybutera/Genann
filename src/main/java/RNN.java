import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.NDArrayFactory;
import org.nd4j.linalg.factory.Nd4j;

import java.util.stream.Collectors;
import java.util.Arrays;

public class RNN {
    public RNN(Genome g) {
        // TODO: This entire constructor is so ugly and a terrible idea. Need to fix this.

        /*****************************/
        /* Initialize neuron vectors */
        /*****************************/


        float []  inputNeurons  = new float[g.input_nodes.size()];
        float [] outputNeurons = new float[g.output_nodes.size()];
        float [] hiddenNeurons = new float[g.hiddenSize()];



        // Fill hidden and output neurons with hot vector 1
        Arrays.fill(hiddenNeurons, 0.0);
        Arrays.fill(outputNeurons, 0.0);




        /********************************/
        /* Initialize neuron id vectors */
        /********************************/

        // Temporary storage of ids of each type of neuron in genome
        // (used to create weight matrices)
        int[] input_neuron_ids  = new int[g.input_nodes.size()];
        int[] output_neuron_ids = new int[g.output_nodes.size()];
        int[] hidden_neuron_ids = new int[g.hiddenSize()];

        for (int i = 0; i < g.input_nodes.size(); i++)
            input_neuron_ids[i] = g.input_nodes.get(i).id;
        for (int i = 0; i < g.hidden_nodes.size(); i++)
            hidden_neuron_ids[i] = g.hidden_nodes.get(i).id;
        for (int i = 0; i < g.output_nodes.size(); i++)
            output_neuron_ids[i] = g.output_nodes.get(i).id;

        inputNeuronsNd4j = Nd4j.create(inputNeurons);
        hiddenNeuronsNd4j = Nd4j.create(hiddenNeurons);
        outputNeuronsNd4j = Nd4j.create(outputNeurons);

        /******************************/
        /* Initialize weight matrices */
        /******************************/

        float [][] inputWeights  = new float[g.hiddenSize()][g.input_nodes.size()];                       // N x K
        float [][] hiddenWeights = new float[g.hiddenSize()][g.hiddenSize()];                             // N x N
        float [][] outputWeights = new float[g.output_nodes.size()][g.input_nodes.size()+g.hiddenSize()]; // L x (N+K)

        // Input matrix
        for (int i = 0; i < hidden_neuron_ids.length; i++)
            for (int j = 0; j < input_neuron_ids.length; j++)
                inputWeights[i][j] = g.getWeight(input_neuron_ids[j],hidden_neuron_ids[i]);
        // Hidden matrix
        for (int i = 0; i < hidden_neuron_ids.length; i++)
            for (int j = 0; j < hidden_neuron_ids.length; j++)
                hiddenWeights[i][j] = g.getWeight(hidden_neuron_ids[j],hidden_neuron_ids[i]);
        // Output matrix
        int[] ih_neuron_ids = concat(input_neuron_ids, hidden_neuron_ids);
        for (int i = 0; i < output_neuron_ids.length; i++)
            for (int j = 0; j < (hidden_neuron_ids.length+input_neuron_ids.length); j++)
                outputWeights[i][j] = g.getWeight(ih_neuron_ids[j],output_neuron_ids[i]);

       inputWeightsND4J = Nd4j.create(inputWeights);
       hiddenWeightsND4J = Nd4j.create(hiddenWeights);
       outputWeightsND4J = Nd4j.create(outputWeights);



    }
    /*
    private RNN (float[][] inputWeights,
                     float[][] hiddenWeights,
                     float[][] outputWeights)
    {
        // Initialize neurons
        float [] inputNeurons  = new float[inputWeights[0].length]; // Kx1
        float [] hiddenNeurons = new float[hiddenWeights.length]; // Nx1
        float [] outputNeurons = new float[outputWeights.length]; // Lx1

        inputNeuronsNd4j = Nd4j.create(inputNeurons);
        hiddenNeuronsNd4j = Nd4j.create(hiddenNeurons);
        outputNeuronsNd4j = Nd4j.create(outputNeurons);

        // Fil hidden and output neurons with hot vector 1
        Arrays.fill(hiddenNeurons, 0.0);
        Arrays.fill(outputNeurons, 0.0);

        // Initialize weights
        this.inputWeights  = new float[inputWeights.length][inputWeights[0].length];   // NxK
        this.hiddenWeights = new float[hiddenWeights.length][hiddenWeights.length];    // NxN
        this.outputWeights = new float[outputWeights.length][outputWeights[0].length]; // Lx(K+N)
        //this.inptoOutWeights = new float[outputWeights.length][inputWeights[0].length];  // LxK

        // Initialize input weights
        for (int i = 0; i < inputWeights.length; i++)
            for (int j = 0; j < inputWeights[0].length; j++)
                this.inputWeights[i][j] = inputWeights[i][j];
        // Initialize hidden weights
        for (int i = 0; i < hiddenWeights.length; i++)
            for (int j = 0; j < hiddenWeights[0].length; j++)
                this.hiddenWeights[i][j] = hiddenWeights[i][j];
        // Initialize output weights
        for (int i = 0; i < outputWeights.length; i++)
            for (int j = 0; j < outputWeights[0].length; j++)
                this.outputWeights[i][j] = outputWeights[i][j];
    }
    */
    public float[] step (float[] inps) {
        // Check for incompatible input vector length
        assert inputNeuronsNd4j.size(0) == inps.length;

        //inputNeurons = Arrays.copyOf(inps, inps.length);
        INDArray insND4J = Nd4j.create(inps);

        hiddenNeuronsNd4j = activate( inputWeightsND4J.mmul(insND4J).addi( (hiddenWeightsND4J.mmul(hiddenWeightsND4J) ) ))) ;

        // Concatenate inputNeurons and hiddenNeurons
        outputNeuronsNd4j = activate(  outputWeightsND4J.mmul( Nd4j.create(concat(inps, hiddenNeuronsNd4j)) ) );

        float [] outputNeurons = new float [inputNeuronsNd4j.size(0)];
       for (int i = 0 ;i < inputNeuronsNd4j.size(0);i++) {

           outputNeurons[i] = outputNeuronsNd4j.getFloat(0,i);
        }

        return outputNeurons;
    }

    /*
    // Network builder class
    public static class NetworkBuilder {
        private float[][] inputs;
        private float[][] hidden;
        private float[][] outputs;

        public NetworkBuilder inputs (float[][] inputs) {
            this.inputs = new float[inputs.length][inputs[0].length];

            for (int i = 0; i < inputs.length; i++)
                for (int j = 0; j < inputs[0].length; j++)
                    this.inputs[i][j] = inputs[i][j];

            return this;
        }

        public NetworkBuilder outputs (float[][] outputs) {
            this.outputs = new float[outputs.length][outputs[0].length];

            for (int i = 0; i < outputs.length; i++)
                for (int j = 0; j < outputs[0].length; j++)
                    this.outputs[i][j] = outputs[i][j];

            return this;
        }

        public NetworkBuilder hidden (float[][] hidden) {
            this.hidden = new float[hidden.length][hidden[0].length];

            for (int i = 0; i < hidden.length; i++)
                for (int j = 0; j < hidden[0].length; j++)
                    this.hidden[i][j] = hidden[i][j];

            return this;
        }

        public RNN createNetwork () {
            return new RNN (inputs, hidden, outputs);
        }
    }



    public String toString () {

        //String[][] inputWeights  = new float[this.hiddenNeurons.length][this.inputNeurons.length];   // NxK
        //String[][] hiddenWeights = new float[this.hidden.length][this.hiddenNeurons.length];    // NxN
        //String[][] outputWeights = new float[this.outputWeights.length][this.outputWeights[0].length]; // Lx(K+N)

        String mats = "";

        mats += "\nInput adjacency matrix\n\n";
        for (int i = 0; i < this.hiddenNeurons.length; i++) {
            for (int j = 0; j < this.inputNeurons.length; j++)
                mats += String.valueOf(inputWeights[i][j]) + " ";
            mats += "\n";
        }
        mats += "\nHidden adjacency matrix\n\n";
        for (int i = 0; i < this.hiddenNeurons.length; i++) {
            for (int j = 0; j < this.hiddenNeurons.length; j++)
                mats += String.valueOf(hiddenWeights[i][j]) + " ";
            mats += "\n";
        }
        mats += "\nOutput adjacency matrix\n\n";
        for (int i = 0; i < this.outputNeurons.length; i++) {
            for (int j = 0; j < this.hiddenNeurons.length+this.inputNeurons.length; j++)
                mats += String.valueOf(outputWeights[i][j]) + " ";
            mats += "\n";
        }

        return mats;
    }
    */

    private float[] multWeightToVec (float[][] mat, float[] vec) {
        float sum;

        float[] out_vec = new float[mat.length];

        // Temporary error checker
        try {
            if (mat[0].length != vec.length) {
                Arrays.fill(out_vec, 0.0);
                return out_vec;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            //System.out.println("Warning: No hidden nodes in network");
            Arrays.fill(outputNeurons, 0.0);
            return out_vec;
        }

        for (int i = 0; i < mat.length; i++) {
            sum = 0.0;
            for (int j = 0; j < mat[0].length; j++)
                sum += mat[i][j] * vec[j];
            out_vec[i] = sum;
        }

        return out_vec;
    }

    private float[] addVecs (float[] x1, float[] x2) {
        // Temporary error checker
        if (x1.length != x2.length)
            return new float[] {0.0};

        float[] vec = new float[x1.length];

        for (int i = 0; i < x1.length; i++)
            vec[i] = x1[i] + x2[i];

        return vec;
    }

    private float[] concat (float[] x1, float[] x2) {
        float[] vec = new float[x1.length + x2.length];

        int i = 0;
        for (; i < x1.length; i++)
            vec[i] = x1[i];
        for (int j = 0; j < x2.length; i++, j++)
            vec[i] = x2[j];

        return vec;
    }

    private int[] concat (int[] x1, int[] x2) {
        int[] vec = new int[x1.length + x2.length];

        int i = 0;
        for (; i < x1.length; i++)
            vec[i] = x1[i];
        for (int j = 0; j < x2.length; i++, j++)
            vec[i] = x2[j];

        return vec;
    }

    private float[] activate (float[] inp) {
        float[] vec = new float[inp.length];

        // Compute sigmoid
        for (int i = 0; i < inp.length; i++)
            //vec[i] = (float)Math.round(inp[i]); // Step
            vec[i] = 1 / (1 + Math.exp(-inp[i])); // Sigmoid

        return vec;
    }



    private INDArray inputNeuronsNd4j;
    private INDArray hiddenNeuronsNd4j;
    private INDArray outputNeuronsNd4j;




    private INDArray inputWeightsND4J ;
    private INDArray hiddenWeightsND4J ;
    private INDArray outputWeightsND4J;

}
