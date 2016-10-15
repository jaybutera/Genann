package Genetics;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Comparator;

public final class Genome {

    // These should be private
    public Random r = new Random();
    public Chromosome<ConnectionGene> chromosome;
    private int nodes = 1;
    private InnovationDB db;
    
    public Genome(int nodes, InnovationDB db){
        this(new Chromosome(), nodes, db);
    }
    public Genome(Chromosome<ConnectionGene> chromosome, int nodes, InnovationDB db) {
       this.chromosome = chromosome;
       this.nodes = nodes;
       this.db = db;
    }
    public Genome addNode(){
        return new Genome(chromosome, nodes+1, db);
    }
    public Genome addConnection(int from, int to, double weight){
        ConnectionGene conn = new ConnectionGene(from, to, weight, true);
        int id = db.get(conn);
        conn.setId(id);
        Chromosome<ConnectionGene> conns = chromosome.add(conn);
        return new Genome(conns, nodes, db);
    }
    public String toString(){
        return nodes+", "+chromosome.toString();
    }
   
}