/*
 * $Id: DataRenderingComponent.java,v 1.5 2006/04/24 06:28:37 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.Color;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.syrus.AMFICOM.report.StorableElement;

public abstract class DataRenderingComponent extends JPanel implements RenderingComponent{

	private static final long serialVersionUID = 6913791212343038576L;
	
	protected StorableElement storableElement;
	/**
	 * Точка клика мыши на надписи
	 */
	private Point mousePressedLocation = new Point();

	public static Border DEFAULT_BORDER = BorderFactory.createEmptyBorder();
	
	public static Border SELECTION_BORDER = BorderFactory.createLineBorder(Color.BLACK, 1);

	protected DataRenderingComponent(StorableElement dre){
		this.storableElement = dre;
	}

	public StorableElement getElement() {
		return this.storableElement;
	}
	
	public Point getMousePressedLocation() {
		return this.mousePressedLocation;
	}

	public void setMousePressedLocation(Point mousePressedLocation) {
		this.mousePressedLocation = mousePressedLocation;
	}
}
