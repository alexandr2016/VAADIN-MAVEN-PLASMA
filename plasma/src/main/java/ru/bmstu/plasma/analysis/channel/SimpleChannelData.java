package ru.bmstu.plasma.analysis.channel;

import ru.bmstu.plasma.analysis.db.PlasmaDB;


public class SimpleChannelData extends ArrayOfPoints {
	public void initData(int experimentId, int channelId)
	{
		setPoints(PlasmaDB.getExperimentData(experimentId, channelId));
	}
}
