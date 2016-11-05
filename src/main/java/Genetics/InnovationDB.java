package Genetics;

import Genetics.ConnectionGene;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * 
 * @author shanemendez
 * used to assign ID's to new genes.
 */
public class InnovationDB {

    private int nextId = 0;
    private Hashtable<Gene, Integer> db;

    public InnovationDB() {
        db = new Hashtable();
    }
    public InnovationDB(Hashtable db){
        this.db = db;
    }

    //innovation has occured if gene hash exists inside hashtable
    //adds the gene to the table if it has not occurred.
    public int get(Gene g) {
        if (db.containsKey(g)) {
            return db.get(g);
        }
        throw new IllegalAccessError("Gene "+g+" not found in db");
    }
    public void add(Gene g){
        db.put(g, nextId);
        nextId++;
    }
    public int fget(Gene g){
        try{
            return get(g);
        }
        catch(IllegalAccessError e){
            add(g);
        }
        return nextId -1; // element g will have this id iff it wasnt found by get
    }

    public int size() {
        return db.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InnovationDB other = (InnovationDB) obj;
        return this.db.equals(other.getDB());
    

    }

    public Hashtable getDB() {
        return this.db;
    }
    public String toString(){
        return db.toString();
    }

}
