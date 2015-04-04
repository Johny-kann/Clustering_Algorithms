package com.data_mining.model.clusters;

import java.util.ArrayList;
import java.util.List;



/**
 * List of clusters 
 * @author Janakiraman
 *
 */
public class CluseterList implements Cloneable{

	private List<Cluster> clusters;
	private double totalSilhouteCoefficient;
	
	public CluseterList()
	{
		clusters = new ArrayList<Cluster>();
		
	}
	
	@Override
	public CluseterList clone() throws CloneNotSupportedException {
	      
		CluseterList clonedCustomer = new CluseterList();
		
	//	List<Cluster> clonedClusters = new ArrayList<Cluster>();
		
		for(Cluster cl:clusters)
		{
//			clonedClusters.add(cl.clone());
			clonedCustomer.addCluster(cl.clone());
		}
	       
	      return clonedCustomer;
	   }
	
	public void addCluster(Cluster cluster)
	{
		clusters.add(cluster);
	}
	
	public void addAllCluster(List<Cluster> clusterList)
	{
		clusters.addAll(clusterList);
	}
	
	public void removeClusterAt(int index)
	{
		clusters.remove(index);
	}
	
	public Cluster getClusterAt(int i)
	{
		return clusters.get(i);
	}
	
	public void clearsClusterPoints()
	{
		for(int i=0;i<clusters.size();i++)
		{
			clusters.get(i).clearRecords();
		}
	}
	
	public int size()
	{
		return clusters.size();
	}

	public List<Cluster> getClusters() {
		return clusters;
	}

	public double getTotalSilhouteCoefficient() {
		return totalSilhouteCoefficient;
	}

	public void setTotalSilhouteCoefficient(double totalSilhouteCoefficient) {
		this.totalSilhouteCoefficient = totalSilhouteCoefficient;
	}
}
