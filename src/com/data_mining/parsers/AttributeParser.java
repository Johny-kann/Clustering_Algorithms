package com.data_mining.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.data_mining.constants.Notations;
import com.data_mining.model.attributes_records.TypeAttribute;

/**
 * contains the functions used for parsing the attribute file 
 * @author Janakiraman
 *
 */
public class AttributeParser {

	private String mainLine;
	private String attributeName;
	private TypeAttribute attributeType;
	private List<String> classValues;
	
	public AttributeParser(String line)
	{
		mainLine = line;
		classValues = new ArrayList<String>();
		
		int split;
		if(mainLine.contains(Notations.ATTRIBUTE_SPLITTER1))
		{
		split = mainLine.indexOf(Notations.ATTRIBUTE_SPLITTER1);
		}
		else
		{
		split = mainLine.indexOf(Notations.ATTRIBUTE_SPLITTER2);
		}

		attributeName = mainLine.substring(0, split);
		
		
		splitter(mainLine.substring(split+1,mainLine.length()),Notations.ATTRIBUTE_DELIMITER);
	}
	
	/**
	 * splits the line depending on the delimiter setting the attribute and values
	 * @param line from file
	 * 
	 * @param delimiter used in the file
	 */
	private void splitter(String str,String delim)
	{
	//	System.out.println(str);
	
		StringTokenizer stk = new StringTokenizer(str, delim);
		
		if(stk.countTokens()==1)
		{
			String ss = stk.nextToken();
			
			if(ss.equalsIgnoreCase("continuous"))
				attributeType = TypeAttribute.continuous;
			else if(ss.equalsIgnoreCase("ID"))
				attributeType=TypeAttribute.ID;
		}
		else
		{
		while(stk.hasMoreTokens())
		{
			  String pattern= "[a-zA-Z0-9.]*";
		       String s = stk.nextToken();
		       if(s.matches(pattern))
		    	   classValues.add(s);
		       
		       attributeType = TypeAttribute.externalLabel;
		}
		attributeType = TypeAttribute.externalLabel;
		}
		
	}
	
	/**
	 * @return possible values of the attributes
	 */
	public List<String> getAttributeValues()
	{
		return classValues;
	}

	public String getMainLine() {
		return mainLine;
	}

	public void setMainLine(String mainLine) {
		this.mainLine = mainLine;
	}

	/**
	 * @return attribute name
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @param attributeName
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * @return
	 */
	public TypeAttribute getAttributeType() {
		return attributeType;
	}

	/**
	 * @param attributeType
	 */
	public void setAttributeType(TypeAttribute attributeType) {
		this.attributeType = attributeType;
	}
	
}
