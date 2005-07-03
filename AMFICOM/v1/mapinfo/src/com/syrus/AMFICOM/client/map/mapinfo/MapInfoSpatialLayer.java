package com.syrus.AMFICOM.Client.Map.Mapinfo;

import java.awt.Component;

import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.unit.Distance;
import com.syrus.AMFICOM.Client.Map.SpatialLayer;

public class MapInfoSpatialLayer implements SpatialLayer
{
	private boolean visible = true;
	
	private FeatureLayer mapLayer = null;

	private MapInfoLogicalNetLayer layerToRepaint = null;

	public MapInfoSpatialLayer(
			FeatureLayer mapLayer,
			MapInfoLogicalNetLayer layerToRepaint)
	{
		this.mapLayer = mapLayer;
		this.layerToRepaint = layerToRepaint;
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

	public boolean isVisibleAtCurrentScale()
	{
		return isVisibleAtScale(this.layerToRepaint.getScale());
	}
	
	public void setVisible(boolean visible)
	{
		this.visible = visible;
		this.layerToRepaint.refreshLayers();		
		if (this.isVisibleAtScale(this.layerToRepaint.getScale()))
		{
			this.layerToRepaint.repaint(true);			
		}
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
