package Genetics;

import Genetics.ConnectionGene;
import java.util.Hashtable;

public class InnovationDB {

    private int nextId = 0;
    public Hashtable<Gene, Integer> db;

    public InnovationDB() {
        db = new Hashtable();
    }

    //returns inv id if innovation has not occurred

    int get(Gene g) {
        if (db.containsKey(g)) {
            return db.get(g);
        }
        db.put(g, nextId);

        return nextId++;

    }

}
