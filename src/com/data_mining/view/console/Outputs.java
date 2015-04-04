package com.data_mining.view.console;

import com.data_mining.constants.Notations;
import com.data_mining.model.clusters.CluseterList;
import com.data_mining.model.clusters.Cluster;
import com.data_mining.model.clusters.DataCluster;
import com.data_mining.model.clusters.ProximityMatrix;
import com.data_mining.model.clusters.RecordCluster;

public class Outputs {

	/**
	 * prints the table
	 * @param table
	 * @return gives the string
	 */
	
	public String outPutTable(DataCluster table)
	{
		String str = "";
		Integer row = table.sizeOfRecords();
		Integer col = table.numberOfAttributes();
	
		
		for(int i=0;i<col;i++)
		{
		
		str += (
					table.getAttributesat(i).getItemName()+"\t\t"
					);
	//	
		}
		str+=System.lineSeparator();
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<col;j++)
			{
				
				
				str+=table.getRecordAt(i).
						getValueat(j)+"\t\t";
				
		}
			str+=System.lineSeparator();
		}
		return str;
	}
	

	public String outputClusterList(CluseterList list)
	{
		StringBuffer stb = new StringBuffer();
		for(int i=0;i<list.size();i++)
		{
			stb.append(System.lineSeparator());
			stb.append("Cluster "+i);
			stb.append(System.lineSeparator());
			stb.append(outputCluster(
					list.getClusterAt(i))
					);
			
		}
		
		return stb.toString();
	}
	
	public String outputCluster(Cluster cluster)
	{
		StringBuffer stb = new StringBuffer();
			stb.append("Centroid"+
		cluster.getCentroid().getAttrList());
		stb.append(System.lineSeparator());
		
		for(int i=0;i<cluster.size();i++)
		{
	/*		stb.append(outputRecordCluster(
					cluster.getRecords().get(i))
					);
					
					*/
			stb.append(cluster.getRecords().get(i).getValueat(0));
			stb.append(System.lineSeparator());
		}
		
		return stb.toString();
	}
	
	public String outputProximityMatrix(ProximityMatrix pMatrix)
	{
		StringBuffer stb = new StringBuffer();
		
		for(int i=0;i<pMatrix.row();i++)
		{
			for(int j=0;j<pMatrix.col();j++)
				stb.append(pMatrix.getValueAt(i, j)+"\t");
			
			stb.append(System.lineSeparator());
		}
		return stb.toString();
	}
	
	private String outputRecordCluster(RecordCluster recs)
	{
		StringBuffer stb = new StringBuffer();
		
		for(int i=0;i<recs.size();i++)
		{
			stb.append(recs.getValueat(i));
			stb.append("\t");
		}
		stb.append(System.lineSeparator());
		return stb.toString();
	}
	

	public static void printToConsole(String str)
	{
		System.out.println(str);
//		TrainingLog.trainLogs.log(Level.INFO,str);
	}
}
