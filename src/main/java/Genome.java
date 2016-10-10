import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Comparator;

public final class Genome {

    // These should be private
    public Random r = new Random();


    public ArrayList<ConnectionGene> connections = new ArrayList(); // Link genes between all layers (with respect to nodes

    ArrayList<NodeGene> input_nodes, output_nodes, hidden_nodes;

    public InnovationDB inv_db;



    public Genome (ArrayList<NodeGene> inputs,
                   ArrayList<NodeGene> hidden,
                   ArrayList<NodeGene> outputs,
                   ArrayList<ConnectionGene> connections) {

        hidden_nodes = new ArrayList<NodeGene>(hidden);
        input_nodes  = new ArrayList<NodeGene>(inputs);
        output_nodes = new ArrayList<NodeGene>(outputs);
        connections = new ArrayList<ConnectionGene>(connections);

    }


    // Randomly generate minimal genome (perceptron structure)
    public Genome (int inputs, int outputs, boolean randGen, InnovationDB inv_db) {
        // Initialize empty lists


        ArrayList<NodeGene> temp_input_nodes = new ArrayList<NodeGene>();
        ArrayList<NodeGene> temp_hidden_nodes = new ArrayList<NodeGene>();
        ArrayList<NodeGene> temp_output_nodes = new ArrayList<NodeGene>();
        ArrayList<ConnectionGene> temp_connection_nodes = new ArrayList<ConnectionGene>();


        this.inv_db = inv_db;

        for (int i = 0; i < inputs; i++) {

            NodeGene n = inv_db.addFloatingNode();
            temp_input_nodes.add(n);

        }

        for (int i = 0; i < outputs; i++) {

            NodeGene n = inv_db.addFloatingNode();
            temp_output_nodes.add(n);
        }

        // Randomly generate weights if requested
        if (randGen) {
            Random r = new Random();

            // Make at least one connection
            inv_db.addConnection(input_nodes.get(r.nextInt(inputs)), output_nodes.get(r.nextInt(outputs)));

            // Too much work to implement now; Need to though to improve diversity of initial population
            //int links = new Random().nextInt(inputs*outputs-1);
            //for (int i = 0; i < links; i++)
            //    addConnection();
        }
    }

    // Copy constructor
    public Genome (Genome g) {

        hidden_nodes = new ArrayList<NodeGene>(g.hidden_nodes);
        input_nodes  = new ArrayList<NodeGene>(g.input_nodes);
        output_nodes = new ArrayList<NodeGene>(g.output_nodes);
        connections = new ArrayList<ConnectionGene>(g.connections);

        this.inv_db = g.inv_db;
    }


    /*
    public Genome addConnection(ConnectionGene cs) {
        ArrayList<ConnectionGene> newConnections = new ArrayList();
        newConnections.addAll(this.connections);
        newConnections.add(cs);
        return alteredGenome(0, newConnections);
    }





    // Automatic random weight
    // Returns a copy of the current Genome with a new connection added to it if it has not already been inonvated before
    private Genome addConnection(NodeGene n1, NodeGene n2, double weight){
        // Get a connection gene from inv database
        ConnectionGene newConnection = inv_db.addConnection(n1, n2, weight);

        Genome child = this.clone().addConnection(newConnection);
        return child;
    }

*/



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



    public ArrayList<NodeGene> getExcessNodes(Genome g) {

        Integer this_max_inv = g.hidden_nodes.stream().map(s -> Integer.valueOf(s.id)).max(Comparator.naturalOrder()).get();

        return this.hidden_nodes.stream().filter(s -> s.id > this_max_inv).collect(Collectors.toCollection(ArrayList::new));
    }

    // Returns any disjoint genes from THIS genome

    public ArrayList<NodeGene> getDisjointNodes(Genome g) {
        ArrayList<NodeGene> c = (this.hidden_nodes.stream()
                .filter(s -> !g.hidden_nodes.contains(s))
                .collect(Collectors.toCollection(ArrayList::new)));
        c.removeAll(getExcess(g));
        return c;
    }

    public ArrayList<NodeGene> getMatchingNodes(Genome g) {
        return this.hidden_nodes.stream()
                .filter(s -> g.hidden_nodes.contains(s))
                .collect(Collectors.toCollection(ArrayList::new));
    }


// Display phenotype of genome
    public String toString() {
        String str = "";

        str += "Genome\n-------\n";
        str += "Input NodeGene IDs:\n";
        for (NodeGene n : input_nodes) {
            str += n.id + " ";
        }
        str += "\nHidden NodeGene IDs: " + hidden_nodes.size() + "\n";
        for (NodeGene n : hidden_nodes) {
            str += n.id + " ";
        }
        str += "\nOutput NodeGene IDs:\n";
        for (NodeGene n : output_nodes) {
            str += n.id + " ";
        }
        str += "\n\nConnections: " + connections.size() + "\n\n";
        for (ConnectionGene g : connections) {
            str += g + "\n";
        }

        return str;
    }

    public ConnectionGene getConnection (NodeGene in, NodeGene out) {
        for ( ConnectionGene cg : connections )
            if (cg.from.id == in.id && cg.to.id == out.id)
                return cg;
        return null;
    }


