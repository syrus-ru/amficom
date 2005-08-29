package com.syrus.AMFICOM.client.map.objectfx;

import com.ofx.repository.SxSpatialObject;
import com.syrus.AMFICOM.client.map.SpatialObject;

public class OfxSpatialObject extends SpatialObject {
	protected SxSpatialObject so;
		
	public OfxSpatialObject(SxSpatialObject so, String label) {
		super(label);
		this.so = so;
	}
	
	public SxSpatialObject getSxSpatialObject() {
		return this.so;
	}
}
