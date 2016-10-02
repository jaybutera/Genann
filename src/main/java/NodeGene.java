public class NodeGene {


    public int id;

    NodeGene(int geneCount){
        this.id = geneCount;
    }
    boolean equals(NodeGene c){
        return this.id == c.id;
    }
}
