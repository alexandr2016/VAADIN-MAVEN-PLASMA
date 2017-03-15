package ru.bmstu.plasma.analysis.channel;

import java.util.ArrayList;
import java.util.Iterator;

public class ArrayOfMaxPoints {
	private ArrayList<MaxPointOfElement> points = new ArrayList<MaxPointOfElement>();

	public void setPoints(ArrayList<MaxPointOfElement> points)
	{
		this.points = points;
	}
	
	public MaxPointOfElement getPoint(int i)
	{
		return points.get(i);
	}
	
	public Iterator<MaxPointOfElement> getIterator()
	{
		return points.iterator();
	}
	
	public void clearPoints()
	{
		points.clear();
	}
	
	public void addPoint(MaxPointOfElement point)
	{
		points.add(point);
	}
	
	public void remPoint(int i)
	{
		points.remove(i);
	}
	
	public int getSize()
	{
		return points.size();
	}
}
