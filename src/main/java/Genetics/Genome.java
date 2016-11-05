package Genetics;

import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;
import java.util.Stack;

public class Genome {

    /**
     * @return the connDB
     */
    public static InnovationDB getConnDB() {
        return connDB;
    }

    /**
     * @param aConnDB the connDB to set
     */
    public static void setConnDB(InnovationDB aConnDB) {
        connDB = aConnDB;
    }

    /**
     * @return the splitDB
     */
    public static InnovationDB getSplitDB() {
        return splitDB;
    }

    /**
     * @param aSplitDB the splitDB to set
     */
    public static void setSplitDB(InnovationDB aSplitDB) {
        splitDB = aSplitDB;
    }

    // These should be private
    public Random random = new Random();
    public Chromosome<Gene> chromosome;
    protected int nodes = 1;
    private static InnovationDB connDB = new InnovationDB();
    private static InnovationDB splitDB = new InnovationDB();
    private double mrate = .2;
    private double srate = .3;

    public Genome(int nodes) {
        this(new Chromosome(), nodes);
    }

    public Genome() {
        this(0);
    }

    public Genome(Chromosome chromosome, int nodes) {
        this.chromosome = chromosome;
        this.nodes = nodes;
    }

    public Genome addNode() {
        nodes += 1;
        return this;
    }

    public Genome addAll(ArrayList<? extends Gene> genes) {
        for (Gene g : genes) {
            this.add(g);
        }
        return this;
    }

    public Genome add(Gene g) {
        int id = getConnDB().fget(g);
        g.setId(id);
        chromosome.add(g);
//        System.out.println("Adding new Gene " + g + " \n" + this);
        return this;
    }

    public Genome remove(Gene g) {
        chromosome.remove(g);
        return this;
    }

    @Override
    public String toString() {
        return nodes + ", " + chromosome.toString();
    }

    /**
     * Only parts that cannot be separated from ConnectionGene Splitting is a
     * feature of only connection genes maybe in the future we can make this
     * less specific
     *
     * @param c
     * @return
     */
    protected ArrayList<ConnectionGene> split(ConnectionGene c) {
//        System.out.println("Checking "+c+" against "+this);
        ArrayList<ConnectionGene> gs = new ArrayList();
        if (this.chromosome.contains(c)) {
            this.addNode();
            System.out.println("Splitting " + c + " with node" + nodes);

            gs.addAll(c.split(nodes));
            this.addAll(gs);
            this.remove(c);
        }

        return gs;
    }

    public Genome split() {
        Stack<Gene> removed = new Stack(); //stack of at most one of each connection
        for (Gene g : this.chromosome) {
            if (this.srate > this.random.nextDouble()) { //pick random connections to split
                removed.push(g);
            }
        }
        ConnectionGene sgene;
        while (!removed.isEmpty()) {
            sgene = (ConnectionGene) removed.pop();
            this.split(sgene);

        }
        return this;
    }

    public Genome mutate() {
        this.chromosome.mutate(mrate);
        this.split();
        return this;

    }

    @Override
    public Genome clone() {
        return new Genome(this.chromosome.clone(), nodes);
    }

    public boolean equals(Object o) {
        Genome c = (Genome) o;
        return c.chromosome.equals(this.chromosome);
    }

    public static void resetDB() {
        connDB = new InnovationDB();
        splitDB = new InnovationDB();
    }

}
