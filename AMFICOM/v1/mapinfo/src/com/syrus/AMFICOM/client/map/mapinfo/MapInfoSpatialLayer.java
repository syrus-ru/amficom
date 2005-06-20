package com.syrus.AMFICOM.client.map.mapinfo;

import java.awt.Component;

import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.unit.Distance;
import com.syrus.AMFICOM.client.map.SpatialLayer;

public class MapInfoSpatialLayer implements SpatialLayer
{
	private boolean visible = true;
	
	private FeatureLayer mapLayer = null;

	public MapInfoSpatialLayer(
			FeatureLayer mapLayer)
	{
		this.mapLayer = mapLayer;
	}

	public MapInfoSpatialLayer()
	{//empty
	}

	public boolean isVisible()
	{
		return this.visible;
	}

	public boolean isLabelVisible()
	{
		boolean returnValue = false;
		try
		{
			//Проверяем видны ли надписи на текущем масштабе на сервере
			returnValue = this.mapLayer.isAutoLabel();
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
		
		//надписи видны когда виден слой.
		returnValue &= this.visible;
		
		return returnValue;
	}

	public boolean isVisibleAtScale(double scale)
	{
		boolean returnValue = false;
		try
		{
			if (this.mapLayer.isZoomLayer())
			{
				Distance minZoom = this.mapLayer.getMinZoom();
				Distance maxZoom = this.mapLayer.getMaxZoom();
				if ((minZoom.getScalarValue() < scale) && (scale < maxZoom.getScalarValue()))
					returnValue = true;
			}
			else
				returnValue = true;
//			returnValue = this.mapLayer.isVisibleAtCurrentZoom();
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}

		return returnValue;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	public void setLabelVisible(boolean visible)
	{
		throw new UnsupportedOperationException();
	}

	public Component getLayerImage()
	{
		/*
		 * JPanel panel = new JPanel(); Canvas canvas = new Canvas();
		 * this.mapLayer. panel.add( return ;
		 */
		return null;
	}

	public String getName()
	{
		return this.mapLayer.getName();
	}
}
