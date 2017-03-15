package ru.bmstu.plasma.analysis.channel;

import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;

public class ChartChannelData {
	private DataSeries dataSeries = new DataSeries();
	
	public DataSeries getDataSeries()
	{
		return dataSeries;
	}
	
	public void addPointToDataSeries(PointOfElement point, int typeOfPoint) throws Exception
	{				
		double x = point.getWaveLength();
		double y;
		switch (typeOfPoint) {
    	case 1:
    		y = point.getCounts();
            break;
    	case 2:
    		y = point.getIntensity();
            break;
        default:
        	throw new Exception("Неверный тип точки");
		}
		
		dataSeries.add(new DataSeriesItem(x, y));
	}
}
