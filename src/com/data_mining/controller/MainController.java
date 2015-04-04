package com.data_mining.controller;

import java.util.Map;

import com.data_mining.constants.FilesList;
import com.data_mining.constants.Notations;
import com.data_mining.constants.ValueConstants;
import com.data_mining.file_readers.PropertiesConfig;
import com.data_mining.file_readers.TextFileWriter;
import com.data_mining.logic.AgglomerativeComputations;
import com.data_mining.logic.AttributeAndRecordLoaders;
import com.data_mining.logic.ChoosingAttributes;
import com.data_mining.logic.ClusterLogics;
import com.data_mining.logic.CommonLogics;
import com.data_mining.logic.ErrorsAndGain;
import com.data_mining.logic.SearchingLogics;
import com.data_mining.logs.TrainingLog;
import com.data_mining.model.attributes_records.DataTable;
import com.data_mining.model.attributes_records.OrderedClassSet;
import com.data_mining.model.clusters.AlgorithmType;
import com.data_mining.model.clusters.CluseterList;
import com.data_mining.model.clusters.Cluster;
import com.data_mining.model.clusters.DataCluster;
import com.data_mining.model.rules.RuleSet;
import com.data_mining.view.console.Outputs;


/**
 * @author Janakiraman 
 * 
 * Main Controller for loading test data and train data and calling functions
 *
 */
public class MainController {

	DataTable mainAttributes;
	DataTable trainData;
	DataTable validationData;
	DataTable testData;
	RuleSet mainRuleSet;
	OrderedClassSet sortedClassSet;
	DataCluster dataCluster;
	CluseterList clusters;

	
	public MainController()
	{
		mainAttributes = new DataTable();
		mainRuleSet = new RuleSet();
		testData = new DataTable();
		dataCluster = new DataCluster();
		clusters = new CluseterList();
		PropertiesConfig.assignInputFiles();
	
	}
	
	
	
	public void loadTable()
	{
	//	TransactionTable temp = new TransactionTable();
		DataCluster temp = new DataCluster();
		AttributeAndRecordLoaders.loadAttributeFromFile(temp, FilesList.ATTRIBUTES_FILES, FilesList.RECORD_FILES);
	//	transTable = temp;
		if(Notations.NORMALIZING)
		{
			dataCluster = new ClusterLogics().normalizeData(temp);
		}else
		{
			dataCluster = temp;
		}
		
		
	}
	
	public void start()
	{
		if(Notations.RAND_STATISTICS)
		{
			Notations.DEFAULT_CLASS = new ClusterLogics().getMaxClass(dataCluster.getColumClasses());
		}
		
		if(Notations.AGLORITHM_TYPE==AlgorithmType.BasicKmeans)
			startBasicKMeans();
		
		if(Notations.AGLORITHM_TYPE==AlgorithmType.Agglomerative)
			startAgglomerative();
		
		if(Notations.AGLORITHM_TYPE==AlgorithmType.BisectKmeans)
			startBisectKMeans();
	}

	public void startBasicKMeans()
	{
	
		loadTable();
	
		for(int i = 0 ;i<ValueConstants.K_NUMBER;i++)
		{
			clusters.addCluster(new Cluster(
					new CommonLogics().generateRandomNumbers(1, dataCluster.numberOfPoints()),i)
					);
			
			clusters.getClusterAt(i).setAttributes(dataCluster.getAttributes());
		}
		
		
		
		computeClusters();
		
		if(Notations.RAND_STATISTICS)
			new ClusterLogics().assignClusterClass(clusters);
	}
	
	public void startAgglomerative()
	{
		loadTable();
		
	
		for(int i=0;i<dataCluster.sizeOfRecords();i++)
		{
			clusters.addCluster(new Cluster(
					CommonLogics.convertsStringArrayTDouble(dataCluster.returnPointsAt(i))
					, i));
			clusters.getClusterAt(i).setAttributes(dataCluster.getAttributes());
			clusters.getClusterAt(i).addPoints(dataCluster.getRecordAt(i));
		}
		
		new AgglomerativeComputations().computeClustersAgglomerative(clusters);
		
		if(Notations.RAND_STATISTICS)
			new ClusterLogics().assignClusterClass(clusters);
	}
	
