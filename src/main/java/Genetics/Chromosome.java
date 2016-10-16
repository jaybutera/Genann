package Genetics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author shanemendez
 */
public class Chromosome<T extends Gene> implements Iterable<T> {

    ArrayList<T> genes = new ArrayList();

    public Chromosome() {

    }

    public Chromosome(ArrayList<T> temp) {
        this.genes = temp;
    }

    /**
     * [0] matching [1] disjoint
    *
     */
    private ArrayList<ArrayList<T>> complements(Chromosome mate) {
        ArrayList<ArrayList<T>> temp = new ArrayList(2);
        genes.forEach((T e) -> {
            if (this.contains(e)) {
                temp.get(0).add(e);
            } else {
                temp.get(1).add(e);
            }

        });
        return temp;
    }

    Chromosome crossover(Chromosome mate) {
        ArrayList<ArrayList<T>> cSets = this.complements(mate);
        return new Chromosome(cSets.get(0));
    }

    Chromosome mutate(double mrate) {
        ArrayList temp = new ArrayList();
        Random random = new Random();
        for (T g : genes) {
            Gene gm = g;
            if (mrate > random.nextDouble()) {
                gm = g.mutate();
            }
            temp.add(gm);
        }
        this.genes = temp;
        return this;
    }

    boolean contains(T mate) {
        return genes.contains(mate);
    }

    public Chromosome add(T gene) {
        this.genes.add(gene);
        return this;
    }
    public Chromosome addAll(Collection<T> genes){
        this.genes.addAll(genes);
        return this;
    }

    @Override
    public String toString() {
        return genes.toString();
    }

    public T get(int i) {
        return genes.get(i);
    }

    @Override
    public Iterator<T> iterator() {
        return genes.iterator();
    }
    public Chromosome remove(T gene){
        this.genes.remove(gene);
        return this;
    }
    public Chromosome removeAll(Collection<T> genes){
        this.genes.removeAll(genes);
        return this;
    }
    public Chromosome clone(){
        return new Chromosome(this.genes);
    }

}
