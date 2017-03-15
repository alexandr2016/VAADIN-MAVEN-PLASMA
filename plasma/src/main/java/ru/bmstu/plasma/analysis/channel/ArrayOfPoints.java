package ru.bmstu.plasma.analysis.channel;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class ArrayOfPoints {
	private ArrayList<PointOfElement> points = new ArrayList<PointOfElement>();

	public void setPoints(ArrayList<PointOfElement> points)
	{
		this.points = points;
	}
	
	public PointOfElement getPoint(int i)
	{
		return points.get(i);
	}
	
	public Iterator<PointOfElement> getIterator()
	{
		return points.iterator();
	}
	
	public void clearPoints()
	{
		points.clear();
	}
	
	public void addPoint(PointOfElement point)
	{
		points.add(point);
	}
}
