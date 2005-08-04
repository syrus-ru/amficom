/**
 * $Id: MapTypedElementsContainer.java,v 1.1 2005/08/04 08:21:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.mapview;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;

public class MapTypedElementsContainer {

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

	/**
	 * Получить список выделенных элементов.
	 * @return список выделенных элементов
	 */
	public Set<MapElement> getElements() {
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

}
