/**
 * Created by devesh on 1/10/16.
 */
import java.util.ArrayList;
import java.util.Random;

public class Species {
    public Species (final Genome seed,
                    double dis_rate,
                    double link_rate,
                    double node_rate,
                    Fitness f,
                    Innovations inv_db) {
        genomes = new ArrayList<Genome>();
        genomes.add(seed);

        // Init mutation params
        this.dis_rate  = dis_rate;
        this.link_rate = link_rate;
        this.node_rate = node_rate;

        this.inv_db = inv_db;

        // Initialize species in/out node standard
        input_size  = seed.inputSize();
        output_size = seed.outputSize();

        // Initialize fitness
        this.f = f;

        // Initial genome becomes the rep
        representative = seed;

        // Initialize compatability parameters
        c1 = 1.0;
        c2 = 1.0;
        c3 = 1.0;
    }

    /*
    public Species (final Genome seed,
                    double c1,
                    double c2,
                    double c3) {
        genomes = new ArrayList<Genome>();
        genomes.add(seed);
        // Initialize species in/out node standard
        input_size  = seed.inputSize();
        output_size = seed.outputSize();
        // Initial genome becomes the rep
        representative = seed;
        // Initialize compatability parameters
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }
    */

    public double compatibility (Genome g) {
        int N;

        /*
        if (g.connections.size() < 20 && representative.connections.size() < 20)
            N = 1;
            */

        // Set largest genome
        if (g.connections.size() > representative.connections.size())
            N = g.connections.size();
        else
            N = representative.connections.size();

        double x =   (c1 * g.getExcess(representative).size()) / N
                + (c2 * g.getDisjoint(representative).size()) / N
                +  c3 * g.weightDiff(representative);

        /*
        System.out.println("\nExcess size: " + g.getExcess(representative).size());
        System.out.println("Disjoint size: " + g.getDisjoint(representative).size());
        System.out.println("Weight diff: " + g.weightDiff(representative));
        */
        return x;
    }

    public void add (Genome g) {
        genomes.add(g);
    }

    public void flush () {
        genomes.clear();
    }

    public double getSpeciesFit () {
        return genomes.stream()
                .mapToDouble(s -> adjFitness(s))
                .sum();
    }

    public ArrayList<Genome> reproduce (double total_fit) {
        // Return empty if no genomes in species
        if ( genomes.isEmpty() )
            return genomes;

        ArrayList<Genome> children = new ArrayList<Genome>();

        // Determine size of next generation species population
        updateFitness();
        int pop_size = (int) Math.round(updateFitness() * genomes.size() / total_fit);
        System.out.println("New species size: " + pop_size);

        // TODO: Initial reproduction algorithm, use factorial formulation in
        // future.
        // Mate each adjacent genome
        for (int i = 1; i < pop_size; i++)
            children.add( crossover(genomes.get(i-1), representative) );

        // Add a final genome to keep same population size
        children.add( crossover(genomes.get(0), genomes.get(genomes.size()-1)) );

        // Get rep from genomes to guide next generation speciation
        updateRep();

        // Replace pop with next generation
        genomes = children;

        return genomes;
    }

    // Find new representative for species
    public Genome updateRep () {
        for ( Genome g : genomes )
            if (adjFitness(g) > adjFitness(representative))
                representative = g;

        return representative;
    }

    public Double updateFitness () {
        avg_fit = genomes.stream().map(g -> g.fitness)
                .mapToDouble(Double::doubleValue)
                .sum();

        return avg_fit;
    }



    public double adjFitness (Genome g) {
        return g.fitness / genomes.size();
    }

    // For debugging. Should take this out soon.
    public Genome getRep () {
        return representative;
    }

    public Double getAvgFit () {
        return avg_fit;
    }

    public int size () {
        return genomes.size();
    }

    /***************/
    /*   PRIVATE   */
    /***************/

    private void perturbLinks (ArrayList<Node> input_layer,
                               ArrayList<Node> output_layer,
                               Genome g) {
        Random r = new Random();

        Node inp;
        Node out;

        // Predefinition avoids run away size changes in for loops
        int inp_size = input_layer.size();
        int out_size = output_layer.size();

        for (int i = 0; i < inp_size; i++) {
            inp = input_layer.get(i);

            for (int j = 0; j < out_size; j++) {
                out = output_layer.get(j);

                //if ( r.nextDouble() < link_rate ) {
                // Chance to add a connection
                if ( r.nextDouble() < link_rate ) {
                    g.addConnection(inp, out);
                }
                else if ( r.nextDouble() < node_rate ) {
                    g.addNode(inp, out);
                }
                //}
            }
        }
    }

    private ArrayList<Genome> genomes;
    private Genome representative;
    private Fitness f;
    private Innovations inv_db;

    public final int input_size;
    public final int output_size;

    // Compatibility parameters
    private double c1;
    private double c2;
    private double c3;

    // Mutation parameters
    private double dis_rate;
    private double link_rate;
    private double node_rate;

    // Species average fitness
    private Double avg_fit;
}

