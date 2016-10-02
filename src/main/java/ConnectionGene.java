public final class ConnectionGene {

    NodeGene from;
    NodeGene to;
    double weight;
    boolean enabled;
    int id;

    ConnectionGene(NodeGene from, NodeGene to, double weight, boolean enabled, int id) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.enabled = enabled;
        this.id = id;

    }

    ConnectionGene(ConnectionGene c) {
        this(c.from, c.to, c.weight, c.enabled, c.id);
    }

    ConnectionGene flip() {
        return new ConnectionGene(this.from, this.to, this.weight, !this.enabled, this.id);
    }

    ConnectionGene endable() {
        return new ConnectionGene(this.from, this.to, this.weight, true, this.id);
    }

    ConnectionGene disable() {
        return new ConnectionGene(this.from, this.to, this.weight, false, this.id);
    }

    ConnectionGene setWeight(double w) {
        return new ConnectionGene(this.from, this.to, w, this.enabled, this.id);
    }

    boolean isEnabled() {
        return enabled;
    }
    boolean equals(ConnectionGene c){
        return this.id == c.id;
    }

}
