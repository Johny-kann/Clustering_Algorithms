package com.data_mining.model.clusters;

/**
 * Special cluster which calculates the two clusters to bisect from main cluster in Bisect k means
 * @author Janakiraman
 *
 */
public class BisectCluster {

	private Cluster c1;
	private Cluster c2;
	private Double totalSSE;
	
	public BisectCluster(Cluster c1,Cluster c2)
	{
		this.c1 = c1;
		this.c2 = c2;
	}
	
	public Double getTotalSSE() {
		return totalSSE;
	}
	public void setTotalSSE(Double totalSSE) {
		this.totalSSE = totalSSE;
	}

	public Cluster getC1() {
		return c1;
	}

	public Cluster getC2() {
		return c2;
	}

	public void setC1(Cluster c1) {
		this.c1 = c1;
	}

	public void setC2(Cluster c2) {
		this.c2 = c2;
	}
	
	
}
