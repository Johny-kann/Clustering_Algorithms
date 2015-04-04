package com.data_mining.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Janakiraman
 *
 */
public class RecordParser {

	private String mainLine;
	private List<String> elements;
		
	public RecordParser(String line)
	{
		mainLine = line;
		
		elements = new ArrayList<String>();
		
	//	int split = mainLine.indexOf(Notations.ATTRIBUTE_SPLITTER);
		splitter(line," \t"
		//		Notations.RECORD_DELIMITER
				);
	}
	
	
	/**
	 * List of elements in the record
	 * @return
	 */
	public List<String> getRecordElements()
	{
		return elements;
	}
	
	
	
	/**
	 * Splits the record line from file based on delimiter
	 * @param Record line from file
	 * @param delimiter for records
	 */
	private void splitter(String str,String delim)
	{
		StringTokenizer stk = new StringTokenizer(str, delim);
		
		
		while(stk.hasMoreTokens())
		{
			// String pattern= "[a-zA-Z0-9.]*";
		       String s = stk.nextToken();
		       			
		//    if(s.matches(pattern))   
		    	elements.add(s);
		}
		
//		category = elements.get(elements.size()-1);
//		elements.remove(elements.size()-1);
	}
	

	
}
