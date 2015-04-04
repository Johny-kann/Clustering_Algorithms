package com.data_mining.model.clusters;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.data_mining.logic.CommonLogics;

/**
 * Centroid class
 * @author Janakiraman
 *
 */
public class Centroid implements Cloneable{

//	private int numOfAttrbs;
	private List<Double> attrList;
	
	public Centroid(
			double[] attrs)
	{
		this.attrList = new CommonLogics().arrayToList(attrs);
//		this.numOfAttrbs = numOfAttrbs;	
	}
	
	public Centroid(
			Double[] attrs)
	{
		this.attrList = //new CommonLogics().arrayToList(attrs);
						Arrays.asList(attrs);
//		this.numOfAttrbs = numOfAttrbs;	
	}
	
	public Centroid(
			List<Double> attrs)
	{
		this.attrList = attrs;
//		this.numOfAttrbs = numOfAttrbs;	
	}
	
	@Override
	public Centroid clone() throws CloneNotSupportedException {
	      
		List<Double> list2 = new ArrayList<>();
		for(Double value:attrList)
		{
			list2.add(value);
		}
		Centroid clonedCustomer = new Centroid(list2);
		
		return clonedCustomer;
	   }
	
	public List<Double> getAttrList()
	{
		return attrList;
	}
}
