import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Comparator;

public class Genome {
    // These should be private
    private ArrayList<NodeGene> input_nodes;           // Input neurons
    private ArrayList<NodeGene> output_nodes;          // Output neurons
    private ArrayList<NodeGene> hidden_nodes;          // Hidden neurons
    private ArrayList<NodeGene> nodes;                 // All layers of nodes concatenated
    private ArrayList<ConnectionGene> connections; // Link genes between all layers (with respect to nodes

    public Genome (ArrayList<NodeGene> inputs,
                   ArrayList<NodeGene> hidden,
                   ArrayList<NodeGene> outputs,
                   ArrayList<ConnectionGene> connections) {
        this.input_nodes = inputs;
        this.output_nodes = outputs;
        this.hidden_nodes = hidden;
        this.connections = connections;

    }

    // Randomly generate minimal genome (perceptron structure)
    public Genome (int inputs, int outputs, boolean randGen) {
        // Initialize empty lists
        connections  = new ArrayList<ConnectionGene>();
        nodes        = new ArrayList<NodeGene>();
        hidden_nodes = new ArrayList<NodeGene>();

        this.inv_db = inv_db;

        int inv_id = 0;

        // Initialize input neurons
        input_nodes  = new ArrayList<NodeGene>();
        for (int i = 0; i < inputs; i++) {
            NodeGene n = new NodeGene(NodeType.INPUT);
            n.id = inv_id++;
            addNode(n);
        }

        // Initialize output neurons
        output_nodes = new ArrayList<NodeGene>();
        for (int i = 0; i < outputs; i++) {
            NodeGene n = new NodeGene(NodeType.OUTPUT);
            n.id = inv_id++;
            addNode(n);
        }

        // Randomly generate weights if requested
        if (randGen) {
            Random r = new Random();

            // Make at least one connection
            addConnection(input_nodes.get(r.nextInt(inputs)), output_nodes.get(r.nextInt(outputs)));

            // Chance to make each possible link between input and output nodes
            // with probability .5
            /*
            for (int i = 0; i < inputs; i++)
                for (int o = 0; o < outputs; o++)
                    if ( new Random().nextBoolean() )
                        addConnection(input_nodes.get(i).id, output_nodes.get(o).id);
                        */
            int links = new Random().nextInt(inputs*outputs-1);
            for (int i = 0; i < links; i++)
                addConnection();
        }
    }


    /*
    // Manually defined weight
    // Returns a copy of the current Genome with a new connection added to it if it has not already been innovated before
    public Genome addConnection (NodeGene n1, NodeGene n2, double weight) {

        // Get a connection gene from inv database
        ConnectionGene newConnection = inv_db.createConnectionGene(n1,n2,weight,0);

        // Create copy of list and add new Connection gene  to it
        ArrayList<ConnectionGene> newList = new ArrayList<>() ;
        for (int i = 0 ; i<connections.size();i++){
            newList.add(connections.get(i)) ;
        }


        // Create copy of current Genome
        Genome newGenome = new Genome(this.input_nodes,this.hidden_nodes,this.output_nodes,newList);



        return newGenome;

    }
    */

    // Automatic random weight
    // Returns a copy of the current Genome with a new connection added to it if it has not already been inonvated before

    public Genome addConnection (NodeGene n1, NodeGene n2) {
        double weight = new Random().nextDouble();

        // Get a connection gene from inv database
        ConnectionGene newConnection = inv_db.createConnectionGene(n1,n2,weight,0);

        // Create copy of list and add new Connection gene  to it
        ArrayList<ConnectionGene> newList = new ArrayList<>() ;
        for (int i = 0 ; i<connections.size();i++){
            newList.add(connections.get(i)) ;
        }

        // Create copy of current Genome
        Genome newGenome = new Genome(this.input_nodes,this.hidden_nodes,this.output_nodes,newList);



        return newGenome;
    }




    // Add NodeGene given two nodes
    public NodeGene addNode (NodeGene n1, NodeGene n2) {
        NodeGene n = new NodeGene();

        // Connect n1 to n
        ConnectionGene c1 = addConnection(n1, n);
        // Connect n to n2
        ConnectionGene c2 = addConnection(n, n2);

        // If this is a new innovation, finish augmentation process
        if (inv_db.addInnovation(c1, c2, n)) {
            // Add to local genome database
            nodes.add(n);
            hidden_nodes.add(n);

            // Disable connection from n1 to n2
            connections.remove(getConnection(n1, n2));
            for (int i = 0; i < connections.size(); i++)
                if (connections.get(i).in == n1 && connections.get(i).out == n2)
                    connections.remove(connections.get(i));
        }
        // If it's not a new innovation, finish process if it doesn't already
        // exist in genome
        else {
            if ( getNodeById(n.id) == null ) {
                nodes.add(n);
                hidden_nodes.add(n);

                // Disable connection from n1 to n2
                connections.remove(getConnection(n1, n2));
                for (int i = 0; i < connections.size(); i++)
                    if (connections.get(i).in == n1 && connections.get(i).out == n2)
                        connections.remove(connections.get(i));
            }
        }

        return n;
    }

