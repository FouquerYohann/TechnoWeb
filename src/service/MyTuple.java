package service;

import org.bson.types.ObjectId;

public class MyTuple implements Comparable<MyTuple> {
    private double weight;
    private ObjectId obid;
    
    
    public MyTuple() {
	super();
	this.obid = null;
	this.weight = 0.;
    }
    
    public MyTuple(ObjectId obid, double frq) {
	super();
	this.obid = obid;
	this.weight = frq;
    }
    
    public String getObid() {
        return obid.toString();
    }
    public void setObid(ObjectId obid) {
        this.obid = obid;
    }
    public double getWeight() {
        return weight;
    }
    public void setFrq(double frq) {
        this.weight = frq;
    }

    @Override
    public int compareTo(MyTuple o) {
	return Double.compare(this.weight,o.getWeight());
    }
    
    
}
