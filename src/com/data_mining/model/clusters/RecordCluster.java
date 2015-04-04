package com.data_mining.model.clusters;

import java.util.ArrayList;
import java.util.List;

/**
 * Records in cluster
 * @author Janakiraman
 *
 */
public class RecordCluster implements Cloneable{

	private List<String> value;
	
	public RecordCluster()
	{
		value = new ArrayList<String>();
	}
	
	public RecordCluster(List<String> list)
	{
		value = list;
	}
	
	
	@Override
	public RecordCluster clone() throws CloneNotSupportedException {
	      
		RecordCluster clonedCustomer = new RecordCluster();
		
		for(String str:value)
		{
			clonedCustomer.addValue(str);
			
		}
	       
	      return clonedCustomer;
	   }
	
	
	public String getValueat(int i)
	{
		return value.get(i);
	}
	
	public String getClassName()
	{
		return value.get(value.size()-1);
	}
	
	public void addValue(String val)
	{
		this.value.add(val);
	}
	
	public void addValue(Double val)
	{
		this.value.add(val.toString());
	}
	
	public List<String> getValue() {
		return value;
	}
	public void setValue(List<String> value) {
		this.value = value;
	}
	
	public int size()
	{
		return value.size();
	}

}