    public void addNode () {
        // Choose a random connection to augment
        ConnectionGene cg = connections.get(new Random().nextInt(connections.size()-1));

        addNode(cg.in, cg.out);
    }

    /*

    public Double getWeight (int input_id, int output_id) {
        try {
            // Find the connection gene that holds given ids
            Double w = connections.stream()
                    .filter(c -> c.in.id == input_id && c.out.id == output_id)
                    .map(c -> c.weight)
                    .findFirst()
                    .get();
            //System.out.println("Found weight from NodeGene [" + input_id + "] to [" + output_id + "] - " + w);
            return w;
        } catch (Exception e) {
            // If it doesn't exist, there is no connection
            //System.out.println("Couldn't find weight from NodeGene [" + input_id + "] to [" + output_id + "]");
            return 0.0;
        }
    }

    public double weightDiff (Genome g) {
        ArrayList<ConnectionGene> m = this.getMatching(g);
        // Debugging
        if (m.size() == 0) {

           // System.out.println("No matching genes between genomes:");
           //System.out.println(g);
           //System.out.println(this);


            return 0.0;
        }

        double avg = 0.0;

        for ( ConnectionGene c : m )
            avg += c.weight;

        return avg / m.size();
    }
    */
    public int hiddenSize () {
        return hidden_nodes.size();
    }

    public int inputSize () {
        return input_nodes.size();
    }

    public int outputSize () {
        return output_nodes.size();
    }

    public int size() {
        return nodes.size();
    }


    public boolean contains (ConnectionGene c) {
        // Look for matching innovation number
        for ( ConnectionGene cg : connections )
            if (cg.in.id == c.in.id && cg.out.id == c.out.id) {
                return true;
            }
        return false;
    }

    public boolean contains (NodeGene n) {
        // Look for matching innovation number
        for ( NodeGene NodeGene : nodes )
            if (n.id == NodeGene.id)
                return true;
        return false;
    }



    // Returns any excess genes from THIS genome
    public ArrayList<ConnectionGene> getExcess (Genome g) {
        /*
        Genome small = getSmallest(g);
        // Get Largest gene id for both genomes
        Integer this_max_inv = small.connections.stream().map(s -> Integer.valueOf(s.innovation)).max(Comparator.naturalOrder()).get();
        Integer g_max_inv    = g.connections.stream().map(s -> Integer.valueOf(s.innovation)).max(Comparator.naturalOrder()).get();
        if (g_max_inv < this_max_inv)
            return this.connections.stream().filter(s -> s.innovation > g_max_inv).collect(Collectors.toCollection(ArrayList::new));
        */

        // Get largest innovation number in genome
        Integer this_max_inv = g.connections.stream().map(s -> Integer.valueOf(s.innovation)).max(Comparator.naturalOrder()).get();

        return this.connections.stream().filter(s -> s.innovation > this_max_inv).collect(Collectors.toCollection(ArrayList::new));
    }

