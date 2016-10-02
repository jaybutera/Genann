import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Comparator;

public final class Genome {

    // These should be private
    public Random r = new Random();
    public final int INPUTS, OUTPUTS;       // Hidden neurons
    public ArrayList<NodeGene> nodes;                 // All layers of nodes concatenated
    public ArrayList<ConnectionGene> connections; // Link genes between all layers (with respect to nodes

    ArrayList<NodeGene> input_nodes, output_nodes, hidden_nodes;

    public InnovationDB inv_db;

    // Randomly generate minimal genome (perceptron structure)
    public Genome(int inputs, int outputs, int hidden, InnovationDB inv_db) {
        this.INPUTS = inputs;
        this.OUTPUTS = outputs;

        for (int i = 0; i < inputs + outputs + hidden; i++) {
            nodes.add(new NodeGene(i + 1));
        }
        input_nodes = inputNodes();
        output_nodes = outputNodes();
        hidden_nodes = hiddenNodes();
    }

    public Genome(int inputs, int outputs, int hidden, InnovationDB inv_db, ArrayList<ConnectionGene> c) {
        this(inputs, outputs, hidden, inv_db);
        this.connections.addAll(c);
    }

    private Genome(int inputs, int outputs, ArrayList<NodeGene> hidden, InnovationDB inv_db, ArrayList<ConnectionGene> connections) {
        this(inputs, outputs, 0, inv_db, connections);
        this.nodes.addAll(hidden);
    }

    private Genome addNode(NodeGene ng) {
        ArrayList<NodeGene> temp = new ArrayList(hidden_nodes);
        temp.add(ng);
        return alteredGenome(temp, connections);
    }

    private Genome addNode() {
        return addNodes(1);

    }

    private Genome addNodes(int n) {
        return alteredGenome(n, connections);
    }

    public Genome addConnection(ConnectionGene cs) {
        ArrayList<ConnectionGene> newConnections = new ArrayList();
        newConnections.addAll(this.connections);
        newConnections.add(cs);
        return alteredGenome(0, newConnections);
    }

    // Automatic random weight
    // Returns a copy of the current Genome with a new connection added to it if it has not already been inonvated before
    private Genome addConnection(NodeGene n1, NodeGene n2, double weight) {

        // Get a connection gene from inv database
        ConnectionGene newConnection = inv_db.createConnection(n1, n2, weight);

        Genome child = this.clone().addConnection(newConnection);
        return child;
    }
//        public ArrayList<ConnectionGene> split(NodeGene g1, NodeGene, g2){
//            ArrayList<ConnectionGene> consplit = new ArrayList;
//        }

    public ArrayList<ConnectionGene> getExcess(Genome g) {

        Integer this_max_inv = g.connections.stream().map(s -> Integer.valueOf(s.id)).max(Comparator.naturalOrder()).get();

        return this.connections.stream().filter(s -> s.id > this_max_inv).collect(Collectors.toCollection(ArrayList::new));
    }

    // Returns any disjoint genes from THIS genome
    public ArrayList<ConnectionGene> getDisjoint(Genome g) {
        ArrayList<ConnectionGene> c = (this.connections.stream()
                .filter(s -> !g.connections.contains(s))
                .collect(Collectors.toCollection(ArrayList::new)));
        c.removeAll(getExcess(g));
        return c;
    }

