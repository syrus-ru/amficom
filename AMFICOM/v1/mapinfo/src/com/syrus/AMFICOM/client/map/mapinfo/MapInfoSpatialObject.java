package com.syrus.AMFICOM.client.map.mapinfo;

import com.syrus.AMFICOM.client.map.SpatialObject;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.HashCodeGenerator;

public class MapInfoSpatialObject extends SpatialObject {
	private DoublePoint centre = null;
	
	public MapInfoSpatialObject(final DoublePoint centre, final String featureLabel) {
		super(featureLabel);
		this.centre = centre;
	}

	public DoublePoint getCenter() {
		return this.centre;
	}

	public boolean equals(MapInfoSpatialObject object){
		if (	object.getLabel().equals(this.getLabel())
			&&	object.centre.equals(this.centre))
			return true;
		return false;
	}
	
	@Override
	public int hashCode(){
		final HashCodeGenerator generator = new HashCodeGenerator();
		generator.addByteArray(this.label.getBytes());		
		generator.addDouble(this.centre.getX());
		generator.addDouble(this.centre.getY());
		return generator.getResult();
	}
}
