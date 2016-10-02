import java.util.ArrayList;

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


        species = new ArrayList<Species>();
        this.inv_db = new InnovationDB();

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
        Genome g = new Genome(inputs, outputs,  inv_db);

        species.add( new Species(g, dis_rate, link_rate, node_rate, f, inv_db) );

        // Speciate all genomes in population
        for (int i = 1; i < size; i++) {
            species.get(0).add(new Genome(g)) ;
        }
    }

    private NEATPop (ArrayList<Genome> pop,
                        double dis_rate,
                        double inter_rate,
                        double node_rate,
                        double link_rate,
                        double compat_thresh,
                        ArrayList<Species> species,
                        InnovationDB inv_db,
                        Fitness f) {
        this.pop = pop;
        this.inv_db = inv_db;
        //species = new ArrayList<Species>();
        this.f = f;
        this.dis_rate   = dis_rate;
        this.inter_rate = inter_rate;
        this.node_rate  = node_rate;
        this.link_rate  = link_rate;
        this.compatThresh= compat_thresh;
        this.species    = species;

        // Speciate all genomes in population
        for ( Genome g : pop ) {
            //System.out.print("|" + g.size());
            speciate(g);
        }
       // System.out.println("\nNumber of species: " + species.size());
    }

    public void speciate (Genome g) {
        if (species.isEmpty())
            species.add( new Species(g, dis_rate, link_rate, node_rate, f, inv_db) );

        else {
            double g_compat;
            int i = 0;

            // Search for an appropriate species
            do {
                g_compat = species.get(i).compatibility(g);
                i++;
            } while (g_compat > compatThresh && i < species.size());

            // Add genome to threshold matched species
            if (g_compat < compatThresh) {
                //System.out.println("Added to a species : " + g_compat);
                species.get(i-1).add(g);
            }
            // If no match exists, create a new species
            else {
                //System.out.println("New species : " + g_compat);
                //System.out.println("G1: \n" + g + "\nG2: " + species.get(i-1).getRep());
                species.add( new Species(g, dis_rate, link_rate, node_rate, f, inv_db) );
            }
        }
    }
    public Genome getMostFit () {

       Creature cret = species.get(0).getMostFit();
        for (Species s : species) {

            if (cret.fitness < s.getMostFit().fitness) {


               cret = s.getMostFit();
            }
        }

        return cret.g;

    }

    // Mutation parameters
    private double dis_rate;
    private double inter_rate;
    private double node_rate;
    private double link_rate;
    private double compatThresh;

    // Number of interface nodes in NN
    private int inputs;
    private int outputs;

    private ArrayList<Genome> pop;
    private ArrayList<Species> species;
    private Fitness f;
    private InnovationDB inv_db;
}
