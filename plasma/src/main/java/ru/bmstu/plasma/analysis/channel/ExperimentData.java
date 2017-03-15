package ru.bmstu.plasma.analysis.channel;

import ru.bmstu.plasma.analysis.element.ArrayOfElements;

import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.ui.Table;

public class ExperimentData {
	private int experimentId;
	private Channel channel0;
	private Channel channel1;
	private Channel channel2;
	private Channel channel3;
	private DataSeries maxDataSeries = new DataSeries();
	
	public ExperimentData(int experimentId)
	{
		this.experimentId = experimentId;
		channel0 = new Channel(this.experimentId, 0);
		channel1 = new Channel(this.experimentId, 1);
		channel2 = new Channel(this.experimentId, 2);
		channel3 = new Channel(this.experimentId, 3);
	}
	
	public void addAllDataToCleanData(int typeOfPoint)
	{
		PointOfElement v = null;
		v = channel0.addSimpleDataToCleanData(v, typeOfPoint);
		v = channel1.addSimpleDataToCleanData(v, typeOfPoint);
		v = channel2.addSimpleDataToCleanData(v, typeOfPoint);
		channel3.addSimpleDataToCleanData(v, typeOfPoint);
	}
	
	public void addAllDataToDataSeries(int typeOfPoint) throws Exception
	{
		channel0.addCleanDataToDataSeries(typeOfPoint);
		channel1.addCleanDataToDataSeries(typeOfPoint);
		channel2.addCleanDataToDataSeries(typeOfPoint);
		channel3.addCleanDataToDataSeries(typeOfPoint);
	}
	
	public void initDataSeries(int typeOfPoint) throws Exception
	{
		clearMaximums();
		clearMaxDataSeries();
		clearMaxTables();
		addAllDataToCleanData(typeOfPoint);
		addAllDataToDataSeries(typeOfPoint);
	}
	
	public DataSeries getDataSeries(int channelId)
	{
		DataSeries res;
		switch (channelId) {
    	case 0:
    		res = channel0.getDataSeries();
            break;
    	case 1:
    		res = channel1.getDataSeries();
            break;
    	case 2:
    		res = channel2.getDataSeries();
            break;
    	case 3:
    		res = channel3.getDataSeries();
            break;    
        default:
        	res = null;
        	break;
		}
		return res;
	}
	
	public void addAllMaximumsToGUI(int typeOfPoint, double bottomLevel, double topLevel, int halfWidth) throws Exception
	{
		maxDataSeries.clear();
		
		channel0.addMaximumsToDataSeries(typeOfPoint, maxDataSeries);
		channel0.fillMaxTable(typeOfPoint);
		
		channel1.addMaximumsToDataSeries(typeOfPoint, maxDataSeries);
		channel1.fillMaxTable(typeOfPoint);
		
		channel2.addMaximumsToDataSeries(typeOfPoint, maxDataSeries);
		channel2.fillMaxTable(typeOfPoint);
		
		channel3.addMaximumsToDataSeries(typeOfPoint, maxDataSeries);
		channel3.fillMaxTable(typeOfPoint);
	}
	
	public DataSeries getMaxDataSeries()
	{
		return maxDataSeries;
	}
	
	public void setChannelTable(Table table, int channelId)
	{
		switch (channelId) {
    	case 0:
    		channel0.setMaxTable(table);
            break;
    	case 1:
    		channel1.setMaxTable(table);
            break;
    	case 2:
    		channel2.setMaxTable(table);
            break;
    	case 3:
    		channel3.setMaxTable(table);
            break;    
		}
	}
	
	public void compareMaximums(ArrayOfElements elements, double halfWidth, double k)
	{
		channel0.compareMaximums(elements, halfWidth, k);
		channel1.compareMaximums(elements, halfWidth, k);
		channel2.compareMaximums(elements, halfWidth, k);
		channel3.compareMaximums(elements, halfWidth, k);
	}
	
	public void clearMaxDataSeries()
	{
		maxDataSeries.clear();
	}
	
	public void clearMaxTables()
	{
		channel0.clearMaxTable();
		channel1.clearMaxTable();
		channel2.clearMaxTable();
		channel3.clearMaxTable();
	}
	
	public boolean addMaxPoint(double x, double y, int typeOfPoint)
	{
		return channel0.addMaxPoint(x, y, typeOfPoint) || channel1.addMaxPoint(x, y, typeOfPoint) ||
				channel2.addMaxPoint(x, y, typeOfPoint) || channel3.addMaxPoint(x, y, typeOfPoint);
	}
	
	public boolean removeMaxPoint(double x, double y, int typeOfPoint)
	{
		return channel0.removeMaxPoint(x, y, typeOfPoint) || channel1.removeMaxPoint(x, y, typeOfPoint) ||
				channel2.removeMaxPoint(x, y, typeOfPoint) || channel3.removeMaxPoint(x, y, typeOfPoint);
	}
	
	public void findMaximums(int typeOfPoint, double bottomLevel, double topLevel, int halfWidth) throws Exception
	{
		channel0.findMaximums(typeOfPoint, bottomLevel, topLevel, halfWidth);
		channel1.findMaximums(typeOfPoint, bottomLevel, topLevel, halfWidth);
		channel2.findMaximums(typeOfPoint, bottomLevel, topLevel, halfWidth);
		channel3.findMaximums(typeOfPoint, bottomLevel, topLevel, halfWidth);
	}
	
	public void clearMaximums()
	{
		channel0.clearMaxData();
		channel1.clearMaxData();
		channel2.clearMaxData();
		channel3.clearMaxData();
	}
}
