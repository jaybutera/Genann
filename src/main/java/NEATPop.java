public class NEATPop {

    // Constructor for first (user) population initialization
    public NEATPop(int size,
                       double dis_rate,
                       double inter_rate,
                       double node_rate,
                       double link_rate,
                       double compat_thresh,
                       int inputs,
                       int outputs,
                       Fitness f) {
        // Initialize population
        pop = new ArrayList<>();
        species = new ArrayList<Species>();
        this.inv_db = new Innovations();

        this.f = f;
        this.dis_rate   = dis_rate;
        this.inter_rate = inter_rate;
        this.node_rate  = node_rate;
        this.link_rate  = link_rate;
        this.compatThresh= compat_thresh;

        this.inputs  = inputs;
        this.outputs = outputs;

        // TODO: Don't let the initial pop be uniform like this.
        // Create an initial species for all genomes of first generation to reproduce in (randomly generate initial weights)
        Genome g = new Genome(inputs, outputs, true, inv_db);
        pop.add(g);
        g.fitness = f.simulate( new Network(g) );
        species.add( new Species(g, dis_rate, link_rate, node_rate, f, inv_db) );

        // Speciate all genomes in population
        for (int i = 1; i < size; i++) {
            pop.add( new Genome(pop.get(0)) );
            g = pop.get(i);
            g.fitness = f.simulate( new Network(g) );
            species.get(0).add(g);
        }
    }


    public Genome getMostFit () {

        


    }

}
