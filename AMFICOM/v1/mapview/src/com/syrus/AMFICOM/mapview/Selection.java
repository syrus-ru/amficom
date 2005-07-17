/*-
 * $Id: Selection.java,v 1.15 2005/07/17 05:20:55 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������
 * ������������������� ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.mapview;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
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
 * ����� ��������� ���������.
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.15 $, $Date: 2005/07/17 05:20:55 $
 * @module mapviewclient_v1
 * @todo copy/paste, properties
 */
public final class Selection implements MapElement {
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
	 * {@inheritDoc}
	 */
	public boolean isRemoved() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRemoved(boolean removed) {
		// cannot be removed
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
	 * �������� ������ ���������� ���������.
	 */
	public void clear() {
		this.elements.clear();
		recalcLocation();
		recalcType();
	}

	/**
	 * �������� � ������ ����� ���������� ������� �����.
	 *
	 * @param mapElement ������� �����
	 */
	public void add(MapElement mapElement) {
		this.elements.add(mapElement);
		recalcLocation();
		recalcType();
	}

	/**
	 * �������� � ������ ��������� ���������� ���������.
	 *
	 * @param coll ����� ���������
	 */
	public void addAll(Set<MapElement> coll) {
		this.elements.addAll(coll);
		recalcLocation();
		recalcType();
	}

	/**
	 * ������ �� ������ ������� �����.
	 *
	 * @param mapElement ������� �����
	 */
	public void remove(MapElement mapElement) {
		this.elements.remove(mapElement);
		recalcLocation();
		recalcType();
	}

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation() {
		return this.location;
	}

	/**
	 * ����������� ��� ������� ���������� ���������.
	 */
	protected void recalcLocation() {
		MapElement me;
		double x = 0.0D;
		double y = 0.0D;
		for(Iterator it = this.elements.iterator(); it.hasNext();) {
			me = (MapElement )it.next();
			DoublePoint pt = me.getLocation();

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

		for(Iterator it = this.elements.iterator(); it.hasNext();) {
			MapElement me = (MapElement )it.next();
			if(!(me instanceof TopologicalNode))
				this.physicalNodeSelection = false;
			if(!(me instanceof UnboundNode))
				this.unboundNodeSelection = false;
			if(!(me instanceof UnboundLink))
				this.unboundLinkSelection = false;
			if(!(me instanceof CablePath))
				this.unboundCableSelection = false;
			if(!(me instanceof UnboundNode) && !(me instanceof UnboundLink)
					&& !(me instanceof CablePath))
				this.unboundSelection = false;
			if(!(me instanceof PhysicalLink))
				this.physicalLinkSelection = false;
		}
	}

	public void setLocation(DoublePoint aLocation) {
		throw new UnsupportedOperationException(
				"Cannot set location to selection");
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
	public void setSelected(boolean selected) {
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
	public void setName(String name) {
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
	public void revert(MapElementState state) {
		throw new UnsupportedOperationException();
	}

	/**
	 * �������� ������ ���������� ���������.
	 * @return ������ ���������� ���������
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
