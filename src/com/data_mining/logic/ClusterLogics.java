package com.data_mining.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

import com.data_mining.constants.Notations;
import com.data_mining.constants.ValueConstants;
import com.data_mining.logs.TrainingLog;
import com.data_mining.model.clusters.AttributeCluster;
import com.data_mining.model.clusters.BisectCluster;
import com.data_mining.model.clusters.BisectClusterList;
import com.data_mining.model.clusters.Centroid;
import com.data_mining.model.clusters.CluseterList;
import com.data_mining.model.clusters.Cluster;
import com.data_mining.model.clusters.DataCluster;
import com.data_mining.model.clusters.RandPoints;
import com.data_mining.model.clusters.RecordCluster;
import com.data_mining.model.clusters.TypeAttribute;

/**
 * Has most of the functions necessary for dealing with cluster operations
 * @author Janakiraman
 *
 */
public class ClusterLogics {
	
	
	/**
	 * Normalizes data in a cluster
	 * @param data
	 * @return
	 */
	public DataCluster normalizeData(DataCluster data)
	{
		DataCluster temp = new DataCluster();
		
		temp.setAttributes(data.getAttributes());
		
		List<Double> max = new ArrayList<Double>();
		List<Double> min = new ArrayList<Double>();
		
		
		for(int i=0;i<data.getAttributes().size();i++)
		{
			if(data.getAttributesat(i).getType()==TypeAttribute.continuous)
			{
				List<Double> listDouble = 
							data.getColumnAt(i);
				max.add(
				Collections.max(listDouble));
				
				min.add(
						Collections.min(listDouble));
		
			}
		}
		
		for(int k=0;k<data.sizeOfRecords();k++)
		{
			RecordCluster rec =new RecordCluster();
//			List<String> str = new ArrayList<>();
			int mark=0;
			
			for(int i=0;i<data.numberOfAttributes();i++)
			{
				if(data.getAttributes().get(i).getType()==TypeAttribute.continuous)
				{
					rec.addValue(new ErrorsAndGain().roundOff(
							(
									(Double.parseDouble(
									data.getRecordAt(k).getValueat(i))-min.get(mark))
									/(max.get(mark)-min.get(mark))
									),4
							));
					mark++;
					
					
				}
				else
				{
				rec.addValue(data.getRecordAt(k).getValueat(i));
				}
			}
			
			temp.addRecord(rec);
			
		}
		
		return temp;
	}
	
