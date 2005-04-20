package com.syrus.AMFICOM.Client.Map.Mapinfo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;

public class MapImagePanel extends JPanel
{
	private Image mapImage = null;
	private boolean imageIsMoving = false;

	MapInfoLogicalNetLayer layerToPaint = null;

	public MapImagePanel()
	{
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

	public Image getImage()
	{
		return this.mapImage;
	}
	
	public void setImage(Image newImage)
	{
		this.mapImage = newImage;
		this.imageIsMoving = false;
//		if (newImage != null)
//			this.mapImage.getGraphics().drawImage(newImage,0,0,this);
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
		if (this.imageIsMoving == false)
		{
			try
			{
				this.layerToPaint.paint(this.mapImage.getGraphics());
			} catch (MapConnectionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MapDataException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.imageIsMoving = true;
		}
		
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
	}
}
