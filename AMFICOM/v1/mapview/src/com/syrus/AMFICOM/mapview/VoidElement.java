/*-
 * $Id: VoidElement.java,v 1.25 2005/09/29 11:34:11 krupenn Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * ������ �������.
 *
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @version $Revision: 1.25 $, $Date: 2005/09/29 11:34:11 $
 * @module mapview
 */
public final class VoidElement implements MapElement {
	/**
	 * ��� �����.
	 */
	private MapView mapView;

	/**
	 * Instance.
	 */
	private static VoidElement singleton = new VoidElement();

	/**
	 * Protected constructor.
	 */
	private VoidElement() {
		// empty
	}

	/**
	 * Get instance.
	 *
	 * @param mapView ��� �����
	 * @return instance
	 */
	public static VoidElement getInstance(final MapView mapView) {
		singleton.mapView = mapView;
		return singleton;
	}

	/**
	 * �������� ��� �����.
	 *
	 * @return ��� �����
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
