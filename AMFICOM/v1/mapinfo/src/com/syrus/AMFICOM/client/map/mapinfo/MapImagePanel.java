package com.syrus.AMFICOM.Client.Map.Mapinfo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;

public class MapImagePanel extends JPanel
{
	private Image mapImage = null;
	private Image mapPaintableImage = null;

	MapInfoLogicalNetLayer layerToPaint = null;

	public MapImagePanel()
	{
		this.setDoubleBuffered(true);
		
		ComponentListener[] listeners = this.getComponentListeners();
		for (int i = 0; i < listeners.length; i++)
			this.removeComponentListener(listeners[i]);

		this.addComponentListener(new ComponentAdapter()
		{
			private void setLayerSize()
			{
				if(MapImagePanel.this.layerToPaint != null)
					MapImagePanel.this.layerToPaint.setMapImageSize(
							MapImagePanel.this.getWidth(),
							MapImagePanel.this.getHeight());
			}
			public void componentResized(ComponentEvent e)
			{
				setLayerSize();
			}

			public void componentShown(ComponentEvent e)
			{
				setLayerSize();
			}
		});
	}

	public void refreshImages()
	{
		if (	(this.mapImage == null)
				||(this.mapImage.getWidth(this) != this.getWidth())
				||(this.mapImage.getHeight(this) != this.getHeight()))
		{
			this.mapImage = new BufferedImage(
					this.getWidth(),
					this.getHeight(),
				BufferedImage.TYPE_USHORT_565_RGB);
			
			this.mapPaintableImage = new BufferedImage(
					this.getWidth(),
					this.getHeight(),
				BufferedImage.TYPE_USHORT_565_RGB);
		}
	}
	
	public void redrawMainImage()
	{
		this.mapImage.getGraphics().drawImage(this.mapPaintableImage,0,0,this);
	}
	
	public Image getPaintableImage()
	{
		return this.mapPaintableImage;
	}

	public Image getCurrentImage()
	{
		return this.mapImage;
	}
	
	public void setLogicalLayer(MapInfoLogicalNetLayer layerToPaint)
	{
		this.layerToPaint = layerToPaint;
		this.repaint();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		if(this.mapImage != null && g != null)
			g.drawImage(this.mapImage, 0, 0, this);

		if(this.layerToPaint != null)
			try
			{
				this.layerToPaint.paint(g);
			} catch (MapConnectionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MapDataException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void repaint(Graphics g, int shiftX, int shiftY)
	{
		g.setColor(Color.GRAY);

		if(shiftX > 0)
			g.fillRect(0, 0, shiftX, this.getHeight());
		else
			if(shiftX < 0)
				g.fillRect(this.getWidth() + shiftX, 0, -shiftX, this
						.getHeight());

		if(shiftY > 0)
			g.fillRect(0, 0, this.getWidth(), shiftY);
		else
			if(shiftY < 0)
				g.fillRect(
						0,
						this.getHeight() + shiftY,
						this.getWidth(),
						-shiftY);

		if(this.mapImage != null && g != null)
			g.drawImage(this.mapImage, shiftX, shiftY, this);

		/*
		 * if (this.layerToPaint != null) layerToPaint.paint(g);
		 */
	}
}
