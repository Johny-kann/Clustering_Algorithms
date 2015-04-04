package com.data_mining.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.data_mining.logs.TrainingLog;
import com.data_mining.model.clusters.CluseterList;
import com.data_mining.model.clusters.Cluster;
import com.data_mining.model.clusters.RandPoints;

/**
 * @author Janakiraman
 *Errors and gain class
 */
public class ErrorsAndGain {

	public double roundOff(double x, int position)
    {
        double a = x;
        double temp = Math.pow(10.0, position);
        a *= temp;
        a = Math.round(a);
        return (a / (double)temp);
    }

	
	/**
	 * @param map of categories and their counts
	 * @return classification error
	 */
	public Double classificationError(Map<String,Integer> categories)
	{
		
		List<Integer> list = mapToList(categories);
		Integer max = Collections.max(list);
		Integer tot = listToTotal(list);
		
		double error =  1 - (double) max/tot;
		return roundOff(error, 8);
	}
	
	/**
	 * @param map of categories and their counts
	 * @return gini error
	 */
	public Double giniError(Map<String,Integer> categories)
	{
		
		List<Integer> list = mapToList(categories);
		Integer tot = listToTotal(list);
		
		Double gini = 1.0;
		
		for(Integer i : list)
		{
			gini -= (double)i/tot*(double)i/tot;
		}
		
//		double error =  1 - (double) max/tot;
		return roundOff(gini, 8);
	}
	
	/**
	 * @param map
	 * @return list
	 */
	public List<Integer> mapToList(Map<String,Integer> categor)
	{
		Set<String> keys = categor.keySet();
		List<Integer> classes = new ArrayList<Integer>();
		
		for(String key:keys)
		{
			classes.add(categor.get(key));
		}
		
		return classes;
	}
	
	/**
	 * @param nums
	 * @return sum
	 */
	public Integer listToTotal(List<Integer> nums)
	{
		Integer sum = 0;
		
		for(Integer num:nums)
		{
			sum+=num;
		}
		
		return sum;
	}
	
	
	/**
	 * @param errors
	 * @param records
	 * @param totalParentRecords
	 * @return error of split
	 */
	public Double errorSplit(List<Double> errors,List<Integer> records,Integer totalParentRecords)
	{
		Double error = 0.0;
		Double numRec = 0.0;
		Double totRec = 0.0;
		
		for(int i=0;i<errors.size();i++)
		{
			numRec = (double)records.get(i);
//			System.out.println(numRec);
			totRec = (double)totalParentRecords;
//			System.out.println(totRec);
			error += numRec/totalParentRecords*errors.get(i);
//			System.out.println("Error" + error);
		}
		
	
		return roundOff(error, 8);
	}
	
	/**
	 * @param records
	 * @param totalParentRecords
	 * @param parent error
	 * @param errorSplit
	 * @return gain ratio
	 */
	public Double gainRatio(List<Integer> records,Integer totalParentRecords,Double pError,Double errorSplit)
	{
		Double gain = pError - errorSplit;
		return gain/splitInfo(records,totalParentRecords);
		
	}
	
	/**
	 * @param records
	 * @param totalParentRecords
	 * @return split information
	 */
	public Double splitInfo(List<Integer> records,Integer totalParentRecords)
	{
		Double error = 0.0;
		Double numRec = 0.0;
		Double totRec = 0.0;
		
		for(int i=0;i<records.size();i++)
		{
		 error -= ((double) records.get(i)/totalParentRecords) * Math.log(
				 (double) records.get(i)/totalParentRecords)/
				 Math.log(2);
		}
		
		return roundOff(error, 8);
	}
	
	/**
	 * @param errorParent
	 * @param errorSplit
	 * @return gain
	 */
	public Double gainCalculator(Double errorParent,Double errorSplit)
	{
		return errorParent - errorSplit;
	}
	
	/**
	 * @param errorParent
	 * @param parentRecords
	 * @param list of ChildRecords
	 * @param list of ChildErrors
	 * @return gain
	 */
	public Double gainCalculator(double errorParent,Integer parentRecords,List<Integer> listChildRecordsNum,List<Double> listChildErrors)
	{
		double errorSplit = 0;
		for(int i = 0; i<listChildRecordsNum.size();i++)
		{
			errorSplit += listChildErrors.get(i)*listChildRecordsNum.get(i);
		}
		
//		System.out.println(errorSplit/parentRecords);
		return roundOff(errorParent - errorSplit/parentRecords,4);
	}
	
