package ru.bmstu.plasma.analysis.channel;

import java.util.Iterator;

import ru.bmstu.plasma.analysis.element.ArrayOfElements;


public class MaxChannelData extends ArrayOfMaxPoints {

	public void compareMaximums(ArrayOfElements elements, double halfWidth, double k)
	{
	    Iterator<MaxPointOfElement> pntItr = getIterator();
	    while (pntItr.hasNext())
	    	pntItr.next().compareWithSpectrLine(elements, halfWidth, k);
	}
	
	public void addPoint(double x, double y)
	{
		addPoint(new MaxPointOfElement(x, y, y));
	}
	
	public boolean removePoint(double x, double y, int typeOfPoint)
	{
		PointOfElement pnt;
		double k = 0.000001;
		double waveLength, intensity;
		
		for (int i = 0; i < getSize(); i++)
		{
			pnt = getPoint(i);
			
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
				remPoint(i);
				return true;
			}
		}
		
		return false;
	}
}
