/*
 * $Id: DataRenderingComponent.java,v 1.2 2005/08/31 10:32:54 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.Point;

import javax.swing.JPanel;

import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.StorableElement;

public abstract class DataRenderingComponent extends JPanel implements RenderingComponent{

	private static final long serialVersionUID = 6913791212343038576L;
	
	public static final String BOUNDS_PROPERTY = "boundsProperty";
	
	public static int EDGE_SIZE = 7;
	public static int DIAGONAL_EDGE_SIZE = 10;
	
	private final DataStorableElement dataRenderingElement;
	/**
	 * Точка клика мыши на надписи
	 */
	private Point mousePressedLocation = new Point();

	protected DataRenderingComponent(DataStorableElement dre)
	{
		this.dataRenderingElement = dre;
		this.setSize(dre.getWidth(),dre.getHeight());
	}

	public StorableElement getElement() {
		return this.dataRenderingElement;
	}
	
	private void setListeners()
	{
		
	}

	public Point getMousePressedLocation() {
		return this.mousePressedLocation;
	}

	public void setMousePressedLocation(Point mousePressedLocation) {
		this.mousePressedLocation = mousePressedLocation;
	}
}
