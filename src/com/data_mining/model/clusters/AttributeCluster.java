package com.data_mining.model.clusters;

import java.util.ArrayList;
import java.util.List;

import com.data_mining.model.attributes_records.TypeAttribute;

/**
 * Contains attributes data used in the table of the data. More like a header
 * @author Janakiraman
 *
 */
public class AttributeCluster {

	private String itemName;
	private TypeAttribute type;
	private List<String> values;
	
	public AttributeCluster(String itemName, TypeAttribute type) {
		super();
		this.itemName = itemName;
		this.type = type;
		this.values = new ArrayList<String>();
		
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public TypeAttribute getType() {
		return type;
	}

	public void setType(TypeAttribute type) {
		this.type = type;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}
	
	
	
}
