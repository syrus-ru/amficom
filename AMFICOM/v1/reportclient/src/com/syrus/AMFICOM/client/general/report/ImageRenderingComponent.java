/*
 * $Id: ImageRenderingComponent.java,v 1.1 2005/08/11 11:17:34 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.general.report;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.syrus.AMFICOM.report.ImageRenderingElement;
import com.syrus.AMFICOM.report.RenderingElement;

public class ImageRenderingComponent extends JPanel implements ReportComponent {

	private static final long serialVersionUID = -7829464177961824335L;
	private final ImageRenderingElement imageRenderingElement;

	public ImageRenderingComponent(ImageRenderingElement ire) {
		this.imageRenderingElement = ire;
	}

	public RenderingElement getElement() {
		return this.imageRenderingElement;
	}

	public void paintComponent(Graphics g) {
		BufferedImage image = this.imageRenderingElement.getImage();
		Point location = this.imageRenderingElement.getLocation();
		Dimension size = this.imageRenderingElement.getSize();

		double scaleX = size.width / image.getWidth();
		double scaleY = size.height / image.getHeight();

		Graphics2D g2D = (Graphics2D) g;
		g2D.scale(scaleX, scaleY);

		g.drawImage(image, location.x, location.y, Color.white, this);

		g.setColor(Color.black);
		g2D.scale(1 / scaleX, 1 / scaleY);
		g.drawRect(0, 0, size.width, size.height);
	}
}
