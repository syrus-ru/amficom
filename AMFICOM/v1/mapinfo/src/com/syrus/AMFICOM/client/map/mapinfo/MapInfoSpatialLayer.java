package com.syrus.AMFICOM.Client.Map.Mapinfo;
import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.mapj.Layers;
import java.awt.Canvas;
import java.awt.Component;
import com.syrus.AMFICOM.Client.Map.SpatialLayer;
import javax.swing.JPanel;

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
	{
	}

	public boolean isVisible()
	{
    boolean returnValue = false;
    try
    {
      returnValue = this.mapLayer.isVisibleAtCurrentZoom();
    }
    catch (Exception exc)
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
    catch (Exception exc)
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
    catch (Exception exc)
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
    catch (Exception exc)
    {
      exc.printStackTrace();
    }
	}

	public Component getLayerImage()
	{
/*    JPanel panel = new JPanel();
    Canvas canvas = new Canvas();
    this.mapLayer.
    panel.add(
		return ;*/
    return null;
	}

	public String getName()
	{
		return this.mapLayer.getName();
	}
}