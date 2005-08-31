/*
 * $Id: RenderingComponent.java,v 1.1 2005/08/31 10:32:55 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import com.syrus.AMFICOM.report.StorableElement;

public interface RenderingComponent {
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
