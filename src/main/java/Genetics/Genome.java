package Genetics;

import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;

public class Genome {

    // These should be private
    public Random random = new Random();
    public Chromosome<Gene> chromosome;
    private int nodes = 1;
    private InnovationDB db;
    private double mrate = .2;
    private double srate = .3;

    public Genome(int nodes, InnovationDB db) {
        this(new Chromosome(), nodes, db);
    }

    public Genome(Chromosome chromosome, int nodes, InnovationDB db) {
        this.chromosome = chromosome;
        this.nodes = nodes;
        this.db = db;
    }

    public Genome addNode() {
        nodes += 1;
        return this;
    }

    public Genome addAll(ArrayList genes) {
        chromosome.addAll(genes);
        return this;
    }

    public Genome add(Gene g) {
        int id = db.get(g);
        g.setId(id);
        chromosome.add(g);
        System.out.println(chromosome);

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

    @Override
    public Genome clone() {
        return new Genome(this.chromosome, nodes, db);
    }

    /**
     * Only parts that cannot be separated from ConnectionGene Splitting is a
     * feature of only connection genes maybe in the future we can make this
     * less specific
     *
     * @return
     */
    public Genome split(ConnectionGene c) {
        this.addAll(c.split(nodes));
        this.remove(c);

        return this;
    }

    public Genome mutate() {
        this.chromosome.mutate(mrate);
        if (this.srate < this.random.nextDouble()) {

        }

        return this;

    }

}
