/**
 * $Id: MapTypedElementsContainer.java,v 1.1 2005/08/04 08:21:54 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
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
	 * ������ ��������� ���������.
	 */
	protected Set<MapElement> elements = new HashSet<MapElement>();
	/**
	 * ����� ����� ��������� ���������.
	 */
	protected DoublePoint location = new DoublePoint(0, 0);
	/**
	 * ������� ������ �������������� ����. ������������ ��� ������� �������
	 * ����� ������ ��������������.
	 */
	private boolean physicalNodeSelection = true;
	/**
	 * ������� ������ ������������� ����. ������������ ��� ��������� �����
	 * �������������� ����� �� ������������� ���������.
	 */
	private boolean unboundNodeSelection = true;
	/**
	 * ������� ������ ������������� �����. ������������ ��� ���������
	 * �������������� ����� �� �������������.
	 */
	private boolean unboundLinkSelection = true;
	/**
	 * ������� ������ ������������� ��������� ����. ������������ ��� ���������
	 * �������������� ����� �� ������������� � ��������� ������ � ���.
	 */
	private boolean unboundCableSelection = true;
	/**
	 * ������� ������ ������������� ��������. ������������ ��� ���������
	 * �������������� ��������� �� �������������.
	 */
	private boolean unboundSelection = true;
	/**
	 * ������� ������ �����. ������������ ��� ������ � ���������.
	 */
	private boolean physicalLinkSelection = true;

	/**
	 * �������� ������ ���������� ���������.
	 */
	public void clear() {
		this.elements.clear();
		this.recalcLocation();
		this.recalcType();
	}

	/**
	 * �������� � ������ ����� ���������� ������� �����.
	 *
	 * @param mapElement ������� �����
	 */
	public void add(final MapElement mapElement) {
		this.elements.add(mapElement);
		this.recalcLocation();
		this.recalcType();
	}

	/**
	 * �������� � ������ ��������� ���������� ���������.
	 *
	 * @param coll ����� ���������
	 */
	public void addAll(final Set<MapElement> coll) {
		this.elements.addAll(coll);
		this.recalcLocation();
		this.recalcType();
	}

	/**
	 * ������ �� ������ ������� �����.
	 *
	 * @param mapElement ������� �����
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
	 * ����������� ��� ������� ���������� ���������.
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
	 * ���������� ����� �������.
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
	 * �������� ������ ���������� ���������.
	 * @return ������ ���������� ���������
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
