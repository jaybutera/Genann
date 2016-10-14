
import Genetics.ConnectionGene;
import Genetics.Chromosome;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TestGA {
    public static void main(String args[]) {
        int nodes = 6;
        int inputs = 3;
        int outputs = 3;
        int inv = 0;
        Chromosome<ConnectionGene> connections;
        Random r = new Random();
        ArrayList<ConnectionGene> init = new ArrayList();
        for(int i = 0; i < inputs; i++){
            for(int j = 0; j < outputs; j++){
                init.add(new ConnectionGene(i,j,r.nextDouble(),true, inv++));
            }
        }
        connections = new Chromosome(init);
        System.out.println(init);
    }
//    public static void XOR(){
//        XOR f = new XOR();
////        NEATPop p = new NEATPop(100, .1, .1, .1, .3, 1.3, 2, 1, f);
//
//        p = p.nextGen();
//        Genome init_fit = p.getMostFit();
//
//        // Run through 100 generations
//        for (int i = 0; i < 5; i++) {
//            System.out.println("Running generation " + (i+1) + "...");
//            //System.out.println("Number of species: " + p.getNumSpecies());
//            p = p.nextGen();
//            System.out.println("Most fit genome:\n\n" + p.getMostFit());
//            System.out.println("Top fitness: " + f.simulate( new Network(p.getMostFit()) ));
//        }
//
//        Genome g = p.getMostFit();
//        System.out.println(g);
//        Network n = new Network(g);
//        System.out.println(n);
//        System.out.print("Output: ");
//        Double[] outs = f.getOuts();
//        for (int i = 0; i < outs.length; i++)
//            System.out.print(outs[i] + ", ");
//        System.out.println("");
//        //System.out.println("Convergence success: " + (1 - g.fitness / init_fit.fitness) + " %");
//    }
    
}
