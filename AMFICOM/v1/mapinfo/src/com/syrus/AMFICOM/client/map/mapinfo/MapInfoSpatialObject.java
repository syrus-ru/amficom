package com.syrus.AMFICOM.client.map.mapinfo;

import com.syrus.AMFICOM.client.map.SpatialObject;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.HashCodeGenerator;

public class MapInfoSpatialObject implements SpatialObject,Comparable {
	private DoublePoint centre = null;

	private String label = null;

	public MapInfoSpatialObject(final DoublePoint centre, final String featureLabel) {
		this.centre = centre;
		this.label = featureLabel;
	}

	public String getLabel() {
		return this.label;
	}

	public DoublePoint getCenter() {
		return this.centre;
	}
	
	public boolean equals(MapInfoSpatialObject object){
		if (	object.label.equals(this.label)
			&&	object.centre.equals(this.centre))
			return true;
		return false;
	}
	
	public int hashCode(){
		final HashCodeGenerator generator = new HashCodeGenerator();
		generator.addByteArray(this.label.getBytes());		
		generator.addDouble(this.centre.getX());
		generator.addDouble(this.centre.getY());
		return generator.getResult();
	}

	public int compareTo(Object o) {
		if (!(o instanceof MapInfoSpatialObject))
			throw new ClassCastException();
		
		MapInfoSpatialObject spatialObject = (MapInfoSpatialObject)o;
		return (this.label.compareTo(spatialObject.label));
	}
}