    public ArrayList<ConnectionGene> getMatching(Genome g) {
        return this.connections.stream()
                .filter(s -> g.connections.contains(s))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ConnectionGene getConnection(NodeGene in, NodeGene out) {
        for (ConnectionGene cg : connections) {
            if (cg.from.id == in.id && cg.to.id == out.id) {
                return cg;
            }
        }
        return null;
    }

    public ArrayList<NodeGene> getExcessNodes(Genome g) {

        Integer this_max_inv = g.hiddenNodes().stream().map(s -> Integer.valueOf(s.id)).max(Comparator.naturalOrder()).get();

        return this.hiddenNodes().stream().filter(s -> s.id > this_max_inv).collect(Collectors.toCollection(ArrayList::new));
    }

    // Returns any disjoint genes from THIS genome

    public ArrayList<NodeGene> getDisjointNodes(Genome g) {
        ArrayList<NodeGene> c = (this.hiddenNodes().stream()
                .filter(s -> !g.hiddenNodes().contains(s))
                .collect(Collectors.toCollection(ArrayList::new)));
        c.removeAll(getExcess(g));
        return c;
    }

    public ArrayList<NodeGene> getMatchingNodes(Genome g) {
        return this.hiddenNodes().stream()
                .filter(s -> g.hiddenNodes().contains(s))
                .collect(Collectors.toCollection(ArrayList::new));
    }

// Display phenotype of genome
    public String toString() {
        String str = "";

        str += "Genome\n-------\n";
        str += "Input NodeGene IDs:\n";
        for (NodeGene n : inputNodes()) {
            str += n.id + " ";
        }
        str += "\nHidden NodeGene IDs: " + hiddenNodes().size() + "\n";
        for (NodeGene n : hiddenNodes()) {
            str += n.id + " ";
        }
        str += "\nOutput NodeGene IDs:\n";
        for (NodeGene n : outputNodes()) {
            str += n.id + " ";
        }
        str += "\n\nConnections: " + connections.size() + "\n\n";
        for (ConnectionGene g : connections) {
            str += g + "\n";
        }

        return str;
    }

    private Genome getSmallest(Genome g) {
        if (g.connections.size() < this.connections.size()) {
            return g;
        }
        return this;
    }

    public Genome crossover(Genome g2) {
        // Create empty child, no random weights

        // Start from standard template
        //Genome child = new Genome(g1);
        // Assign all matching connection genes
        ArrayList<ConnectionGene> matching = this.getMatching(g2);

        // If parents have equal fitness, randomly match excess genes
        // Get excess from both parents
        ArrayList<ConnectionGene> excess = this.getExcess(g2);
        excess.addAll(g2.getExcess(this));
        // Get disjoint from both parents
        ArrayList<ConnectionGene> disjoint = this.getDisjoint(g2);
        disjoint.addAll(g2.getDisjoint(this));

        ArrayList<NodeGene> matchingNodes = this.getMatchingNodes(g2);
        // If parents have equal fitness, randomly match excess genes
        // Get excess from both parents
        ArrayList<NodeGene> excessNodes = this.getExcessNodes(g2);
        excess.addAll(g2.getExcess(this));
        // Get disjoint from both parents
        ArrayList<NodeGene> disjointNodes = this.getDisjointNodes(g2);
        disjointNodes.addAll(g2.getDisjointNodes(this));

        Genome child = alteredGenome(matchingNodes, matching);

        for (ConnectionGene c : excess) {
            if (r.nextBoolean()) {
                child = child.addConnection(c);
            }
        }
        for (ConnectionGene c : disjoint) {
            if (r.nextBoolean()) {
                child = child.addConnection(c);
            }
        }

        for (NodeGene c : excessNodes) {
            if (r.nextBoolean()) {
                child = child.addNode(c);
            }
        }
        for (NodeGene c : disjointNodes) {
            if (r.nextBoolean()) {
                child = child.addNode(c);
            }
        }

        // Apply mutations
        return child.mutate(.1, .01);

    }

    public Genome mutate(double weight_val_rate, double dis_rate) {

        //double weight_val_rate = .10;
        // Mutations for input to hidden connections
        perturbLinks(inputNodes(), hiddenNodes());

        // Mutations for hidden to hidden connections
        perturbLinks(hiddenNodes(), hiddenNodes());

        // Mutations for hidden to output connections
        perturbLinks(hiddenNodes(), outputNodes());

        // Mutations for input to output connections
        perturbLinks(inputNodes(), outputNodes());
        ArrayList<ConnectionGene> muttables = new ArrayList(connections);
        // Mutate existing connections
        for (ConnectionGene cg : muttables) {
            // Chance to change weight
            if (r.nextDouble() < weight_val_rate) {
                cg.weight = r.nextDouble();
            }

            // Chance to enable or disable (flip) connection gene
            if (r.nextDouble() < dis_rate) {
                cg.enabled = !cg.enabled;
            }
        }

        return alteredGenome(0, muttables);
    }

//
    private void perturbLinks(ArrayList<NodeGene> input_layer, ArrayList<NodeGene> output_layer) {

        NodeGene inp;
        NodeGene out;

        // Predefinition avoids run away size changes in for loops
        int inp_size = input_layer.size();
        int out_size = output_layer.size();

        for (int i = 0; i < inp_size; i++) {
            inp = input_layer.get(i);

            for (int j = 0; j < out_size; j++) {
                out = output_layer.get(j);

                //if ( r.nextDouble() < link_rate ) {
                // Chance to add a connection
                if (r.nextDouble() < .2) {
                    addConnection(inp, out, r.nextDouble());
                } else if (r.nextDouble() < .1) {
//                    split(inp, out);
                }
                //}
            }
        }
    }

    double weightDiff(Genome g) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param n number of genes to add (will add to hidden)
     * @param connections (connections to initialize gene with);
     * @return
     */
    Genome alteredGenome(int n, ArrayList<ConnectionGene> connections) {
        return new Genome(INPUTS, OUTPUTS, nodes.size() - INPUTS - OUTPUTS + n, inv_db, connections);
    }

    Genome alteredGenome(ArrayList<NodeGene> hidden, ArrayList<ConnectionGene> connections) {
        return new Genome(INPUTS, OUTPUTS, hidden, inv_db, connections);
    }

    protected Genome clone() {
        return alteredGenome(0, connections);
    }

    public ArrayList<NodeGene> inputNodes() {
        return new ArrayList(nodes.subList(0, INPUTS));
    }

    public ArrayList<NodeGene> outputNodes() {
        return new ArrayList(nodes.subList(INPUTS, OUTPUTS));
    }

    public ArrayList<NodeGene> hiddenNodes() {
        return new ArrayList(nodes.subList(OUTPUTS, nodes.size() - 1));
    }

}
