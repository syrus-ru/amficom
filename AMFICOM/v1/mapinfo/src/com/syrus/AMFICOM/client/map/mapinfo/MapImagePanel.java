package com.syrus.AMFICOM.Client.Map.Mapinfo;

import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class MapImagePanel extends JPanel
{
  private Image mapImage;
  private boolean imageChanged = true;
  
  private DoublePoint center = new DoublePoint (0,0);
  private double zoom = 1;
  
	public static final double ZOOM_FACTOR = 2D;  
  
  public MapImagePanel()
  {
  }
  
  public void setImage(Image newImage)
  {
    this.mapImage = newImage;
    this.setImageChanged(false);    
    this.repaint();
  }

/*  public boolean imageUpdate(Image img, int infoflag, int x, int y, 
                             int width, int height) 
  {
    boolean result = true;

    if ((infoflag & (ERROR | ABORT)) != 0) 
    {
      result = false;
    }
    else 
    {
      if ((infoflag & ALLBITS) != 0) 
      {
        result = false;
      }
      repaint();
    }
    return result;
  }

  public void update(Graphics g)
  {
      paint(g);
  }*/

  public void setImageChanged(boolean ic)
  {
    this.imageChanged = ic;
  }

  public boolean getImageChanged()
  {
    return this.imageChanged;
  }

  public void setCenter(DoublePoint c)
  {
    center = c;
    this.setImageChanged(true);
  }

  public DoublePoint getCenter()
  {
    return center;
  }

  public void setZoom(double z)
  {
    zoom = z;
    this.setImageChanged(true);
  }

  public double getZoom()
  {
    return zoom;
  }

	/**
	 * Установить масштаб вида карты с заданным коэффициентом
	 */
	public void scaleTo(double scaleСoef)
	{
    setZoom(getZoom() * scaleСoef);
	}

	/**
	 * Приблизить вид карты со стандартным коэффициентом
	 */
	public void zoomIn()
	{
		scaleTo(1.0D / ZOOM_FACTOR);
	}

	/**
	 * Отдалить вид карты со стандартным коэффициентом
	 */
	public void zoomOut()
	{
		scaleTo(ZOOM_FACTOR);
	}


  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
		if (mapImage != null && g != null)
		{
			    g.drawImage(this.mapImage, 0, 0, this);
		}
  }
  
  public String getMapMainParamString()
  {
    String result = "";
    result += "&" + ServletCommandNames.WIDTH + "=" + this.getWidth();
    result += "&" + ServletCommandNames.HEIGHT + "=" + this.getHeight();
    result += "&" + ServletCommandNames.CENTER_X + "=" + this.getCenter().x;
    result += "&" + ServletCommandNames.CENTER_Y + "=" + this.getCenter().y;
    result += "&" + ServletCommandNames.ZOOM_FACTOR + "=" + this.getZoom();
    
    return result;
  }
}