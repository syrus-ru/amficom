package com.syrus.AMFICOM.client.map.mapinfo;

import com.syrus.AMFICOM.client.map.SpatialObject;
import com.syrus.AMFICOM.map.DoublePoint;

public class MapInfoSpatialObject implements SpatialObject
{
	private DoublePoint centre = null;

	private String label = null;

	public MapInfoSpatialObject(DoublePoint centre, String featureLabel)
	{
		this.centre = centre;
		this.label = featureLabel;
	}

	public String getLabel()
	{
		return this.label;
	}

	public DoublePoint getCenter()
	{
		return this.centre;
	}
}
