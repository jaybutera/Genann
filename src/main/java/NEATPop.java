import java.util.ArrayList;
import java.util.stream.Collectors;

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


        this.species = new ArrayList<Species>();
        this.inv_db = new InnovationDB();

        this.f = f;
        this.dis_rate   = dis_rate;
        this.inter_rate = inter_rate;
        this.node_rate  = node_rate;
        this.link_rate  = link_rate;
        this.compatThresh= compat_thresh;
        this.pop = new ArrayList<Genome>();
        this.inputs  = inputs;
        this.outputs = outputs;

        // TODO: Don't let the initial pop be uniform like this.
        // Create an initial species for all genomes of first generation to reproduce in (randomly generate initial weights)
        Genome g = new Genome(inputs, outputs, false, inv_db);
        species.add( new Species(g, dis_rate, link_rate, node_rate, f, inv_db) );

        // Copy seed genome, all go into one species
        for (int i = 1; i < size; i++) {
            species.get(0).add( new Genome(g) );
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
                System.out.println("Compat: " + g_compat);
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


    public Double getAvgSpeciesFitness () {

        // Get total species fitness as a parameter for reproduction
        Double total_fit = 0.0;
        for (Species s : species )
            total_fit += s.getAvgFitness();
        total_fit = total_fit /  species.size();
        return total_fit;

    }


     public NEATPop nextGen () {
        // Accumulate genomes from species reproduction
        System.out.println("Number of species: " + species.size());
        //System.out.println("size of that: " + species.get(0).size());

        Double total_fit = getAvgSpeciesFitness();

        Species s;
        for (int i = 0; i < species.size(); i++) {
            s = species.get(i);
            // Remove obsolete species
            if (s.size() < 1)
                species.remove(s);

            ArrayList<Genome> genomePop = new ArrayList();
            ArrayList<Creature> creaturePop = s.reproduce(total_fit);
            for(Creature c: creaturePop){
                genomePop.add(c.g);
            }
//            s.reproduce(total_fit).stream()
            //pop.addAll(genomePop);
            pop = genomePop;
            s.flush(); // Remove all genomes for respeciation (NEAT style)
        }

        /*
        System.out.println("Node innovation num: " + inv_db.getNodeInvNum());
        System.out.println("Conn innovation num: " + inv_db.getConnInvNum());
        */

        System.out.println("Pop size:" + pop.size());
        return new NEATPop(pop,
                              dis_rate,
                              inter_rate,
                              node_rate,
                              link_rate,
                              compatThresh,
                              species,
                              inv_db,
                              f);
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
