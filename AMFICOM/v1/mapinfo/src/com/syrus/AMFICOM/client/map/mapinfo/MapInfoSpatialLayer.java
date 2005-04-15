package com.syrus.AMFICOM.Client.Map.Mapinfo;

import java.awt.Component;

import com.mapinfo.mapj.FeatureLayer;
import com.syrus.AMFICOM.Client.Map.SpatialLayer;

public class MapInfoSpatialLayer implements SpatialLayer
{
	private boolean visible = true;
	private boolean labelsVisible = true;	
	
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
		return true;
	}

	public boolean isVisibleAtCurrentScale()
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
	
	public void setVisible(boolean visible)
	{
		this.visible = visible;
		this.layerToRepaint.refreshLayers();		
		if (this.isVisibleAtCurrentScale())
		{
			this.layerToRepaint.repaint(true);			
		}
	}

	public void setLabelVisible(boolean visible)
	{
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
