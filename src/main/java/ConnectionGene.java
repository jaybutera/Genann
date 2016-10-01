public final class ConnectionGene {
  int id;
  float weight;
  boolean enabled;
  NodeGene to;
  NodeGene from;

  ConnectionGene(int id, float weight, boolean enabled, NodeGene to, NodeGene from){
    this.id = id;
    this.weight = weight;
    this.enabled = enabled;
    this.to = to;
    this.from = from;
  }
  ConnectionGene(ConnectionGene c){
    this(c.id,c.weight,c.enabled,c.to,c.from);
  }
  ConnectionGene flip(){
    return new ConnectionGene(c.id,c.weight,!c.enabled,c.to,c.from);
  }
  ConnectionGene setWeight(float w){
    return new ConnectionGene(c.id,w,c.enabled,c.to,c.from);
  }
  boolean isEnabled(){
    return enabled;
  }


}