	public Double laplace(Integer correctClassNo,Integer noCoveredbyRule,Integer noOfClassValues)
	{
	
	//	System.out.println(correctClassNo+"1"+"/"+noCoveredbyRule+"+"+noOfClassValues);
		return (double)(correctClassNo+1)/(noCoveredbyRule+noOfClassValues);
	}
	
	
	/**
	 * Computes the Random Statistics
	 * @param list
	 * @return
	 */
	public Double RandStatistic(List<RandPoints> list)
	{
		int f00=0;
		int f01=0;
		int f10=0;
		int f11=0;
		
		for(int i=0;i<list.size();i++)
		{
			for(int j=i+1;j<list.size();j++)
			{
				if(i!=j)
				{
					if(list.get(i).getClassName().equalsIgnoreCase(list.get(j).getClassName())
						&& list.get(i).getClusterName().equalsIgnoreCase(list.get(j).getClusterName())	
							)
					{
						f11++;
					}
					else if(
							list.get(i).getClassName().equalsIgnoreCase(list.get(j).getClassName())
							&& !list.get(i).getClusterName().equalsIgnoreCase(list.get(j).getClusterName())
							)
					{
						f10++;
					}
					else if(
							!list.get(i).getClassName().equalsIgnoreCase(list.get(j).getClassName())
							&& list.get(i).getClusterName().equalsIgnoreCase(list.get(j).getClusterName())
							)
					{
						f01++;
					}
					else if(
							!list.get(i).getClassName().equalsIgnoreCase(list.get(j).getClassName())
							&& !list.get(i).getClusterName().equalsIgnoreCase(list.get(j).getClusterName())
							)
					{
						f00++;
					}
				}
			}
		}
		
		return roundOff(
				(double)(f00+f11)/(double)(f00+f01+f10+f11),6);
	}
	
	/**
	 * silhoute coefficient of the cluster list
	 * @param list of clusters
	 * @return
	 */
	public Double calculateSilhoute(CluseterList list)
	{
		double sum=0.0;
		int numberOfPoints = 0;
		
		for(int i = 0;i<list.size();i++)
		{
			for(int j=0;j<list.getClusterAt(i).size();j++)
			{
				sum+=calculateSilhoute(list.getClusterAt(i).getRecordPointsAt(j), i, list);
				
			}
			numberOfPoints+=list.getClusterAt(i).size();
	
						
		}
		
		return sum/numberOfPoints;
		
		
	}
	
	/**
	 * Silhoute coefficient for the cluster
	 * @param cluster
	 * @param index of the cluster in the list of clusters
	 * @param list
	 * @return
	 */
	public Double calculateSilhoute(Cluster cluster,int index,CluseterList list)
	{
		double silhoute = 0.0;
		for(int i=0;i<cluster.size();i++)
		{
			silhoute += calculateSilhoute(cluster.getRecordPointsAt(i), index, list);
			
		}
		
		return silhoute/cluster.size();
	}
	
	
	/**
	 * Calculates the silhoute for a point
	 * @param The point for which silhoute is to be calculated
	 * @param cluster index which the point belongs to 
	 * @param encapsulation of cluster list
	 * @return
	 */
	public Double calculateSilhoute(String points[],int clusterIndex,CluseterList list)
	{
		double a,b;
		
		Cluster cluster = list.getClusterAt(clusterIndex);
		ClusterLogics cl = new ClusterLogics();
		
		double sum = cl.findEucleideanDistance(points, cluster);
		
		a = sum/(cluster.size());
		
		sum=0.0;
		
		List<Double> values = new ArrayList<>();
		
		for(int i=0;i<list.size();i++)
		{
			if(i!=clusterIndex)
			{
				values.add(
						(cl.findEucleideanDistance(points, list.getClusterAt(i)))/list.getClusterAt(i).size()
						);
			}
		}
		
		b = Collections.min(values);
		
		if(a>b)
		{
		
			TrainingLog.accuracyLogs.info("A>B unusual case");
			return a/b-1;
		}
		else
		{
			return 1-a/b;	
		}
		
		
	}
}
