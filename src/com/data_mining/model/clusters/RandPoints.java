package com.data_mining.model.clusters;

/**
 * Special object model to compute Rand statistic
 * @author Janakiraman
 *
 */
public class RandPoints {

	private String className;
	private String clusterName;
	private String pointID;
	
	
	public RandPoints(String className,String clusterName,String pointId)
	{
		this.className = className;
		this.clusterName = clusterName;
		this.pointID = pointId;
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClusterName() {
		return clusterName;
	}
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	public String getPointID() {
		return pointID;
	}
	public void setPointID(String pointID) {
		this.pointID = pointID;
	}
	
	
	
}
