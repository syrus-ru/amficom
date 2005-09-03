/*
 * $Id: RenderingComponent.java,v 1.2 2005/09/03 12:42:19 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import com.syrus.AMFICOM.report.StorableElement;

public interface RenderingComponent {
	public static final String BOUNDS_PROPERTY = "boundsProperty";
	public static final int EDGE_SIZE = 7;
	public static final int DIAGONAL_EDGE_SIZE = 10;
	public abstract StorableElement getElement();
	
	//На самом деле, методы getX()и getWidth() не нужны - они нигде не
	//используются. Сделаны для симметрии.
	public abstract int getX();
	public abstract int getY();
	public abstract int getWidth();
	public abstract int getHeight();
	
	public abstract void setX(int x);
	public abstract void setY(int y);
	public abstract void setWidth(int width);
	public abstract void setHeight(int height);
}
