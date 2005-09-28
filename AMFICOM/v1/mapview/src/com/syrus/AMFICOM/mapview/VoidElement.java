/*-
 * $Id: VoidElement.java,v 1.23 2005/09/28 15:03:15 krupenn Exp $
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
 * Пустой элемент.
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.23 $, $Date: 2005/09/28 15:03:15 $
 * @module mapview
 */
public final class VoidElement implements MapElement {
	/**
	 * Вид карты.
	 */
	protected MapView mapView;

	/**
	 * Instance.
	 */
	protected static VoidElement singleton = new VoidElement();

	/**
	 * Protected constructor.
	 */
	protected VoidElement() {
		// empty
	}

	/**
	 * Get instance.
	 *
	 * @param mapView вид карты
	 * @return instance
	 */
	public static VoidElement getInstance(final MapView mapView) {
		singleton.mapView = mapView;
		return singleton;
	}

	/**
	 * Получить вид карты.
	 *
	 * @return вид карты
	 */
	public MapView getMapView() {
		return this.mapView;
	}

	public String getToolTipText() {
		return getMapView().getName();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected() {
		return true;
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public MapElementState getState() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public void setSelected(final boolean selected) {
		throw new UnsupportedOperationException();
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
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public Identifier getId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public String getName() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public void setName(final String name) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public DoublePoint getLocation() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public void revert(MapElementState state) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public boolean isRemoved() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public void setRemoved(final boolean removed) {
		throw new UnsupportedOperationException();
	}

	public Characterizable getCharacterizable() {
		return null;
	}

}
