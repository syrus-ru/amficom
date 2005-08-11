/*
 * $Id: MapImagePanel.java,v 1.14 2005/08/11 13:55:50 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
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

/**
 * @version $Revision: 1.14 $, $Date: 2005/08/11 13:55:50 $
 * @author $Author: arseniy $
 * @module mapinfo
 */
public class MapImagePanel extends JPanel {
	private static final long serialVersionUID = -7769295230950865949L;

	private Image resultImage = null;
	private Image mapImage = null;

	private final MapInfoNetMapViewer viewer;

	public MapImagePanel(final MapInfoNetMapViewer viewer) {
		this.viewer = viewer;
		final ComponentListener[] listeners = this.getComponentListeners();
		for (int i = 0; i < listeners.length; i++) {
			this.removeComponentListener(listeners[i]);
		}

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent e) {
				MapImagePanel.this.setLayerSize();
			}
		});

		this.addPropertyChangeListener(MapFrame.MAP_FRAME_SHOWN, new PropertyChangeListener() {

			public void propertyChange(final PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(MapFrame.MAP_FRAME_SHOWN) && ((Boolean) evt.getNewValue()).booleanValue()) {
					MapImagePanel.this.setLayerSize();
				}
			}
		});
	}

	void setLayerSize() {
		try {
			final int width = this.getWidth();
			final int height = this.getHeight();
			if ((width > 0) && (height > 0)) {
				this.resultImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
			}

			this.viewer.setMapImageSize(this.getSize());
		} catch (MapException e) {
			Log.errorMessage("MapImagePanel.setLayerSize | " + e.getMessage());
			Log.errorException(e);
		}
	}

	public Image getImage() {
		return this.resultImage;
	}

	public void setMapImage(final Image newImage) {
		if (newImage != null) {
			this.mapImage = newImage;
		}
	}

	@Override
	public void paintComponent(final Graphics g) {
		final long t1 = System.currentTimeMillis();
		super.paintComponent(g);
		final long t2 = System.currentTimeMillis();

		if (this.resultImage != null && g != null) {
			g.drawImage(this.resultImage, 0, 0, this.getWidth(), this.getHeight(), this);
		}

		final long t3 = System.currentTimeMillis();

		Log.debugMessage("MapImagePanel.paintComponent | total " + (t3 - t1) + " (ms)\n" + "		"
				+ (t2 - t1) + " ms (super paint)\n" + "		"
				+ (t3 - t2) + " ms (result image paint)\n", Level.INFO);
	}

	public void refreshLayerImage() throws MapConnectionException, MapDataException {
		if (this.resultImage == null) {
			return;
		}

		if (this.mapImage == null) {
			return;
		}

		final Graphics riGraphics = this.resultImage.getGraphics();
		final long t1 = System.currentTimeMillis();

		riGraphics.drawImage(this.mapImage, 0, 0, this.getWidth(), this.getHeight(), this);

		final long t2 = System.currentTimeMillis();

		this.viewer.getLogicalNetLayer().paint(riGraphics, this.viewer.getVisibleBounds());

		final long t3 = System.currentTimeMillis();
		Log.debugMessage("MapImagePanel.refreshLayerImage | total " + (t3 - t1) + "\n		"
				+ (t2 - t1) + " ms (painted mapImage to resultImage) " + "\n		"
				+ (t3 - t2) + " ms (painted LogicalNetLayer), ms.", Level.INFO);
	}
}
