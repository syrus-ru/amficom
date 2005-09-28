/*-
 * $Id: Selection.java,v 1.28 2005/09/28 15:03:15 krupenn Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import java.util.Set;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * Набор выбранных элементов.
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.28 $, $Date: 2005/09/28 15:03:15 $
 * @module mapview
 */
public final class Selection extends MapTypedElementsContainer implements MapElement {
	/**
	 * {@inheritDoc}
	 */
	public boolean isRemoved() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRemoved(final boolean removed) {
		// cannot be removed
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public void setAlarmState(final boolean alarmState) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public boolean getAlarmState() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DoublePoint getLocation() {
		return super.getLocation();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public boolean isSelected() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public MapElementState getState() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} cannnot be selected
	 */
	public void setSelected(final boolean selected) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public Identifier getId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return "selection";
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public void setName(final String name) {
		throw new UnsupportedOperationException();
	}

	public String getToolTipText() {
		return this.getName();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public void revert(final MapElementState state) {
		throw new UnsupportedOperationException();
	}

	public Characterizable getCharacterizable() {
		return null;
	}

}