    public Genome crossover(Genome g2) {



        // Connections stuff
        //------------------


        // Assign all matching connection genes
        ArrayList<ConnectionGene> matching_connections = this.getMatching(g2);

        // If parents have equal fitness, randomly match excess genes
        // Get excess from both parents
        ArrayList<ConnectionGene> excess = this.getExcess(g2);
        excess.addAll(g2.getExcess(this));
        // Get disjoint from both parents
        ArrayList<ConnectionGene> disjoint = this.getDisjoint(g2);
        disjoint.addAll(g2.getDisjoint(this));


        // Nodes stuff
        //------------


        // Assign all matching nodes genes
        ArrayList<NodeGene> matchingNodes = this.getMatchingNodes(g2);

        // If parents have equal fitness, randomly match excess genes
        // Get excess from both parents
        ArrayList<NodeGene> excessNodes = this.getExcessNodes(g2);
        excess.addAll(g2.getExcess(this));

        // Get disjoint from both parents
        ArrayList<NodeGene> disjointNodes = this.getDisjointNodes(g2);
        disjointNodes.addAll(g2.getDisjointNodes(this));


        GenomeBuilder child = new GenomeBuilder(inv_db)
                .inputs(input_nodes)
                .outputs(output_nodes)
                .hiddens(matchingNodes)
                .connections(matching_connections);


        for (ConnectionGene c : excess) {
            if (r.nextBoolean()) {
                child = child.addExistingConnection(c);
            }
        }
        for (ConnectionGene c : disjoint) {
            if (r.nextBoolean()) {
                child = child.addExistingConnection(c);
            }
        }



        // Apply mutations
        return child.createGenome().mutate(.1, .01);

    }

    public Genome mutate(double weight_val_rate, double dis_rate) {


        //double weight_val_rate = .10;
        // Mutations for input to hidden connections
        Genome genome = perturbLinks(this);


        ArrayList<ConnectionGene> mutables = new ArrayList(genome.connections);

        // Mutate existing connections
        for (ConnectionGene cg : mutables) {
            // Chance to change weight
            if (r.nextDouble() < weight_val_rate) {
                mutables.add(cg.setWeight(r.nextDouble()));
                mutables.remove(cg);

            }

            // Chance to enable or disable (flip) connection gene
            if (r.nextDouble() < dis_rate) {
                mutables.add(cg.flip());
                mutables.remove(cg);
            }
        }


        return new Genome(genome.input_nodes,genome.hidden_nodes,genome.output_nodes,mutables);
    }

    // Used to add new random Connections
    private Genome  perturbLinks(Genome g) {
        ArrayList<ConnectionGene> new_connections = new ArrayList<ConnectionGene>(g.connections);
        ArrayList<NodeGene> new_nodes = new ArrayList<NodeGene>(g.hidden_nodes);

        for (NodeGene n : g.input_nodes) {
            for (NodeGene o : g.output_nodes) {

                // Chance to add a connection
                if (r.nextDouble() < .2)
                    new_connections.add( inv_db.addConnection(n, o) );

                // Chance to add a node split
                else if (r.nextDouble() < .1) {
                    NodeInnovation ni = inv_db.addNode(n, o);
                    new_connections.remove(getConnection(n,o));
                    new_nodes.add(ni.n);
                    new_connections.add(ni.c1);
                    new_connections.add(ni.c2);
                }
            }
        }

        return new Genome(g.input_nodes, new_nodes, g.output_nodes, new_connections);
    }

    double weightDiff(Genome g) {
        ArrayList<ConnectionGene> m = this.getMatching(g);
        // Debugging
        if (m.size() == 0) {
            /*
            System.out.println("No matching genes between genomes:");
            System.out.println(g);
            System.out.println(this);
            */

            return 0.0;
        }

        double avg = 0.0;

        for ( ConnectionGene c : m )
            avg += c.weight;

        return avg / m.size();
    }




    //public int hiddenSize(){
      //  return hidden_nodes.size();
    //}


    public double getWeight(int g1_id, int g2_id){
        for(ConnectionGene c: connections){
            if(c.to.id == g1_id){
                if(c.from.id == g2_id){
                    return c.weight;
                }
            }
        
        }
        return 0;
    }


    // Genome builder class
    public static class GenomeBuilder {

        ArrayList<NodeGene> inputs, outputs, hiddens;
        ArrayList<ConnectionGene> connections;
        InnovationDB inv_db;

         //for add node func
        GenomeBuilder (InnovationDB inv_db) {

            this.inv_db = inv_db;

        }


        public GenomeBuilder inputs (ArrayList<NodeGene> inputs) {
            this.inputs = inputs;
            return this;

        }

        public GenomeBuilder outputs (ArrayList<NodeGene> outputs) {
            this.outputs = outputs;
            return this;

        }

        public GenomeBuilder hiddens (ArrayList<NodeGene> hiddens) {
            this.hiddens = hiddens;
            return this;

        }

        public GenomeBuilder connections (ArrayList<ConnectionGene> connections) {
            this.connections = connections;
            return this;

        }



        //  Might need to be used in the future
        public GenomeBuilder addNode () {
            NodeGene single = inv_db.addFloatingNode();
            hiddens.add(single);
            return this;

        }


        public GenomeBuilder addExistingConnection (ConnectionGene c) {

            connections.add(c);
            if (!hiddens.contains(c.from)){

                hiddens.add(c.from);

            }

            if (!hiddens.contains(c.to)){

                hiddens.add(c.to);

            }
            return this;

        }

        public Genome createGenome () {

            return new Genome (inputs,hiddens,outputs,connections);


        }



    }


}
