package com.syrus.AMFICOM.Client.Map.Mapinfo;

import java.awt.Graphics;
import java.awt.Image;

import java.awt.SystemColor;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.io.IOException;
import javax.swing.JPanel;

public class MapImagePanel extends JPanel
{
  private Image mapImage;
  private MapInfoLogicalNetLayer layerToPaint = null;
  
  public MapImagePanel()
  {
    this.setDoubleBuffered(true);
    
    this.addComponentListener(new ComponentAdapter()
    {
      public void componentResized(ComponentEvent e)
      {
        if (layerToPaint != null)
          layerToPaint.setMapImageSize(
            e.getComponent().getWidth(),
            e.getComponent().getHeight());
      }
      
      public void componentShown(ComponentEvent e)
      {
        if (layerToPaint != null)
          layerToPaint.setMapImageSize(
            e.getComponent().getWidth(),
            e.getComponent().getHeight());
      }
    });
  }
  
  public void setImage(Image newImage)
  {
    this.mapImage = newImage;
    this.repaint();
  }

  public void setLogicalLayer(MapInfoLogicalNetLayer layerToPaint)
  {
    this.layerToPaint = layerToPaint;
    this.repaint();
  }

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
		if (mapImage != null && g != null)
      g.drawImage(this.mapImage, 0, 0, this);

    if (this.layerToPaint != null)
      layerToPaint.paint(g);
  }
  
  public void repaint(Graphics g, int shiftX, int shiftY)
  {
//    super.paintComponent(g);
    g.setColor(SystemColor.GRAY);
    
    if (shiftX > 0)
      g.fillRect(0,0,shiftX,this.getHeight());
    else if (shiftX < 0)
      g.fillRect(this.getWidth() + shiftX,0,-shiftX,this.getHeight());
      
    if (shiftY > 0)
      g.fillRect(0,0,this.getWidth(),shiftY);
    else if (shiftY < 0)
      g.fillRect(0,this.getHeight() + shiftY,this.getWidth(),-shiftY);


		if (mapImage != null && g != null)
      g.drawImage(this.mapImage, shiftX, shiftY, this);

/*    if (this.layerToPaint != null)
      layerToPaint.paint(g);*/
  }
}