package Genetics;

import Genetics.ConnectionGene;
import java.util.Hashtable;

public class InnovationDB {

    private int nextId = 0;
    private Hashtable<Gene, Integer> db;

    InnovationDB() {
        db = new Hashtable();
    }
    //returns inv id if innovation has not occurred
    int get(Gene g) {
        if(db.contains(g)){
            return db.get(g);
        }
        db.put(g, nextId);
        return nextId++;
        
    }


}
