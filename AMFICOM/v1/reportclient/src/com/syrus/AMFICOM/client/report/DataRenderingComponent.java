/*
 * $Id: DataRenderingComponent.java,v 1.1 2005/08/12 10:23:10 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.Point;

import javax.swing.JPanel;

import com.syrus.AMFICOM.report.DataRenderingElement;
import com.syrus.AMFICOM.report.RenderingElement;

public abstract class DataRenderingComponent extends JPanel implements ReportComponent{

	private static final long serialVersionUID = 6913791212343038576L;
	
	public static final String BOUNDS_PROPERTY = "boundsProperty";
	
	public static int EDGE_SIZE = 7;
	public static int DIAGONAL_EDGE_SIZE = 10;
	
	private final DataRenderingElement dataRenderingElement;
	/**
	 * Точка клика мыши на надписи
	 */
	private Point mousePressedLocation = new Point();

	protected DataRenderingComponent(DataRenderingElement dre)
	{
		this.dataRenderingElement = dre;
		this.setSize(dre.getSize());
	}

	public RenderingElement getElement() {
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
