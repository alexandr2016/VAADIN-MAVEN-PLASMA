package ru.bmstu.plasma.analysis.element;

import java.util.ArrayList;
import java.util.Iterator;

import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;

public class ArrayOfElements {
	private int experimentId;
	private ArrayList<ElementSpectrLines> elements;
	private DataSeries ds;
	
	public ArrayOfElements(int experimentId)
	{
		this.experimentId = experimentId;
		elements = new ArrayList<ElementSpectrLines>();
		ds = new DataSeries();
	}
	
	public void initElements() throws Exception
	{
		ElementSpectrLines elem;
		
		elem = new ElementSpectrLines("Ar-I");
		elements.add(elem);
		
		elem = new ElementSpectrLines("Ar-II");
		elements.add(elem);
		
		elem = new ElementSpectrLines("H-I");
		elements.add(elem);
		
		elem = new ElementSpectrLines("Hg-I");
		elements.add(elem);
		
		initDataSeries();
	}
	
	public void initDataSeries()
	{
		ds.clear();
		
		Iterator<ElementSpectrLines> itr = elements.iterator();
		ElementSpectrLines lines;
		while (itr.hasNext())
		{
			lines = itr.next();
			lines.calculateKoef(20000);
			lines.scaleSpectrLines();
			if (lines.getCheckBox().getValue())
			{
				Iterator<SpectrLine> itr2 = lines.getLines().iterator();
				SpectrLine line;
				while (itr2.hasNext())
				{
					line = itr2.next();
					ds.add(new DataSeriesItem(line.getWavelength(), line.getIntensity()));
				}
			}
		}
	}
	
	public ArrayList<ElementSpectrLines> getArray()
	{
		return elements;
	}
	
	public DataSeries getDataSeries()
	{
		return ds;
	}
}
