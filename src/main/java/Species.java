import java.util.ArrayList;
import java.util.Random;

public class Species {
    public Species (final Genome seed,
                    double dis_rate,
                    double link_rate,
                    double node_rate,
                    Fitness f,
                    Innovations inv_db) {
        creatures = new ArrayList<Creature>();
        creatures.add( new Creature(seed) );

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

    // Checks if a genome should exist in a species
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
        creatures.add(g);
    }

    public void flush () {
        creatures.clear();
    }

    public double getSpeciesFit () {
        return creatures.stream()
                .mapToDouble(s -> adjFitness(s.g))
                .sum();
    }

    public ArrayList<Genome> reproduce (double total_fit) {
        // Return empty if no creatures in species
        if ( creatures.isEmpty() )
            return creatures;

        ArrayList<Genome> children = new ArrayList<Genome>();

        // Determine size of next generation species population
        updateFitness();
        int pop_size = (int) Math.round(updateFitness() * creatures.size() / total_fit);
        System.out.println("New species size: " + pop_size);

        // TODO: Initial reproduction algorithm, use factorial formulation in
        // future.
        // Mate each adjacent genome
        for (int i = 1; i < pop_size; i++)
            children.add( creatures.get(i-1).crossover(representative) );

        // Add a final genome to keep same population size
        children.add( creatures.get(creatures.size()-1).crossover(creatures.get(0) ) );



        // Get rep from creatures to guide next generation speciation
        updateRep();

        // Replace pop with next generation
        creatures = children;

        return creatures;
    }

    // Find new representative for species
    public Genome updateRep () {
        for ( Genome g : creatures )
            if (adjFitness(g) > adjFitness(representative))
                representative = g;

        return representative;
    }

    public Double updateFitness () {
        avg_fit = creatures.stream().map(g -> g.fitness)
                .mapToDouble(Double::doubleValue)
                .sum();

        return avg_fit;
    }



    public double adjFitness (Genome g) {
        return g.fitness / creatures.size();
    }


    // For debugging. Should take this out soon.
    public Genome getRep () {
        return representative;
    }

    public Double getAvgFit () {
        return avg_fit;
    }

    public int size () {
        return creatures.size();
    }

    /***************/
    /*   PRIVATE   */
    /***************/


    private ArrayList<Creature> creatures;
    private Creature representative;
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
    private double weight_val_rate;

    // Species average fitness
    private Double avg_fit;
}

