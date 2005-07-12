package com.syrus.AMFICOM.client.map.mapinfo;

import java.awt.Color;
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
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.util.Log;

public class MapImagePanel extends JPanel
{
 	private static final Color BACKGROUND_COLOR = Color.WHITE;
 	
 	private Image resultImage = null;
	private Image mapImage = null;

	private final MapInfoNetMapViewer viewer;
	
	private boolean forceLNLRepaint = false;

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
 			this.viewer.setMapImageSize(this.getSize());
 			int width = this.getWidth();
 			int height = this.getHeight();
 			if ((width > 0) && (height > 0))
 				this.resultImage = new BufferedImage(
 						width,
 						height,
 						BufferedImage.TYPE_USHORT_565_RGB);

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
		return this.resultImage;
	}

	public void setMapImage(Image newImage)
	{
		this.mapImage = newImage;
	}

	public void paintComponent(Graphics g)
	{
		long t1 = System.currentTimeMillis();		
		super.paintComponent(g);
		long t2 = System.currentTimeMillis();

		if (this.mapImage != null && g != null)
			g.drawImage(this.mapImage, 0, 0,this.getWidth(),this.getHeight(), this);

		long t3 = System.currentTimeMillis();
		
		if (this.forceLNLRepaint)
		{
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
			this.forceLNLRepaint = false;			
		}
		
		long t4 = System.currentTimeMillis();
		
		Log.debugMessage("MapImagePanel.paintComponent | " + "times (ms)\n"
				+	(t2 - t1) + " (super paint)\n"
				+ (t3 - t2) + " (map image paint)\n"
				+ (t4 - t3) + " (LogicalNetLayer paint)", Level.FINE);		
	}

	public void forceLNLRepaint()
	{
		this.forceLNLRepaint = true;
	}
	
	public void refreshLayerImage() throws MapConnectionException, MapDataException
	{
		if (this.resultImage == null)
			return;

		Graphics riGraphics = this.resultImage.getGraphics();
		long t1 = System.currentTimeMillis();

		this.resultImage.getGraphics().drawImage(
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
		Log.debugMessage("MapImagePanel.refreshLayerImage | "
				+ (t2 - t1) + "(painted mapImage to resultImage) "
				+	(t3 - t2) + "(painted LogicalNetLayer), ms.", Level.FINE);		
	}	
}
