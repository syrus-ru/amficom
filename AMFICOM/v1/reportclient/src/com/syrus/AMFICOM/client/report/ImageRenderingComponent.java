/*
 * $Id: ImageRenderingComponent.java,v 1.7 2005/10/08 13:30:14 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.syrus.AMFICOM.report.StorableElement;
import com.syrus.AMFICOM.resource.IntDimension;

public class ImageRenderingComponent extends DataRenderingComponent {

	private static final long serialVersionUID = -7829464177961824335L;
	public static final Dimension DEFAULT_IMAGE_SIZE = new Dimension(200, 200);
	public static final Dimension MINIMUM_IMAGE_SIZE = new Dimension(15, 15);	
	/**
	 * Отображаемое изображение
	 */
	private final BufferedImage imageToRender;	

	public ImageRenderingComponent(StorableElement se, BufferedImage image) {
		super(se);
		this.imageToRender = image;		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		IntDimension size = this.storableElement.getSize();

		double scaleX = (double)size.getWidth() / (double)this.imageToRender.getWidth();
		double scaleY = (double)size.getHeight() / (double)this.imageToRender.getHeight();

		Graphics2D g2D = (Graphics2D) g;
		g2D.scale(scaleX, scaleY);

		g.drawImage(this.imageToRender, 0, 0, Color.white, this);

		g.setColor(Color.black);
		g2D.scale(1.D / scaleX, 1.D / scaleY);
		g.drawRect(0, 0, size.getWidth() - 1, size.getHeight() - 1);
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
	
	@Override
	public void setSize(Dimension newSize) {
		this.setSize(newSize.width,newSize.height);
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(
			width > MINIMUM_IMAGE_SIZE.width ? width : MINIMUM_IMAGE_SIZE.width,
			height > MINIMUM_IMAGE_SIZE.height ? height : MINIMUM_IMAGE_SIZE.height);
	}
	
	public BufferedImage getImage() {
		return this.imageToRender;
	}
}
