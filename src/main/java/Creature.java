public class Creature {
    Creature(Genome g) {
        this.g = g;
        net = new Network(g);
    }

    public Double fitness;
    public Genome g;
    public RNN net;
}