	/**Calculates SSE
	 * @param cluster
	 */
	public void calculateSSEForCluster(Cluster cluster)
	{
		double SSE = 0.0;

		for(int i=0;i<cluster.size();i++)
		{
			double temp = findEucleideanDistance(cluster.getCentroid().getAttrList(), 
					getPoints(cluster.getAttributes(), cluster.getRecords().get(i)));
			
			SSE += temp*temp;
			
		}
		
		cluster.setSSE(SSE);
		
	}
	
	
	/**
	 * True if equal else false
	 * @param centroids1
	 * @param centroids2
	 * @return
	 */
	public boolean centroidChange(List<Cluster> centroids1,List<Cluster> centroids2)
	{
		if(centroids1.size()==centroids2.size())
		{
			for(int i=0;i<centroids1.size();i++)
			{
				if(
				!compareTwoCentroids(centroids1.get(i).getCentroid(), centroids2.get(i).getCentroid()))
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * True if equal else false
	 * @param c1
	 * @param c2
	 * @return
	 */
	private boolean compareTwoCentroids(Centroid c1,Centroid c2)
	{
		if(c1.getAttrList().size()==c2.getAttrList().size())
		{
		for(int i=0;i<c1.getAttrList().size();i++)
		{
			if(
			
					c1.getAttrList().get(i).compareTo(c2.getAttrList().get(i))!=0)
			{
		
				return false;
			}
		}
		}
		else
		{
			TrainingLog.mainLogs.log(Level.INFO, "Mismatch in size");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Computes the clusters using Bisect k means
	 * @param Encapsulation of clusters
	 * @throws CloneNotSupportedException
	 */
	public void computeFinalClustersBisect(CluseterList list) throws CloneNotSupportedException
	{
		for(int i=0;list.size()<ValueConstants.K_NUMBER;i++)
		{
			int index = findHighestSSE(list);
			
			List<Cluster> clusterList = bisectCluster(list.getClusterAt(index));
			
			list.removeClusterAt(index);
				
			list.addAllCluster(clusterList);
			
		}
	}
	
	/**
	 * computes clusters using basic k means
	 * @param Cluster lists
	 * @param data for calculating
	 */
	public void computeFinalClusters(CluseterList list,DataCluster data)
	{
		addClusterPoints(list, data);
		CluseterList temp = null;
		
		do
		{
		try {
			temp = list.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		recomputingCentroids(list, data);;
		
		addClusterPoints(list, data);

	//	System.out.println("Iteration");
				
		}while(!centroidChange(list.getClusters(), temp.getClusters()));
		
		
	}
	
	/**
	 * Adds cluster points from data table
	 * @param list of clusters
	 * @param data
	 */
	public void addClusterPoints(CluseterList list,DataCluster data)
	{
		list.clearsClusterPoints();
		for(int i=0 ; i<data.sizeOfRecords() ; i++)
		{
			int temp = findClosestClusterAndAddPoint(list, 
					getPoints(data.getAttributes(), data.getRecordAt(i))
					);
			
			list.getClusterAt(temp).addPoints(data.getRecordAt(i));
		}
	}
	
	/**
	 * Add points from cluster to Bisect Cluster data model
	 * @param bisect
	 * @param data
	 */
	public void addClusterPoints(BisectCluster bisect,Cluster data)
	{
		CluseterList list = new CluseterList();
		list.addCluster(bisect.getC1());
		list.addCluster(bisect.getC2());
		
		list.clearsClusterPoints();
		
		for(int i=0 ; i<data.size() ; i++)
		{
			int temp = findClosestClusterAndAddPoint(list, 
					getPoints(data.getAttributes(), data.getRecords().get(i)
							)
					);
			
			list.getClusterAt(temp).addPoints(data.getRecords().get(i)
					);
		}
		

		
		calculateSSEForCluster(bisect.getC1());
	//	System.out.println(bisect.getC1().getSSE());
		calculateSSEForCluster(bisect.getC2());
		
		bisect.setTotalSSE(bisect.getC1().getSSE()+bisect.getC2().getSSE());
		
		
	}
	
	/**
	 * FInds the closest cluster and adds the point to it
	 * @param list
	 * @param dataPoints
	 * @return cluster number
	 */
	public int findClosestClusterAndAddPoint(CluseterList list,String[] dataPoints)
	{
	//	double min = findEucleideanDistance(list.getClusterAt(0).getCentroid().getAttrList(), dataPoints);
		int k = 0;
		double min = findEucleideanDistance(
										list.getClusterAt(0).getCentroid().getAttrList(), 
										dataPoints);
		for(int i=0;i<list.size();i++)
		{
			
			double temp = findEucleideanDistance(
					list.getClusterAt(i).getCentroid().getAttrList(), dataPoints);
		
			
			if(temp<min)
			{
				min = temp;
			//	System.out.println(min);
				k=i;
			}
			
		}
		
		return k;
	}

	/**
	 * Finds all the pairs of eucleidean distances between cluster 1 and cluster 2
	 * @param cluster 1
	 * @param cluster 2
	 * @return list of all distances
	 */
	public List<Double> findAllEucleideanDistances(Cluster cl1,Cluster cl2)
	{
		List<Double> values = new ArrayList<Double>();
		for(int i=0;i<cl1.size();i++)
		{
			for(int j=0;j<cl2.size();j++)
			{
				values.add(
						findEucleideanDistance(cl1.getRecordPointsAt(i), cl2.getRecordPointsAt(j)));
			}
		}
		return values;
	}
	
	public double findEucleideanDistance(String[] p1,Cluster cluster)
	{
		double sum = 0.0;
		for(int i=0;i<cluster.size();i++)
		{
			sum += findEucleideanDistance(p1, cluster.getRecordPointsAt(i));
			
		}
		
		return sum;
	}
	
	/**Finds distance between two points
	 * @param p1
	 * @param p2
	 * @return
	 */
	public double findEucleideanDistance(String[] p1,String[] p2)
	{
	
	Double dist = 0.0;
	if(p1.length==p2.length)
	{
		for(int i=0;i<p1.length;i++)
		{
			double temp = (Double.parseDouble(p1[i])
					- Double.parseDouble(p2[i])); 
			dist += temp*temp;
		}
				
				dist = Math.sqrt(dist);
	}
	else
	{
		System.out.println(p1.length+ " Size not equal "+p2.length);
	}
			
		return dist;
		
	}
	
	
	
	public double findEucleideanDistance(List<Double> p1,String[] p2)
	{
		Double dist = 0.0;
		if(p1.size()==p2.length)
		{
			for(int i=0;i<p1.size();i++)
			{
				double temp = (p1.get(i)
						- Double.parseDouble(p2[i])); 
				dist += temp*temp;
			}
			
			dist = Math.sqrt(dist);
		}
		else
		{
			System.out.println(p1.size()+ " Size not equal "+p2.length);
		}
		
		return dist;
	}
	
	/**
	 * Returns the list of only points excluding the class label and point ID
	 * @param data
	 * @return
	 */
	public List<String[]> findPoints(DataCluster data)
	{
		List<String[]>
				list = new ArrayList<String[]>();
		for(int i = 0;i<data.sizeOfRecords();i++)
		{
			list.add(getPoints(data.getAttributes(), data.getRecordAt(i)
					));
		}
		
		return list;
	}
	
	public List<String[]> findPoints(Cluster data,List<AttributeCluster> attrbs)
	{
		List<String[]>
				list = new ArrayList<String[]>();
		for(int i = 0;i<data.size();i++)
		{
			list.add(getPoints(attrbs, data.getRecords().get(i)
					));
		}
		
		return list;
	}
	
	public int getNumberOfPoints(List<AttributeCluster> attrb)
	{
		int count = 0;
		
		for(int i=0;i<attrb.size();i++)
		{
			if(attrb.get(i).getType()==TypeAttribute.continuous)
				count++;
			
		}
		
		return count;
	}
	
	/**
	 * Gets the point in record excluding ID and Class
	 * @param attrb header
	 * @param recCluster
	 * @return array of points
	 */
	public String[] getPoints(List<AttributeCluster> attrb,RecordCluster recCluster)
	{
		List<String> strlist = new ArrayList<String>();
		
		for(int i=0;i<
				attrb.size()
				;i++)
		{
			if(attrb.get(i).getType()==TypeAttribute.continuous)
			{
				strlist.add(recCluster.getValueat(i));
				
			}
		}
		
		return (String[]) strlist.toArray(new String[0]);
	}
	
	/**
	 * Recomputes centroid points
	 * @param clusterList
	 * @param data
	 */
	public void recomputingCentroids(CluseterList clusterList,DataCluster data)
	{
		for(int i = 0;i<clusterList.size();i++)
		{
			alterCentroid(clusterList.getClusterAt(i), data.getAttributes());
		}
	}
	
	public void recomputingCentroids(BisectCluster bisect,Cluster data)
	{
		CluseterList list = new CluseterList();
		list.addCluster(bisect.getC1());
		list.addCluster(bisect.getC2());
		
		for(int i = 0;i<list.size();i++)
		{
			alterCentroid(list.getClusterAt(i), data.getAttributes());
		}
	}
	
	private void alterCentroid(Cluster cluster,List<AttributeCluster> attrb)
	{
		if(cluster.size()>0)
		{
		List<Double> attr = new ArrayList<>();
		for(int i=0;i<attrb.size();i++)
		{
			if(attrb.get(i).getType()==TypeAttribute.continuous)
			{
				List<Double> mean = cluster.getColumnAt(i);
				attr.add(new CommonLogics().mean(mean));
			}
		}
		
		
		cluster.setCentroid(new Centroid(attr));
		}
		

	}
	
	/**
	 * Finds the mean of all points
	 * @param str
	 * @return
	 */
	private double[] mean(List<String[]> str)
	{
		double mean[] = new double[str.get(0).length];
	
		for(int i = 0;i<str.size();i++)
		{
			for(int j=0;j<mean.length;j++)
			{
				mean[j]=Double.parseDouble(str.get(i)[j]);
			}
		}
		for(int i=0;i<mean.length;i++)
		{
			mean[i] = mean[i]/mean.length;
		}
		
		return mean;
	}
	
	/**
	 * get column of values from a list of records
	 * @param recList
	 * @param index
	 * @return
	 */
	public List<String> getColumnFromRecordList(List<RecordCluster> recList,int index)
	{
		List<String> list = new ArrayList<String>();
		
		for(int i=0;i<recList.size();i++)
		{
			list.add(recList.get(i).getValueat(index)
					);
		}
		
		return list;
	}

	/**
	 * Finds index of cluster with highest SSE
	 * @param clusterList
	 * @return index
	 */
	public int findHighestSSE(CluseterList clusterList)
	{
		double high = clusterList.getClusterAt(0).getSSE();
		int count = 0;
		
		for(int i = 0;i<clusterList.size();i++)
		{
			if(clusterList.getClusterAt(i).getSSE()>high)
			{
				high = clusterList.getClusterAt(i).getSSE();
				count = i;
			}
		}
		
		return count;
	}
	
	
	
		
	/**
	 * Bisects a cluster to give two cluster in  a list
	 * @param cluster
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public List<Cluster> bisectCluster(Cluster cluster) throws CloneNotSupportedException
	{
		Cluster c1 = new Cluster(
				new CommonLogics().generateRandomNumbers(1, cluster.numberOfPoints()),0);
		
		Cluster c2 = new Cluster(
				new CommonLogics().generateRandomNumbers(1, cluster.numberOfPoints()),0);
		
		c1.setAttributes(cluster.getAttributes());
		c2.setAttributes(cluster.getAttributes());
		
		BisectClusterList blist = new BisectClusterList();
		
		
		BisectCluster bisect 
					 = new BisectCluster(c1.clone(), c2.clone());
		
		addClusterPoints(bisect, cluster);
		blist.addBisectCluster(bisect);
		
		for(int i=0 ; i<ValueConstants.TRIALS ; i++)
		{
			Cluster cc1 = bisect.getC1().clone();
			Cluster cc2 = bisect.getC2().clone();
			
			BisectCluster bisectt = new BisectCluster(cc1, cc2);
				
			recomputingCentroids(bisectt, cluster);
			
			addClusterPoints(bisectt, cluster);
			blist.addBisectCluster(bisectt);
		//	blist.getBisectList().get(i).setTotalSSE(bisectt.getC1().getSSE()+bisectt.getC2().getSSE());
			
		}
		
		int lowest = 0;
		
		for(int i=0;i<blist.size();i++)
		{
			if(blist.SSEat(i)<blist.SSEat(lowest))
			{
				lowest = i;
			}
		}
		
		CluseterList clist = new CluseterList();
		clist.addCluster(blist.getBisectList().get(lowest).getC1());
		clist.addCluster(blist.getBisectList().get(lowest).getC2());
		
				
		return clist.getClusters();
	}
	
	
	public List<Double> getColumnFromRecordListDouble(
			List<RecordCluster> recList, int index) {
	
		
		List<Double> list = new ArrayList<Double>();
		
		for(int i=0;i<recList.size();i++)
		{
			list.add(Double.parseDouble(recList.get(i).getValueat(index)
					));
		}
		
		return list;
		
	}
	
	/**
	 * Assigns the class labels to clusters
	 * @param list
	 */
	public void assignClusterClass(CluseterList list)
	{
		for(int i=0;i<list.size();i++)
		{
			assignClusterClass(list.getClusterAt(i));
		}
	}
	
	public void assignClusterClass(Cluster cluster)
	{
		List<String> classString = cluster.getColumnAtString(cluster.getAttributes().size()-1);
		
		Set<String> set = new TreeSet<>();
		
		for(String str:classString)
		{
			set.add(str);
		}
		
		String name = null;
		try
		{
		name = set.toArray(new String[0])[0];
		}catch(ArrayIndexOutOfBoundsException ae)
		{
			TrainingLog.mainLogs.info("Array index out of bounds excepiton");
			cluster.setClassName(Notations.DEFAULT_CLASS);
			return;
					
		}
		int count = CommonLogics.numberOfInstance(classString, name);
		
		Iterator<String> iter = set.iterator();
		
		while(iter.hasNext())
		{
			String temp = iter.next();
			int cct = CommonLogics.numberOfInstance(classString, temp);
			
			if(cct>count)
			{
				name = temp;
				count = cct;
			}
		}
		
		cluster.setClassName(name);
		
	}
	
	/**
	 * Gets the maximum repeated String
	 * @param list
	 * @return
	 */
	public String getMaxClass(List<String> list)
	{
		Set<String> set = new TreeSet<>(list);
		
		Iterator<String> keys = set.iterator();
		
		int max = 0;
		String name = null;
		
		while(keys.hasNext())
		{
			String key = keys.next();
			int temp = CommonLogics.numberOfInstance(list, key);
			if(temp>max)
			{
				max = temp;
				name = key;
			}
		}
		
		return name;
		
	}
	
	/**
	 * Computes the Random points to fill centroid positions
	 * @param list
	 * @return
	 */
	public List<RandPoints> computeRandPoints(CluseterList list)
	{
		List<RandPoints> listPoints = new ArrayList<>();
		
		for(int i=0;i<list.size();i++)
		{
			for(int j=0;j<list.getClusterAt(i).size();j++)
			{
				listPoints.add(
					new RandPoints(
				list.getClusterAt(i).getRecords().get(j).getClassName(), 
				list.getClusterAt(i).getClassName(), 
				list.getClusterAt(i).getRecords().get(j).getValueat(0)
							));
			}
		}
		
		return listPoints;
	}
	
	
}
