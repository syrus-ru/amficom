/**
 * $Id: VoidElement.java,v 1.19 2005/08/12 12:17:44 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный
 * МногоФункциональный Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.mapview;

import java.util.Set;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.DoublePoint;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;

/**
 * Пустой элемент.
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.19 $, $Date: 2005/08/12 12:17:44 $
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
	public Set<Characteristic> getCharacteristics(final boolean usePool) {
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

}
