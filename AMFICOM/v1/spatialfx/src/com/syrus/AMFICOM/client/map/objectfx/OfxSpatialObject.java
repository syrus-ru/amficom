package com.syrus.AMFICOM.Client.Map.ObjectFX;

import com.ofx.repository.SxSpatialObject;

import com.syrus.AMFICOM.Client.Map.SpatialObject;

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
		return label;
	}
	
	public SxSpatialObject getSxSpatialObject()
	{
		return so;
	}
}