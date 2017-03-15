package ru.bmstu.plasma.analysis.channel;

public class CleanChannelData extends ArrayOfPoints {
	
	public boolean addPointToCleanData(PointOfElement point, int typeOfPoint, double from)
	{
		double x = point.getWaveLength();
		double y = -1;
		
		switch (typeOfPoint) {
        	case 1:
        	    y = point.getCounts();
                break;
        	case 2:
        	    y = point.getIntensity();
                break;
		}
		
		if (x > from && y > 0)
		{	
 	        addPoint(point);
 	        return true;
		}
		else
			return false;
	}
}
