/*
 * $Id: RenderingElement.java,v 1.1 2005/08/11 11:15:30 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import java.awt.Rectangle;

public abstract class RenderingElement extends Rectangle{

	/**
	 * Время последнего изменения объекта
	 */
	private long modified = 0L;

	public long getModified() {
		return this.modified;
	}

	public void setModified(long modified) {
		this.modified = modified;
	}
}
