package com.syrus.AMFICOM.Client.Map.Mapinfo;

import java.awt.Component;

import com.mapinfo.mapj.FeatureLayer;
import com.syrus.AMFICOM.Client.Map.SpatialLayer;

public class MapInfoSpatialLayer implements SpatialLayer
{
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
		boolean returnValue = false;
		try
		{
			returnValue = this.mapLayer.isVisibleAtCurrentZoom();
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}

		return returnValue;
	}

	public boolean isLabelVisible()
	{
		boolean returnValue = false;
		try
		{
			returnValue = this.mapLayer.isAutoLabel();
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}

		return returnValue;
	}

	public void setVisible(boolean visible)
	{
		try
		{
			this.mapLayer.setEnabled(visible);
			this.layerToRepaint.repaint(true);
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
	}

	public void setLabelVisible(boolean visible)
	{
		try
		{
			this.mapLayer.setAutoLabel(visible);
			this.layerToRepaint.repaint(true);
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
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
