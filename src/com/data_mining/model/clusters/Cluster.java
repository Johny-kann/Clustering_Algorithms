package com.data_mining.model.clusters;

import java.util.ArrayList;
import java.util.List;

import com.data_mining.logic.ClusterLogics;

/**
 * Cluster object model
 * @author Janakiraman
 *
 */
public class Cluster implements Cloneable{

	private Centroid centroid;
	private List<RecordCluster> records;
	private int clusterNumber;
	private double SSE;
	private List<AttributeCluster> attributes;
	private double silhoute;
	private String className;
	
	public Cluster(Centroid attrs,int clusterNumber)
	{
		this.centroid = attrs;
		this.records = new ArrayList<RecordCluster>();
		this.clusterNumber = clusterNumber;
	}
	
	public Cluster(double[] attrs,int clusterNumber)
	{
		this.centroid = new Centroid(attrs);
		this.records = new ArrayList<RecordCluster>();
		this.clusterNumber = clusterNumber;
	}
	
	public Cluster(Double[] attrs,int clusterNumber)
	{
		this.centroid = new Centroid(attrs);
		this.records = new ArrayList<RecordCluster>();
		this.clusterNumber = clusterNumber;
	}
	
	@Override
	public Cluster clone() throws CloneNotSupportedException {
	      
		Cluster clonedCustomer = new Cluster(centroid.clone(), clusterNumber);
		
		for(int i=0;i<records.size();i++)
		{
			clonedCustomer.addPoints(records.get(i).clone());
		}
	    
		List<AttributeCluster> listAttr = new ArrayList<>();
		for(int i=0;i<attributes.size();i++)
		{
			listAttr.add(attributes.get(i));
			
		}
		
		clonedCustomer.setAttributes(listAttr);
		
	      return clonedCustomer;
	   }

	public Centroid getCentroid() {
		return centroid;
	}

	public void setCentroid(Centroid centroid) {
		this.centroid = centroid;
	}

	public void addPoints(RecordCluster rec)
	{
		records.add(rec);
	}
	
	public void addALLPoints(List<RecordCluster> recList)
	{
		records.addAll(recList);
		
	}
	
	public void clearRecords()
	{
		records.clear();
	}
	
	public List<RecordCluster> getRecords() {
		return records;
	}
	
		
	public String[] getRecordPointsAt(int index)
	{
		return new ClusterLogics().getPoints(attributes, records.get(index));
	}

	public void setRecords(List<RecordCluster> records) {
		this.records = records;
	}
	
	public int size()
	{
		return records.size();
	}
	
	public int getClusterNumber()
	{
		return clusterNumber;
	}
	
	public Integer numberOfPoints()
	{
		return new ClusterLogics().getNumberOfPoints(attributes);
	}
	
	public Integer numberOfAttributes()
	{
		return attributes.size();
	}
	
	public List<Double> getColumnAt(int index)
	{
		return new ClusterLogics().getColumnFromRecordListDouble(records, index);
	}
	
	public List<String> getColumnAtString(int index)
	{
		return new ClusterLogics().getColumnFromRecordList(records, index);
	}
	

	public double getSSE() {
		return SSE;
	}

	public void setSSE(double sSE) {
		SSE = sSE;
	}

	public List<AttributeCluster> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeCluster> attributes) {
		this.attributes = attributes;
	}

	public double getSilhoute() {
		return silhoute;
	}

	public void setSilhoute(double silhoute) {
		this.silhoute = silhoute;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
