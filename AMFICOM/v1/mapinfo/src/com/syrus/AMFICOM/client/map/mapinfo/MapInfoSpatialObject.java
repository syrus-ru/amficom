package com.syrus.AMFICOM.Client.Map.Mapinfo;
import com.mapinfo.dp.Feature;
import com.mapinfo.dp.Attribute;
import com.mapinfo.util.DoublePoint;
import com.mapinfo.util.DoubleRect;
import com.syrus.AMFICOM.Client.Map.SpatialObject;

public class MapInfoSpatialObject implements SpatialObject 
{
  private Feature feature = null;
  private String label = null;
  
	public MapInfoSpatialObject(Feature feature, String featureLabel)
	{
    this.feature = feature;
    this.label = featureLabel;
	}

	public String getLabel()
	{
		return null;
	}
  
  public DoublePoint getCenter()
  {
    try
    {
      DoubleRect bounds = this.feature.getGeometry().getBounds();
      return bounds.center();
    }
    catch(Exception exc)
    {
      exc.printStackTrace();
    }
    return null;
  }
}