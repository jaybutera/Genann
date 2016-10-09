public class NodeGene {


    public int id;

    NodeGene(int geneCount){

        this.id = ++geneCount;
    }

    public boolean equals (ConnectionGene c){
        return this.id == c.id;
    }
}
