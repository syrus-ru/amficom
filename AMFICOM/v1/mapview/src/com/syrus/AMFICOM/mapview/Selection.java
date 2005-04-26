/**
 * $Id: Selection.java,v 1.8 2005/04/26 16:07:31 krupenn Exp $ 
 *
 * Syrus Systems
 * ������-����������� ����� 
 * ������: ������� ������������������
 * ������������������� ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * ����� ��������� ���������.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.8 $, $Date: 2005/04/26 16:07:31 $
 * @module mapviewclient_v1
 * @todo copy/paste, properties
 */
public final class Selection implements MapElement {
	/**
	 * ������ ��������� ���������.
	 */
	protected Set elements = new HashSet();

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
	 * �����������
	 * 
	 * @param map �������������� �����
	 */
	public Selection(Map map) {
		// empty
	}

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
	public void addAll(Set coll) {
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

	/**
	 * {@inheritDoc}
	 */
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
	public Set getCharacteristics() {
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
	 * {@inheritDoc}
	 */
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
	public java.util.Map getExportMap() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(java.util.Set)
	 */
	public void setCharacteristics(Set characteristics) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(java.util.Set)
	 */
	public void setCharacteristics0(Set characteristics) {
		throw new UnsupportedOperationException();
	}
}
