package ru.bmstu.plasma.analysis.channel;

import java.util.Iterator;

import ru.bmstu.plasma.analysis.element.ArrayOfElements;
import ru.bmstu.plasma.analysis.element.ElementSpectrLines;
import ru.bmstu.plasma.analysis.element.SpectrLine;

public class MaxPointOfElement extends PointOfElement {
	private String foundItems = "";
	
	public MaxPointOfElement(double waveLength, double counts, double intensity) {
		super(waveLength, counts, intensity);
	}
	
	public String getFoundItems()
	{
		return foundItems;
	}
	
	public void compareWithSpectrLine(ArrayOfElements elements, double halfWidth, double k)
	{
		Iterator<ElementSpectrLines> linesItr;
		ElementSpectrLines lines;
		Iterator<SpectrLine> lineItr;
		SpectrLine line;
		
		foundItems = "";
	    
		linesItr = elements.getArray().iterator();
	    while (linesItr.hasNext())
    	{
    		lines = linesItr.next();
    		if (lines.getCheckBox().getValue())
    		{	
    			lineItr = lines.getLines().iterator();
    			while (lineItr.hasNext())
    			{
    				line = lineItr.next();
    				if (Math.abs(getWaveLength() - line.getWavelength()) < k * halfWidth)
    				{
    					if (!foundItems.equals(""))
    						foundItems += "<br>";
    					foundItems += lines.getName() + "; " + line.getWavelength();
    				}
    			}
    		}
    	}
	}
}
