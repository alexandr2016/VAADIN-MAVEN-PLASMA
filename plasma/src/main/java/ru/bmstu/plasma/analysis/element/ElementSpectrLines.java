package ru.bmstu.plasma.analysis.element;

import java.util.ArrayList;
import java.util.Iterator;

import ru.bmstu.plasma.analysis.db.PlasmaDB;

import com.vaadin.addon.charts.model.style.Color;
import com.vaadin.ui.CheckBox;

public class ElementSpectrLines {
	private ArrayList<SpectrLine> lines;
	private CheckBox checkbox;
	private Color color;
	private double koef;
	private String name;
	
	public ElementSpectrLines(String elementId) throws Exception
	{
			lines = PlasmaDB.getElementData(elementId);
			checkbox = new CheckBox();
			name = elementId.replace('-', ' ');
	}
	
	public ArrayList<SpectrLine> getLines()
	{
		return lines;
	}
	
	public CheckBox getCheckBox()
	{
		return checkbox;
	}
	
	public String getName()
	{
		return name;
	}
	
	private double getMaxSpectrLine()
	{
		double res = Double.MIN_VALUE;
		double v;
		
		Iterator<SpectrLine> itr = lines.iterator();
		while (itr.hasNext())
		{
			v = itr.next().getIntensity();
			if (v > res)
				res = v;
		}
		
		return res;
	}
	
	public void calculateKoef(double maxValue)
	{
		koef = maxValue / getMaxSpectrLine();
	}
	
	public void scaleSpectrLines()
	{
		Iterator<SpectrLine> itr = lines.iterator();
		while (itr.hasNext())
			itr.next().scale(koef);
	}
}
