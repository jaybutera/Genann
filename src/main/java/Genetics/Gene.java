package Genetics;

import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author shanemendez
 */
public abstract class Gene implements Comparable{
    protected boolean enabled;
    protected int id;

    public Gene(boolean enabled, int id) {
        this.id = id;
        this.enabled = enabled;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return this.id == ((Gene)obj).id;
    }
    int getId(){
        assert(id > 0); //be sure the id is non negative
        return id;
    }
    void setId(int id){
        this.id = id;
    }
    
    boolean isEnabled() {
        return enabled;
    }
    @Override
    public int compareTo(Object o){
        return this.id - ((Gene)o).id;
    }
    
    public abstract Gene mutate();
    public abstract Gene setEnabled(boolean e);

  
}
