package ru.bmstu.plasma.analysis.channel;

import java.util.Iterator;

import ru.bmstu.plasma.analysis.element.ArrayOfElements;

import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

public class Channel {
	private SimpleChannelData simpleData = new SimpleChannelData();
	private CleanChannelData cleanData = new CleanChannelData();
	private MaxChannelData maxData = new MaxChannelData();
	private ChartChannelData chartData = new ChartChannelData();
	private Table maxTable;
	private int experimentId;
	private int channelId; // Тип канала - 0, 1, 2, 3.
	
	public Channel(int experimentId, int channelId)
	{
		this.experimentId = experimentId;
		this.channelId = channelId;
		simpleData.initData(this.experimentId, this.channelId);
	}
	
	public PointOfElement addSimpleDataToCleanData(PointOfElement firstPoint, int typeOfPoint)
	{		
		Iterator<PointOfElement> itr = simpleData.getIterator();
		PointOfElement trm, res = null;
		double from = -1000;
		
		cleanData.clearPoints();
		if (firstPoint != null)
		{	
			cleanData.addPointToCleanData(firstPoint, typeOfPoint, from);
			from = firstPoint.getWaveLength();
		}	
		
		
		for (; itr.hasNext(); )
        {
        	trm = itr.next();
        	if (cleanData.addPointToCleanData(trm, typeOfPoint, from))
        		res = trm;
        }
		
        return res;
	}
	
	public void addCleanDataToDataSeries(int typeOfPoint) throws Exception
	{		
		Iterator<PointOfElement> itr = cleanData.getIterator();
		chartData.getDataSeries().clear();		
		
		for (; itr.hasNext(); )
        	chartData.addPointToDataSeries(itr.next(), typeOfPoint);
	}
	
	public void findMaximums(int typeOfPoint, double bottomLevel, double topLevel, int halfWidth) throws Exception
	{		
		Iterator<PointOfElement> itr = cleanData.getIterator();
		PointOfElement prevPoint, nextPoint, curMax = null;
		int upCounter = 0, downCounter = 0;
		boolean increases = false;
		double prevY, nextY; 
		double startX = -1, endX = -1; // Первый и последний x в "холмике"
		double curMaxY, curMaxX;
		int sens = (halfWidth - 1) / 2;
		
		maxData.clearPoints();
		prevPoint = itr.next();
		
		while (itr.hasNext())
        {
        	nextPoint = itr.next();
        	switch (typeOfPoint) {
            	case 1:
            		prevY = prevPoint.getCounts();
            		nextY = nextPoint.getCounts();
                    break;
            	case 2:
            		prevY = prevPoint.getIntensity();
            		nextY = nextPoint.getIntensity();
                    break;
                default:
                	throw new Exception("Неверный тип точки");
        	}	
        	/*
        	if (nextY < 30)
        		continue;
        	*/
        	if (nextY > prevY) // Если следующее значение больше предыдущего
        	{	
        		if (increases) // Если шло возрастание, то оно просто продолжается
        			upCounter++;
        		else // Если шло убывание, то ВСЁ сбрасывается и начинается возрастание (здесь проверка на максимум)
        		{
        			// Если есть текущий максимум, делаем проверку
        			if (curMax != null)
        			{	
        				switch (typeOfPoint) {
        				case 1:
        					curMaxY = curMax.getCounts();
        					break;
        				case 2:
        					curMaxY = curMax.getIntensity();
        					break;
        				default:
        					throw new Exception("Неверный тип точки");
        				}
        			
        				if (downCounter >= sens && upCounter >= sens &&  curMaxY > bottomLevel && curMaxY < topLevel)
        				{
        					endX = prevPoint.getWaveLength();
        					curMaxX = (startX + endX) / 2.0;
        					//maxData.getPoints().add(new TableRecordMini(curMaxX, curMaxY, curMaxY));
        					maxData.addPoint(new MaxPointOfElement(curMax.getWaveLength(), curMaxY, curMaxY));
        				}
        			}
        			
        			// Сбрасываем значения
        			downCounter = 0;
        			upCounter = 0;
        			increases = true;
        			startX = prevPoint.getWaveLength();
        			upCounter++;
        		}
        	}	
        	else // Если следующее значение меньше предыдущего
        	{	
        		if (nextY < prevY)
        		{	
        			if (!increases) // Если шло убывание, то оно просто продолжается
        				downCounter++;
        			else // Если шло возрастание, то просто начинает убывать
        			{
        				curMax = prevPoint;
        				increases = false;
        				downCounter++;
        			}
        		}
        	}	
        	prevPoint = nextPoint;   		
        }
	}
	
	public void addMaximumsToDataSeries(int typeOfPoint, DataSeries ownMaxDS) throws Exception
	{
		Iterator<MaxPointOfElement> itr = maxData.getIterator();
		PointOfElement trm;
		
		while(itr.hasNext())
		{
			trm = itr.next();
			switch (typeOfPoint) {
        	case 1:
        		ownMaxDS.add(new DataSeriesItem(trm.getWaveLength(), trm.getCounts()));
                break;
        	case 2:
        		ownMaxDS.add(new DataSeriesItem(trm.getWaveLength(), trm.getIntensity()));
                break;
            default:
            	throw new Exception("Неверный тип точки");
			}	
		}
	}
	
	public DataSeries getDataSeries()
	{
		return chartData.getDataSeries();
	}
	
	public void setMaxTable(Table maxTable)
	{
		this.maxTable = maxTable;
	}
	
	public void fillMaxTable(int typeOfPoint) throws Exception
	{
		double y;
		clearMaxTable();
		
		for (int i = 0; i < maxData.getSize(); i++)
		{
			switch (typeOfPoint) {
        	case 1:
        		y = maxData.getPoint(i).getCounts();
                break;
        	case 2:
        		y = maxData.getPoint(i).getIntensity();
                break;
            default:
            	throw new Exception("Неверный тип точки");
			}
			
			maxTable.addItem(new Object[]{maxData.getPoint(i).getWaveLength(), new Label(maxData.getPoint(i).getFoundItems(), Label.CONTENT_XHTML), y}, i + 1);
		}
	}
	
	public void compareMaximums(ArrayOfElements elements, double halfWidth, double k)
	{
		maxData.compareMaximums(elements, halfWidth, k);
	}
	
	public void clearMaxTable()
	{
		if (maxTable != null)
			maxTable.getContainerDataSource().removeAllItems();
	}
	
	public boolean addMaxPoint(double x, double y, int typeOfPoint)
	{
		Iterator<PointOfElement> itr = cleanData.getIterator();
		PointOfElement pnt;
		double k = 0.000001;
		double waveLength, intensity;
		
		while (itr.hasNext())
		{
			pnt = itr.next();
			
			waveLength = pnt.getWaveLength();
			switch (typeOfPoint) {
        	case 1:
        		intensity = pnt.getCounts();
                break;
        	case 2:
        		intensity = pnt.getIntensity();
                break;
            default:
            	intensity = -1;
			}
			
			if (Math.abs(waveLength - x) < k && Math.abs(intensity - y) < k)
			{
				maxData.addPoint(x, y);
				return true;
			}
		}
		
		return false;
	}
	
	public boolean removeMaxPoint(double x, double y, int typeOfPoint)
	{
		return maxData.removePoint(x, y, typeOfPoint);
	}
	
	public void clearMaxData()
	{
		maxData.clearPoints();
	}
}
