package com.data_mining.logic;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.data_mining.constants.Notations;
import com.data_mining.constants.ValueConstants;
import com.data_mining.model.clusters.CluseterList;
import com.data_mining.model.clusters.Cluster;
import com.data_mining.model.clusters.ProximityMatrix;
import com.data_mining.model.clusters.ProximityType;

/**
 * Has functions for operations dealing with Agglomerative clustering
 * @author Janakiraman
 *
 */
public class AgglomerativeComputations {

	/**
	 * Computes the clusters using agglomerative clustering
	 * @param list
	 */
	public void computeClustersAgglomerative(CluseterList list)
	{
		while(list.size()>ValueConstants.K_NUMBER)
		{
			ProximityMatrix pMatrix = calculateProximityMatrix(list);
			
			Dimension dim = findMostSimilar(pMatrix);
			
			Cluster c1 = list.getClusterAt((int)dim.getWidth());
			Cluster c2 = list.getClusterAt((int)dim.getHeight());
					
			c1.addALLPoints(c2.getRecords());
			
			list.removeClusterAt((int)dim.getHeight());
		
		}
	}
	
	/**
	 * Find the most similar from the proximity matrix
	 * @param pMatrix
	 * @return dimension with two clusters
	 */
	public Dimension findMostSimilar(ProximityMatrix pMatrix)
	{
		double min = pMatrix.getValueAt(0, 1);
		int row,col;
		row = 0;
		col = 1;
		
		
		for(int i = 0;i<pMatrix.row();i++)
		{
			for(int j=i+1;j<pMatrix.col();j++)
			{
				if(min>pMatrix.getValueAt(i, j))
				{
					min = pMatrix.getValueAt(i, j);
					row = i;
					col = j;
				}
			}
		}
		
	return new Dimension(row, col);	
	}
	
	
	
	/**
	 * Calculates the Proximity matrix
	 * @param list of clusters
	 * @return proximity matrix
	 */
	public ProximityMatrix calculateProximityMatrix(CluseterList list)
	{
		ProximityMatrix pMatrix = new ProximityMatrix(list.size());
		
		for(int i=0;i<list.size();i++)
		{
			for(int j=i;j<list.size();j++)
			{
				if(Notations.proximityType==ProximityType.SingleLink)
				{
					pMatrix.setValue(singleLink(list.getClusterAt(i), list.getClusterAt(j)),i,j);
			
				}
				else if(Notations.proximityType==ProximityType.CompleteLink)
				{
					pMatrix.setValue(completeLink(list.getClusterAt(i), list.getClusterAt(j)),
							i,j);
			
				}
				else if(Notations.proximityType==ProximityType.GroupAverage)
				{
					pMatrix.setValue(groupAverage(list.getClusterAt(i), list.getClusterAt(j)),
							i, j);;
				}
			}
		}
		
		return pMatrix;
	}
	
	/**
	 * Calculates the single link  value between two clusters
	 * @param cluster 1
	 * @param cluster 2
	 * @return error
	 */
	public double singleLink(Cluster c1,Cluster c2)
	{
		
		ClusterLogics cl = new ClusterLogics();
		
		List<Double> values = 
				cl.findAllEucleideanDistances(c1, c2);
		
		return new ErrorsAndGain().roundOff(
				Collections.min(values),4);
				//,4);
	}
	
	/**
	 * Calculates the completelink between two clusters 
	 * @param c1
	 * @param c2
	 * @return value
	 */
	public double completeLink(Cluster c1,Cluster c2)
	{
		
		ClusterLogics cl = new ClusterLogics();
		
		List<Double> values = 
				cl.findAllEucleideanDistances(c1, c2);
		
	return  new ErrorsAndGain().roundOff
				(Collections.max(values),4);
	}
	
	/**
	 * computes group average between two clusters
	 * @param c1
	 * @param c2
	 * @return
	 */
	public double groupAverage(Cluster c1,Cluster c2)
	{
		ClusterLogics cl = new ClusterLogics();
		
		List<Double> values = 
				cl.findAllEucleideanDistances(c1, c2);
		
		double sum = 0.0;
		
		for(double value:values)
		{
			sum+=value;
		}
		
		return new ErrorsAndGain().roundOff(sum/(c1.size()*c2.size()),4);
	}
}
