import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Comparator;

public final class Genome {
	// These should be private
	public ArrayList<NodeGene> input_nodes;           // Input neurons
	public ArrayList<NodeGene> output_nodes;          // Output neurons
	public ArrayList<NodeGene> hidden_nodes;          // Hidden neurons
	public ArrayList<NodeGene> nodes;                 // All layers of nodes concatenated
	public ArrayList<ConnectionGene> connections; // Link genes between all layers (with respect to nodes

	//Count number of genes in Genome;
	private int count;

    private InnovationDB inv_db;

	public Genome (ArrayList<NodeGene> inputs,
	ArrayList<NodeGene> hidden,
	ArrayList<NodeGene> outputs,
	ArrayList<ConnectionGene> connections) {
		this.input_nodes = inputs;
		this.output_nodes = outputs;
		this.hidden_nodes = hidden;
		this.connections = connections;
		this.count = input_nodes.size() + output_nodes.size() + hidden_nodes.size();

	}

	// Randomly generate minimal genome (perceptron structure)
	public Genome (int inputs, int outputs, InnovationDB inv_db) {
		// Initialize empty lists
		connections  = new ArrayList<ConnectionGene>();
		nodes        = new ArrayList<NodeGene>();
		hidden_nodes = new ArrayList<NodeGene>();

		this.inv_db = inv_db;
		int inv_id = 0;
		// Initialize input neurons
		input_nodes  = new ArrayList<NodeGene>();


		// Randomly generate weights if requested

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

	public addNodes(ArrayList<NodeGenes> gs, int n){

	}

	private Genome addConnections (ArrayList<ConnectionGene> cs) {
        Genome gen;
		for (ConnectionGene c : cs)
		    addConnection(c);
        return this.addConnection(cs);
	}

	// Automatic random weight
	// Returns a copy of the current Genome with a new connection added to it if it has not already been inonvated before

	public Genome addConnection (NodeGene n1, NodeGene n2) {
		double weight = new Random().nextDouble();

		// Get a connection gene from inv database
		ConnectionGene newConnection = inv_db.createConnection(n1,n2,weight);

		// Create copy of list and add new Connection gene  to it
		ArrayList<ConnectionGene> newList = new ArrayList<ConnectionGene>();
		newList.addAll(connections);
		newList.add(newConnection);

		// Create copy of current Genome
		Genome newGenome = new Genome(this.input_nodes,this.hidden_nodes,this.output_nodes,newList);


		return newGenome;
	}




	// Add NodeGene given two nodes
	public NodeGene addNode (NodeGene n1, NodeGene n2) {
		NodeGene n = new NodeGene(count);
		this.count++;

		return n;
	}

	public void addNode () {
		// Choose a random connection to augment
		ConnectionGene cg = connections.get(new Random().nextInt(connections.size()-1));

		addNode(cg.from, cg.to);
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
		if (cg.from.id == c.from.id && cg.to.id == c.to.id) {
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
		return this.connections.stream()
		.filter(s -> !g.contains(s))
		.collect(Collectors.toCollection(ArrayList::new));
	}

	public ArrayList<ConnectionGene> getMatching (Genome g) {

	ArrayList<ConnectionGene> matching = new ArrayList<ConnectionGene>();
	for ( ConnectionGene c : this.connections )
	if ( g.contains(c) )
	matching.add(c);

	return matching;
}

public ConnectionGene getConnection (NodeGene in, NodeGene out) {
	for ( ConnectionGene cg : connections )
	if (cg.from.id == in.id && cg.to.id == out.id)
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

private Genome getSmallest (Genome g) {
	if (g.connections.size() < this.connections.size())
	return g;
	return this;
}

public Genome crossover (Genome g2) {
	// Create empty child, no random weights
	Genome child = new Genome(input_size, output_size, false, inv_db);

	// Start from standard template
	//Genome child = new Genome(g1);

	// Assign all matching connection genes
	ArrayList<ConnectionGene> matching = this.getMatching(g2);
	child.addConnections(matching);

	double g1_fit = adjFitness(this);
	double g2_fit = adjFitness(g2);

	// If parents have equal fitness, randomly match excess genes
	if (g1_fit == g2_fit) {
		Random r = new Random();

		// Get excess from both parents
		ArrayList<ConnectionGene> excess = this.getExcess(g2);
		excess.addAll( g2.getExcess(this) );

		// Get disjoint from both parents
		ArrayList<ConnectionGene> disjoint = this.getDisjoint(g2);
		disjoint.addAll( g2.getDisjoint(this) );

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
		child.addConnections( this.getExcess(g2) );
		child.addConnections( this.getDisjoint(g2) );
	}
	else if (g1_fit < g2_fit) {
		child.addConnections( g2.getExcess(this) );
		child.addConnections( g2.getDisjoint(this) );
	}

	// Apply mutations
	child.mutate();



	return child;
}

public void mutate(double weight_val_rate, double dis_rate) {
	Random r = new Random();

	//double weight_val_rate = .10;

	// Mutations for input to hidden connections
	perturbLinks(input_nodes, hidden_nodes);

	// Mutations for hidden to hidden connections
	perturbLinks(hidden_nodes, hidden_nodes);

	// Mutations for hidden to output connections
	perturbLinks(hidden_nodes, output_nodes);

	// Mutations for input to output connections
	perturbLinks(input_nodes, output_nodes);

	// Mutate existing connections
	for ( ConnectionGene cg : connections ) {
		// Chance to change weight
		if ( r.nextDouble() < weight_val_rate )
		cg.weight = r.nextDouble();

		// Chance to enable or disable (flip) connection gene
		if ( r.nextDouble() < dis_rate )
		cg.enabled = !cg.enabled;
		//cg = cg.flipGene();
	}
}


//
private void perturbLinks (ArrayList<NodeGene> input_layer,
ArrayList<NodeGene> output_layer) {
	Random r = new Random();

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



}
