/**
 * $Id: LayerVisibility.java,v 1.1 2005/06/09 11:30:46 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.ui;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/09 11:30:46 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
class LayerVisibility {
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


