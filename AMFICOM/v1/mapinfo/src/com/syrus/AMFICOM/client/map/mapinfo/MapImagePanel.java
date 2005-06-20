package com.syrus.AMFICOM.client.map.mapinfo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.ui.MapFrame;

public class MapImagePanel extends JPanel
{
	private Image mapImage = null;

	private final MapInfoNetMapViewer viewer;

	public MapImagePanel(MapInfoNetMapViewer viewer)
	{
		this.viewer = viewer;
		ComponentListener[] listeners = this.getComponentListeners();
		for (int i = 0; i < listeners.length; i++)
			this.removeComponentListener(listeners[i]);

		this.addComponentListener(new ComponentAdapter()
				{
					public void componentResized(ComponentEvent e)
					{
						setLayerSize();
					}
				});
		
		this.addPropertyChangeListener(MapFrame.MAP_FRAME_SHOWN, new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt)
			{
				if(evt.getPropertyName().equals(MapFrame.MAP_FRAME_SHOWN)
						&& ((Boolean )evt.getNewValue()).booleanValue())
				setLayerSize();
			}
		});
	}

	void setLayerSize()
	{
			try
			{
				this.viewer.setMapImageSize(
						this.getWidth(),
						this.getHeight());
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

	public Image getImage()
	{
		return this.mapImage;
	}
	
	public void setImage(Image newImage)
	{
   if (newImage != null)
   		this.mapImage = newImage;
	}
	
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		if(this.mapImage != null && g != null)
			g.drawImage(this.mapImage, 0, 0, this);

			try
			{
				this.viewer.getLogicalNetLayer().paint(g, this.viewer.getVisibleBounds());
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
}
