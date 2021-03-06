import java.util.ArrayList;

public class Species {
    public Species (final Genome seed,
                    double dis_rate,
                    double link_rate,
                    double node_rate,
                    Fitness f,
                    InnovationDB inv_db) {
        creatures = new ArrayList<Creature>();
        Creature c = new Creature(seed)  ;
        creatures.add(c );

        // Init mutation params
        this.dis_rate  = dis_rate;
        this.link_rate = link_rate;
        this.node_rate = node_rate;

        this.inv_db = inv_db;

        // Initialize species in/out node standard
        input_size  = seed.input_nodes.size();
        output_size = seed.output_nodes.size();

        // Initialize fitness
        this.f = f;

        // Initial genome becomes the rep
        representative = c;

        // Initialize compatability parameters
        c1 = 1.0;
        c2 = 1.0;
        c3 = 1.0;
    }

    // Checks if a genome should exist in a species
    public double compatibility (Genome g) {
        int N;


        // Set largest genome
        if (g.connections.size() > representative.g.connections.size())
            N = g.connections.size();
        else
            N = representative.g.connections.size();

        // Temp test
        if (N < 1) return 1.5;

        System.out.println("Excess: " + g.getExcess(representative.g).size());
        System.out.println("Dis: " + g.getDisjoint(representative.g).size());
        System.out.println("Diff: " + g.weightDiff(representative.g));

        double x =   (c1 * g.getExcess(representative.g).size()) / N
                + (c2 * g.getDisjoint(representative.g).size()) / N
                +  c3 * g.weightDiff(representative.g);

        /*
        System.out.println("\nExcess size: " + g.getExcess(representative).size());
        System.out.println("Disjoint size: " + g.getDisjoint(representative).size());
        System.out.println("Weight diff: " + g.weightDiff(representative));
        */
        return x;
    }

    public void add (Genome g) {
        creatures.add(new Creature(g));
        representative = updateRep();
    }

    public void flush () {
        creatures.clear();
    }

    public double getSpeciesFit () {
        return creatures.stream()
                .mapToDouble(s -> adjFitness(s))
                .sum();
    }


    public Creature getMostFit () {
        Creature cret = creatures.get(0);

        // Find max
        for (Creature c : creatures)
            if (cret.fitness< c.fitness)
                cret = c;

        return cret;
    }

    public ArrayList<Creature> reproduce (double total_fit) {

        // Return empty if no creatures in species
        if ( creatures.isEmpty() )
            return new ArrayList<Creature>();

        ArrayList<Genome> children = new ArrayList<Genome>();

        // Determine size of next generation species population
        getAvgFitness();

        int pop_size;
        if (total_fit > getAvgFit())
            pop_size = (int) Math.ceil(creatures.size() * 1.1;
        else
            pop_size = (int) Math.ceil(creatures.size() * 0.9);

        System.out.println("New species size: " + pop_size);

        // TODO: Initial reproduction algorithm, use factorial formulation in
        // future.
        // Mate each adjacent genome
        for (int i = 1; i < pop_size; i++)
            children.add( creatures.get(i-1).g.crossover(representative.g) );

        // Add a final genome to keep same population size
//        System.out.println(creatures.get(creatures.size()-1).g+"\n\n"+creatures.get(0).g);
        children.add(creatures.get(creatures.size()-1).g.crossover(creatures.get(0).g) );

        flush(); // Remove all old creatures
        for (Genome g: children)
            creatures.add(new Creature(g));

        // Get rep from creatures to guide next generation speciation
        representative = updateRep();

        return creatures;
    }

    // Find new representative for species
    public Creature updateRep () {
        for ( Creature c : creatures )
            if (adjFitness(c) > adjFitness(representative))
                representative = c;

        return representative;
    }

    public Double getAvgFitness() {
        avg_fit = creatures.stream().map(c -> c.fitness)
                .mapToDouble(Double::doubleValue)
                .sum();


        return avg_fit/creatures.size();
    }



    public double adjFitness (Creature c) {
        return c.fitness / creatures.size();
    }

    
    // For debugging. Should take this out soon.
    public Creature getRep () {
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


    public ArrayList<Creature> creatures;
    private Creature representative;
    private Fitness f;
    private InnovationDB inv_db;


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

