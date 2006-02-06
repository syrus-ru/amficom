/*-
 * $$Id: LayerVisibility.java,v 1.2 2005/09/30 16:08:41 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

/**
 * @version $Revision: 1.2 $, $Date: 2005/09/30 16:08:41 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class LayerVisibility {
	Object type;
	Boolean visible;
	Boolean labelVisible;
	private boolean partial;
	private boolean labelPartial;

	public LayerVisibility(
			Object type,
			boolean visible,
			boolean partial,
			boolean labelVisible,
			boolean labelPartial) {
		this.type = type;
		this.partial = partial;
		this.labelPartial = labelPartial;
		this.visible = new Boolean(visible);
		this.labelVisible = new Boolean(labelVisible);
	}

	public LayerVisibility(
			Object type,
			boolean visible,
			boolean labelVisible) {
		this(type, visible, false, labelVisible, false);
	}

	public Boolean getLabelVisible() {
		return this.labelVisible;
	}

	public void setLabelVisible(boolean labelVisible) {
		this.labelVisible = new Boolean(labelVisible);
	}

	public Object getType() {
		return this.type;
	}

	public void setType(Object type) {
		this.type = type;
	}

	public Boolean getVisible() {
		return this.visible;
	}

	public void setVisible(boolean visible) {
		this.visible = new Boolean(visible);
	}

	public boolean isLabelPartial() {
		return this.labelPartial;
	}

	public void setLabelPartial(boolean labelPartial) {
		this.labelPartial = labelPartial;
	}

	public boolean isPartial() {
		return this.partial;
	}

	public void setPartial(boolean partial) {
		this.partial = partial;
	}
}


