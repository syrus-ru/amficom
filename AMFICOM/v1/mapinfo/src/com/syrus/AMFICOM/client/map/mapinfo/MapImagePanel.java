package com.syrus.AMFICOM.client.map.mapinfo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;

import javax.swing.JPanel;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.util.Log;

public class MapImagePanel extends JPanel
{
 	private Image resultImage = null;
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

		this.addPropertyChangeListener(MapFrame.MAP_FRAME_SHOWN,
				new PropertyChangeListener()
				{

					public void propertyChange(PropertyChangeEvent evt)
					{
						if (evt.getPropertyName().equals(MapFrame.MAP_FRAME_SHOWN)
								&& ((Boolean) evt.getNewValue()).booleanValue())
							setLayerSize();
					}
				});
	}

	void setLayerSize()
	{
		try
		{
 			int width = this.getWidth();
 			int height = this.getHeight();
 			if ((width > 0) && (height > 0))
 				this.resultImage = new BufferedImage(
 						width,
 						height,
 						BufferedImage.TYPE_USHORT_565_RGB);

			this.viewer.setMapImageSize(this.getSize());
		} catch (MapException e)
		{
			Log.errorMessage("MapImagePanel.setLayerSize | " + e.getMessage());
			Log.errorException(e);
		}
	}

	public Image getImage()
	{
		return this.resultImage;
	}

	public void setMapImage(Image newImage)
	{
		if (newImage != null)
			this.mapImage = newImage;
	}

	public void paintComponent(Graphics g)
	{
		long t1 = System.currentTimeMillis();		
		super.paintComponent(g);
		long t2 = System.currentTimeMillis();

		if (this.resultImage != null && g != null)
			g.drawImage(this.resultImage, 0, 0,this.getWidth(),this.getHeight(), this);

		long t3 = System.currentTimeMillis();

		Log.debugMessage("MapImagePanel.paintComponent | total " + (t3 - t1) + " (ms)\n"
				+ "		" + (t2 - t1) + " ms (super paint)\n"
				+ "		" + (t3 - t2) + " ms (result image paint)\n", Level.INFO);		
	}

	public void refreshLayerImage() throws MapConnectionException, MapDataException
	{
		if (this.resultImage == null)
			return;

		if (this.mapImage == null)
			return;
		
		Graphics riGraphics = this.resultImage.getGraphics();
		long t1 = System.currentTimeMillis();

		riGraphics.drawImage(
				this.mapImage,
				0,
				0,
				this.getWidth(),
				this.getHeight(),
				this);

		long t2 = System.currentTimeMillis();

		this.viewer.getLogicalNetLayer().paint(
				riGraphics,
				this.viewer.getVisibleBounds());

		long t3 = System.currentTimeMillis();		
		Log.debugMessage("MapImagePanel.refreshLayerImage | total " + (t3 - t1)
				+ "\n		" + (t2 - t1) + " ms (painted mapImage to resultImage) "
				+ "\n		" + (t3 - t2) + " ms (painted LogicalNetLayer), ms.", Level.INFO);		
	}	
}
