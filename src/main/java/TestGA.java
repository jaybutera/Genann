import Genetics.ConnectionGene;
import Genetics.Chromosome;
import Genetics.Genome;
import Genetics.InnovationDB;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class TestGA {

    public static void main(String args[]) {
        int nodes = 6;
        int inputs = 3;
        int outputs = 3;
        InnovationDB db = new InnovationDB();

        Chromosome<ConnectionGene> connections = new Chromosome();
        ArrayList<Genome> pop = new ArrayList();
        
        Supplier<Genome> factory = () -> {
            final Random r = new Random();
            final ArrayList<ConnectionGene> init = new ArrayList();
            Genome g = new Genome(connections, nodes, db);
            for (int i = 0; i < inputs; i++) {
                for (int j = outputs; j < nodes; j++) {
                    g = g.addConnection(i, j, r.nextDouble());
                }
            }
            return g;
        };
        pop.add(factory.get());
        pop.add(factory.get());

        pop.forEach((e) -> System.out.println(e));

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
