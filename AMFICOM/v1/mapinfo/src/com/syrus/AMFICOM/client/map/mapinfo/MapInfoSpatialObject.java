package com.syrus.AMFICOM.Client.Map.Mapinfo;

import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.Client.Map.SpatialObject;

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
