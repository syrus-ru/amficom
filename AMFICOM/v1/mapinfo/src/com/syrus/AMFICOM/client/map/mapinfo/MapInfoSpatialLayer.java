package com.syrus.AMFICOM.Client.Map.Mapinfo;
import java.awt.Component;
import com.syrus.AMFICOM.Client.Map.SpatialLayer;

public class MapInfoSpatialLayer implements SpatialLayer 
{
	public MapInfoSpatialLayer()
	{
	}

	public boolean isVisible()
	{
		return false;
	}

	public boolean isLabelVisible()
	{
		return false;
	}

	public void setVisible(boolean visible)
	{
	}

	public void setLabelVisible(boolean visible)
	{
	}

	public Component getLayerImage()
	{
		return null;
	}

	public String getName()
	{
		return null;
	}
}