    // Returns any disjoint genes from THIS genome
    public ArrayList<ConnectionGene> getDisjoint (Genome g) {
        // TODO : Could be optimized. Should store genes pools in sets for easy
        // disjoint evaluation

        /*
        Genome small = getSmallest(g);
        Integer max_inv = small.connections.stream().map(s -> Integer.valueOf(s.innovation)).max(Comparator.naturalOrder()).get();
        ArrayList<ConnectionGene> disjoint = new ArrayList<ConnectionGene>();
        if (small != this) {
            disjoint = small.connections.stream()
                                        .filter(s -> !this.contains(s))
                                        .collect(Collectors.toCollection(ArrayList::new));
            disjoint.addAll(this.connections.stream()
                                            .filter(s -> !small.contains(s) && s.innovation < max_inv)
                                            .collect(Collectors.toCollection(ArrayList::new)));
        }
        else if (small != g) {
            disjoint = small.connections.stream()
                                        .filter(s -> !g.contains(s))
                                        .collect(Collectors.toCollection(ArrayList::new));
            disjoint.addAll(g.connections.stream()
                                            .filter(s -> !small.contains(s) && s.innovation < max_inv)
                                            .collect(Collectors.toCollection(ArrayList::new)));
        }
        */

        return this.connections.stream()
                .filter(s -> !g.contains(s))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<ConnectionGene> getMatching (Genome g) {
        // Find all matching innovation numbers
        // TODO: Optimize by iterating through the smallest genome
        /*
        return this.connections.stream()
                               .filter(c -> g.contains(c))
                               .collect(Collectors.toCollection(ArrayList::new));
                               */
        ArrayList<ConnectionGene> matching = new ArrayList<ConnectionGene>();
        for ( ConnectionGene c : this.connections )
            if ( g.contains(c) )
                matching.add(c);

        return matching;
    }

    public ConnectionGene getConnection (NodeGene in, NodeGene out) {
        for ( ConnectionGene cg : connections )
            if (cg.in.id == in.id && cg.out.id == out.id)
                return cg;
        return null;
    }



    // Display phenotype of genome
    public String toString() {
        String str = "";

        str += "Genome\n-------\n";
        str += "Input NodeGene IDs:\n";
        for (NodeGene n : input_nodes)
            str += n.id + " ";
        str += "\nHidden NodeGene IDs: " + hidden_nodes.size() + "\n";
        for (NodeGene n : hidden_nodes)
            str += n.id + " ";
        str += "\nOutput NodeGene IDs:\n";
        for (NodeGene n : output_nodes)
            str += n.id + " ";
        str += "\n\nConnections: " + connections.size() + "\n\n";
        for (ConnectionGene g : connections)
            str += g + "\n";

        return str;
    }

    public double fitness;

    /*** Innovation number ***/
    private int innovationNum () {
        return inv_num++;
    }
    private int inv_num = 0;
    /*************************/

    /*** NodeGene number ***/
    private int nodeNum () {
        return node_num++;
    }
    private int node_num = 0;
    /*******************/

    private Innovations inv_db;

    private Genome getSmallest (Genome g) {
        if (g.connections.size() < this.connections.size())
            return g;
        return this;
    }

    private NodeGene getNodeById(int id) {
        for ( NodeGene n : nodes )
            if (n.id == id)
                return n;
        return null;
    }

    // TODO: public for debugging. Make private
    public Genome crossover (Genome g1, Genome g2) {
        // Create empty child, no random weights
        Genome child = new Genome(input_size, output_size, false, inv_db);

        // Start from standard template
        //Genome child = new Genome(g1);

        // Assign all matching connection genes
        ArrayList<ConnectionGene> matching = g1.getMatching(g2);
        child.addConnections(matching);

        double g1_fit = adjFitness(g1);
        double g2_fit = adjFitness(g2);

        // If parents have equal fitness, randomly match excess genes
        if (g1_fit == g2_fit) {
            Random r = new Random();

            // Get excess from both parents
            ArrayList<ConnectionGene> excess = g1.getExcess(g2);
            excess.addAll( g2.getExcess(g1) );

            // Get disjoint from both parents
            ArrayList<ConnectionGene> disjoint = g1.getDisjoint(g2);
            disjoint.addAll( g2.getDisjoint(g1) );

            // Randomly assign excess genes to child
            for ( ConnectionGene c : excess )
                if ( r.nextBoolean() )
                    child.addConnection(c);
            for ( ConnectionGene c : disjoint )
                if ( r.nextBoolean() )
                    child.addConnection(c);
        }

        // Otherwise child inherits excess/disjoint genes of most fit parent
        else if (g1_fit > g2_fit) {
            child.addConnections( g1.getExcess(g2) );
            child.addConnections( g1.getDisjoint(g2) );
        }
        else if (g1_fit < g2_fit) {
            child.addConnections( g2.getExcess(g1) );
            child.addConnections( g2.getDisjoint(g1) );
        }

        // Apply mutations
        mutate(child);

       

        return child;
    }

    public void mutate(Genome g) {
        Random r = new Random();

        double weight_val_rate = .10;

        // Mutations for input to hidden connections
        perturbLinks(g.input_nodes, g.hidden_nodes, g);

        // Mutations for hidden to hidden connections
        perturbLinks(g.hidden_nodes, g.hidden_nodes, g);

        // Mutations for hidden to output connections
        perturbLinks(g.hidden_nodes, g.output_nodes, g);

        // Mutations for input to output connections
        perturbLinks(g.input_nodes, g.output_nodes, g);

        // Mutate existing connections
        for ( ConnectionGene cg : g.connections ) {
            // Chance to change weight
            if ( r.nextDouble() < weight_val_rate )
                cg.weight = r.nextDouble();

            // Chance to enable or disable (flip) connection gene
            if ( r.nextDouble() < dis_rate )
                cg.enabled = !cg.enabled;
            //cg = cg.flipGene();
        }
    }
}