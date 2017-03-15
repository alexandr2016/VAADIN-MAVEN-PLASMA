package ru.bmstu.plasma.analysis.element;

public class SpectrLine {
	private double wavelength;
	private double intensity;
	
	public SpectrLine(double wavelength, double intensity)
	{
		this.wavelength = wavelength;
		this.intensity = intensity;
	}
	
	public double getWavelength()
	{
		return wavelength;
	}
	
	public double getIntensity()
	{
		return intensity;
	}
	
	public void scale(double k)
	{
		intensity *= k;
	}
}
