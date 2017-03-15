package ru.bmstu.plasma.analysis.channel;

public class PointOfElement {
	private double waveLength;
	private double counts;
	private double intensity;
	
	public PointOfElement(double waveLength, double counts, double intensity)
	{
		this.waveLength = waveLength;
		this.counts = counts;
		this.intensity = intensity;
	}
	
	public double getWaveLength()
	{
		return waveLength;
	}
	
	public double getCounts()
	{
		return counts;
	}
	
	public double getIntensity()
	{
		return intensity;
	}
}
