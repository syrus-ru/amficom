package com.syrus.AMFICOM.client.map.objectfx;

import com.ofx.repository.SxSpatialObject;
import com.syrus.AMFICOM.client.map.SpatialObject;

public class OfxSpatialObject implements SpatialObject
{
	protected SxSpatialObject so;
	protected String label;
		
	public OfxSpatialObject(SxSpatialObject so, String label)
	{
		this.so = so;
		this.label = label;
	}
	
	public String getLabel()
	{
		return this.label;
	}
	
	public SxSpatialObject getSxSpatialObject()
	{
		return this.so;
	}
}
