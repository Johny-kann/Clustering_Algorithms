package com.data_mining.model.clusters;

import java.util.ArrayList;
import java.util.List;

/**
 * List of Bisect clusters
 * @author Janakiraman
 *
 */
public class BisectClusterList {

	private List<BisectCluster> bisectList;
	
	
	public BisectClusterList()
	{
		bisectList = new ArrayList<BisectCluster>();
	}
	
	public void addBisectCluster(BisectCluster bisect)
	{
		bisectList.add(bisect);
	}

	public List<BisectCluster> getBisectList() {
		return bisectList;
	}
	
	public double SSEat(int index)
	{
		return bisectList.get(index).getTotalSSE();
	}
	
	public int size()
	{
		return bisectList.size();
	}
}
