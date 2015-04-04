package com.data_mining.model.attributes_records;

import java.util.List;

/**
 * @author Janakiraman
 * 
 * Model for storing attributes
 *
 */
public class AttributesSpecifications
{
	private String name;
	private TypeAttribute type;
	private List<String> values;
	
	public AttributesSpecifications(String name,TypeAttribute type,List<String> values)
	{
		this.name = name;
		this.type = type;
		this.values = values;
	}

	public String getName() {
		return name;
	}

	
	public TypeAttribute getType() {
		return type;
	}

	public List<String> getValues() {
		return values;
	}

		
}
