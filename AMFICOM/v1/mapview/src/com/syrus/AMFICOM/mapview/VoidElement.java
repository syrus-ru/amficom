/**
 * $Id: VoidElement.java,v 1.14 2005/07/17 05:20:55 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������
 * ������������������� ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.mapview;

import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;

/**
 * ������ �������.
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.14 $, $Date: 2005/07/17 05:20:55 $
 * @module mapviewclient_v1
 */
public final class VoidElement implements MapElement {
	/**
	 * ��� �����.
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
	 * @param mapView ��� �����
	 * @return instance
	 */
	public static VoidElement getInstance(MapView mapView) {
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
	public void setSelected(boolean selected) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public void setAlarmState(boolean alarmState) {
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
	public void setName(String name) {
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
	public Set<Characteristic> getCharacteristics() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public void addCharacteristic(Characteristic ch) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public void removeCharacteristic(Characteristic ch) {
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
	public void setRemoved(boolean removed) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	@SuppressWarnings("deprecation")
	public Map getExportMap() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(Set characteristics) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(Set)
	 */
	public void setCharacteristics0(Set characteristics) {
		throw new UnsupportedOperationException();
	}
}
