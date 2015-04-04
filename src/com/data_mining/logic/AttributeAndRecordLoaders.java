package com.data_mining.logic;

import java.util.List;

import com.data_mining.file_readers.TextFileReader;
import com.data_mining.model.clusters.DataCluster;
import com.data_mining.parsers.AttributeParser;
import com.data_mining.parsers.RecordParser;

/**
 * @author Janakiraman
 * Class with methods to parse the attributes and Records and load it in the models
 *
 */
public class AttributeAndRecordLoaders {


	public static void loadAttributeFromFile(DataCluster temp,String attributeFile,String recordFile)
	{
		TextFileReader tf = new TextFileReader(attributeFile);
		
		AttributeParser attribParser;
		
		List<String> attributes = tf.getOutput();
		tf.closeStream();
		
		//Setting attributes
		for(int i=0;i<attributes.size();i++)
		{
			attribParser = new AttributeParser(attributes.get(i));
						
			temp.addHeader(attribParser.getAttributeName(),
					attribParser.getAttributeType());
			
		}
		loadRecordsFromFile(temp, recordFile);
	}
	

	public static void loadRecordsFromFile(DataCluster temp,String recordFile)
	{
		TextFileReader tf = new TextFileReader(recordFile);
		
		RecordParser recParser;
		
		List<String> records = tf.getOutput();
		tf.closeStream();
		
		for(int i=0;i<records.size();i++)
		{
			recParser = new RecordParser(records.get(i));
				temp.addRecord(recParser.getRecordElements());
							
		}
	}
	
	
	
}
