package com.data_mining.model.clusters;

import java.util.ArrayList;
import java.util.List;

import com.data_mining.logic.ClusterLogics;
import com.data_mining.model.attributes_records.TypeAttribute;


/**
 * Object model for Data given by user to compute clusters
 * @author Janakiraman
 *
 */
public class DataCluster {

	private List<AttributeCluster> attributes;
	
	private List<RecordCluster> records;
	
	public DataCluster()
	{
		attributes = new ArrayList<AttributeCluster>();
		records = new ArrayList<RecordCluster>();
	}
	
	public RecordCluster getRecordAt(int i)
	{
		return records.get(i);
	}
	
	public AttributeCluster getAttributesat(int i)
	{
		return attributes.get(i);
		
	}
	
	public void addRecord(RecordCluster rec)
	{
		records.add(rec);
	}
	
	public void addRecord(List<String> rec)
	{
		records.add(new RecordCluster(rec));
	}

	public void addHeader(String attributeName,TypeAttribute type) {
		
		attributes.add(new AttributeCluster(attributeName, type));
	}


	public Integer sizeOfRecords() {
		// TODO Auto-generated method stub
		return records.size();
	}

	public Integer numberOfAttributes() {
		// TODO Auto-generated method stub
		return attributes.size();
	}
	
	public Integer numberOfPoints()
	{
		return new ClusterLogics().getNumberOfPoints(attributes);
	}
	
	public String[] returnPointsAt(int i)
	{
		return new ClusterLogics().getPoints(attributes, records.get(i));
	}

	public List<AttributeCluster> getAttributes() {
		return attributes;
	}
	
	public List<RecordCluster> getRecordsList()
	{
		return records;
	}
	
	public List<Double> getColumnAt(int index)
	{
		return new ClusterLogics().getColumnFromRecordListDouble(records, index);
	}
	
	public List<String> getColumClasses()
	{
		return new ClusterLogics().getColumnFromRecordList(records, attributes.size()-1);
	}

	public void setAttributes(List<AttributeCluster> attributes) {
		this.attributes = attributes;
	}
	
}
