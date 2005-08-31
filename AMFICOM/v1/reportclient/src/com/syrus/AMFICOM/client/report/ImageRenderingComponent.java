/*
 * $Id: ImageRenderingComponent.java,v 1.2 2005/08/31 10:32:54 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.report.StorableElement;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.resource.IntPoint;

public class ImageRenderingComponent extends JPanel implements RenderingComponent {

	private static final long serialVersionUID = -7829464177961824335L;
	private final ImageStorableElement imageRenderingElement;

	public ImageRenderingComponent(ImageStorableElement ire) {
		this.imageRenderingElement = ire;
	}

	public StorableElement getElement() {
		return this.imageRenderingElement;
	}

	public void paintComponent(Graphics g) {
		BufferedImage image = this.imageRenderingElement.getImage();
		IntPoint location = this.imageRenderingElement.getLocation();
		IntDimension size = this.imageRenderingElement.getSize();

		double scaleX = size.getWidth() / image.getWidth();
		double scaleY = size.getHeight() / image.getHeight();

		Graphics2D g2D = (Graphics2D) g;
		g2D.scale(scaleX, scaleY);

		g.drawImage(image, location.x, location.y, Color.white, this);

		g.setColor(Color.black);
		g2D.scale(1 / scaleX, 1 / scaleY);
		g.drawRect(0, 0, size.getWidth(), size.getHeight());
	}
	
	public void setX(int x) {
		this.setLocation(x,this.getY());
	}

	public void setY(int y) {
		this.setLocation(this.getX(),y);
	}

	public void setWidth(int width) {
		this.setSize(width,this.getHeight());
	}

	public void setHeight(int height) {
		this.setSize(this.getWidth(),height);
	}
}