	public void startBisectKMeans()
	{
		loadTable();
		
	
		Cluster cl = new Cluster( 
				new CommonLogics().generateRandomNumbers(1, dataCluster.numberOfPoints()),0);
		
		cl.setAttributes(dataCluster.getAttributes());
		cl.setRecords(dataCluster.getRecordsList());
		
		clusters.addCluster(cl);
		
		computeClustersBisect();
		
		if(Notations.RAND_STATISTICS)
			new ClusterLogics().assignClusterClass(clusters);
		
	}
	
	
	public void computeClusters()
	{
		ClusterLogics cl = new ClusterLogics();
		cl.computeFinalClusters(clusters, dataCluster);
		

	}
	
	public void computeClustersBisect()
	{
		ClusterLogics cl = new ClusterLogics();
		try {
			
			cl.computeFinalClustersBisect(clusters);
			
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void fillRuleSet()
	{
		CommonLogics cl = new CommonLogics();

		
	//	System.out.println(classes.getOrderedClasses());
	ChoosingAttributes choose = new ChoosingAttributes();
	if(Notations.PRUNING_ON)
	{
		sortedClassSet = new OrderedClassSet(
				cl.sortMapValues
				(cl.classAndCounts(trainData))
				);
		TrainingLog.trainLogs.info("Sorted the input classes");
		
		Outputs.printToConsole("Order of the classes "+ sortedClassSet.getClassesAlone()
				);
		
	mainRuleSet = choose.fillRuleSet(trainData, sortedClassSet,validationData);
	orderBasedOnGeneralError();
	}else
	{
	//	System.out.println(mainAttributes.sizeOfRecords()+","+trainData.sizeOfRecords()+","+testData.sizeOfRecords());
		Map<String,Integer> map = cl.classAndCounts(mainAttributes);
		
	
		sortedClassSet = new OrderedClassSet(
				cl.sortMapValues
				(map)
				);
		
		Outputs.printToConsole("Order of the classes "+ sortedClassSet.getClassesAlone()
				);
		mainRuleSet = choose.fillRuleSet(mainAttributes, sortedClassSet,validationData);
	}
	
	}
	
	public void getResultSet()
	{
		
	}
	
	public void orderBasedOnGeneralError()
	{
		new CommonLogics().sortRulesBasedonGeneralizationError(mainRuleSet);
		
	}
	
	public String outputClusters()
	{
		StringBuffer stb = new StringBuffer();
		stb.append(Notations.AGLORITHM_TYPE);
		stb.append(System.lineSeparator());
		stb.append("Table used ");
		stb.append(System.lineSeparator());
		stb.append(new Outputs().outPutTable(dataCluster));
		stb.append(System.lineSeparator());
		stb.append("Clusters Formed ");
		stb.append(new Outputs().outputClusterList(clusters));
		stb.append(System.lineSeparator());
		
		if(Notations.SILHOUTTE)
		{
			double err = new ErrorsAndGain().calculateSilhoute(clusters);
			stb.append("Silhoutte error "+err);
			stb.append(System.lineSeparator());
		}if(Notations.RAND_STATISTICS)
		{
			new ClusterLogics().assignClusterClass(clusters);
			
			double err2 = new ErrorsAndGain().RandStatistic(new ClusterLogics().computeRandPoints(clusters));
			stb.append("Rand Statistics "+err2);
			stb.append(System.lineSeparator());
		}
		
		return stb.toString();
		
	}
	
	
	
	public void trainDataAccuracy()
	{
		StringBuffer stBuffer = new StringBuffer();

		stBuffer.append(new Outputs().outputRuleSet(mainRuleSet));
		stBuffer.append("Train Data");
		stBuffer.append(System.lineSeparator());
		
				stBuffer.append(new Outputs().outPutTable(mainAttributes));
	
		stBuffer.append("Accuracy "+
		new ChoosingAttributes().AccuracyForTableByRuleSet(mainAttributes, mainRuleSet,stBuffer)
				);

		Outputs.printToConsole(stBuffer.toString());
		new TextFileWriter().writeFile(stBuffer.toString(), FilesList.WRITE_TRAIN_RESULT);
			
	}
	
	public void mainRuleSetPrint()
	{
		StringBuffer stBuffer = new StringBuffer();
		new TextFileWriter().writeFile(
				new Outputs().outputRuleSet(mainRuleSet), 
				FilesList.WRITE_RULE_SET) ;
	}
	
	
	
	
	public DataTable getMainTable()
	{
		return mainAttributes;
	}
	
	public DataTable getTrainAttributes()
	{
		return trainData;
	}
	
	public DataTable getTestAttributes() {
		return validationData;
	}

	public OrderedClassSet getSortedClassSet() {
		return sortedClassSet;
	}



	public DataCluster getDataCluster() {
		return dataCluster;
	}



	public CluseterList getClusters() {
		return clusters;
	}
	
	
}
