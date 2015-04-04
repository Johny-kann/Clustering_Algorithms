package com.data_mining.model.clusters;

/**
 * Proximity Matrix
 * @author Janakiraman
 *
 */
public class ProximityMatrix {

	private Double matrix[][];
	
	
	public ProximityMatrix(int clusterNumbers)
	{
		matrix = new Double[clusterNumbers][clusterNumbers];
		
	}
	
	public double getValueAt(int row,int col)
	{
		return matrix[row][col];
	}
	
	public void setValue(double value,int row,int col)
	{
		matrix[row][col] = value;
		matrix[col][row] = value;
	}
	
	public int row()
	{
		return matrix.length;
	}
	
	public int col()
	{
		return matrix[0].length;
	}
}
