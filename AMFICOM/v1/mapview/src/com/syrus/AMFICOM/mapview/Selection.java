/*-
 * $Id: Selection.java,v 1.16 2005/07/26 13:25:11 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный
 * МногоФункциональный Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.mapview;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;

/**
 * Набор выбранных элементов.
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.16 $, $Date: 2005/07/26 13:25:11 $
 * @module mapviewclient_v1
 * @todo copy/paste, properties
 */
public final class Selection implements MapElement {
	/**
	 * Список выбранных элементов.
	 */
	protected Set<MapElement> elements = new HashSet<MapElement>();

	/**
	 * Общий центр выбранных элементов.
	 */
	protected DoublePoint location = new DoublePoint(0, 0);

	/**
	 * Выбраны только топологические узлы. Используется для вставки сетевых
	 * узлов вместо топологических.
	 */
	private boolean physicalNodeSelection = true;

	/**
	 * Выбраны только непривязанные узлы. Используется для генерации узлов
	 * топологический схемы по непривязанным элементам.
	 */
	private boolean unboundNodeSelection = true;

	/**
	 * Выбраны только непривязанные линии. Используется для генерации
	 * топологических линий по непривязанным.
	 */
	private boolean unboundLinkSelection = true;

	/**
	 * Выбраны только непривязанные кабельные пути. Используется для генерации
	 * топологических линий по непривязанным с привязкой каделя к ним.
	 */
	private boolean unboundCableSelection = true;

	/**
	 * Выбраны только непривязанные элементы. Используется для генерации
	 * топологических эоементов по непривязанным.
	 */
	private boolean unboundSelection = true;

	/**
	 * Выбраны только линии. Используется для работы с колодцами.
	 */
	private boolean physicalLinkSelection = true;

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
	 * Очистить список выделенных элементов.
	 */
	public void clear() {
		this.elements.clear();
		this.recalcLocation();
		this.recalcType();
	}

	/**
	 * Добавить в список новый выделенный элемент карты.
	 *
	 * @param mapElement элемент карты
	 */
	public void add(final MapElement mapElement) {
		this.elements.add(mapElement);
		this.recalcLocation();
		this.recalcType();
	}

	/**
	 * Добавить в список множество выделенных элементов.
	 *
	 * @param coll набор элементов
	 */
	public void addAll(final Set<MapElement> coll) {
		this.elements.addAll(coll);
		this.recalcLocation();
		this.recalcType();
	}

	/**
	 * Убрать из списка элемент карты.
	 *
	 * @param mapElement элемент карты
	 */
	public void remove(final MapElement mapElement) {
		this.elements.remove(mapElement);
		this.recalcLocation();
		this.recalcType();
	}

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation() {
		return this.location;
	}

	/**
	 * Пересчитать ГМТ центров выделенных элементов.
	 */
	protected void recalcLocation() {
		double x = 0.0D;
		double y = 0.0D;
		for (final MapElement mapElement : this.elements) {
			final DoublePoint pt = mapElement.getLocation();
			x += pt.getX();
			y += pt.getY();
		}
		x /= this.elements.size();
		y /= this.elements.size();

		this.location.setLocation(x, y);
	}

	/**
	 * определить флаги выборки.
	 */
	private void recalcType() {
		this.physicalNodeSelection = true;
		this.unboundNodeSelection = true;
		this.unboundLinkSelection = true;
		this.unboundCableSelection = true;
		this.unboundSelection = true;
		this.physicalLinkSelection = true;

		for (final MapElement mapElement : this.elements) {
			if(!(mapElement instanceof TopologicalNode))
				this.physicalNodeSelection = false;
			if(!(mapElement instanceof UnboundNode))
				this.unboundNodeSelection = false;
			if(!(mapElement instanceof UnboundLink))
				this.unboundLinkSelection = false;
			if(!(mapElement instanceof CablePath))
				this.unboundCableSelection = false;
			if(!(mapElement instanceof UnboundNode) && !(mapElement instanceof UnboundLink)
					&& !(mapElement instanceof CablePath))
				this.unboundSelection = false;
			if(!(mapElement instanceof PhysicalLink))
				this.physicalLinkSelection = false;
		}
	}

	public void setLocation(final DoublePoint aLocation) {
		throw new UnsupportedOperationException("Cannot set location to selection");
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

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public Set<Characteristic> getCharacteristics() {
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

	/**
	 * Получить список выделенных элементов.
	 * @return список выделенных элементов
	 */
	public Set getElements() {
		return Collections.unmodifiableSet(this.elements);
	}

	/**
	 * Get {@link #physicalNodeSelection}.
	 * @return this.physicalNodeSelection
	 */
	public boolean isPhysicalNodeSelection() {
		return this.physicalNodeSelection;
	}

	/**
	 * Get {@link #unboundNodeSelection}.
	 * @return this.unboundNodeSelection
	 */
	public boolean isUnboundNodeSelection() {
		return this.unboundNodeSelection;
	}

	/**
	 * Get {@link #unboundLinkSelection}.
	 * @return this.unboundLinkSelection
	 */
	public boolean isUnboundLinkSelection() {
		return this.unboundLinkSelection;
	}

	/**
	 * Get {@link #unboundSelection}.
	 * @return this.unboundSelection
	 */
	public boolean isUnboundSelection() {
		return this.unboundSelection;
	}

	/**
	 * Get {@link #physicalLinkSelection}.
	 * @return this.physicalLinkSelection
	 */
	public boolean isPhysicalLinkSelection() {
		return this.physicalLinkSelection;
	}

	/**
	 * Get {@link #unboundCableSelection}.
	 * @return this.unboundCableSelection
	 */
	public boolean isUnboundCableSelection() {
		return this.unboundCableSelection;
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	@SuppressWarnings("deprecation")
	public Map getExportMap() {
		throw new UnsupportedOperationException();
	}

}
