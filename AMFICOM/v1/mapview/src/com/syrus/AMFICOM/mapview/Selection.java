/**
 * $Id: Selection.java,v 1.5 2005/03/09 14:57:55 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * ����� ��������� ���������.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/03/09 14:57:55 $
 * @module mapviewclient_v1
 * @todo copy/paste, properties
 */
public final class Selection
    implements MapElement 
{
	/**
	 * ������ ��������� ���������.
	 */
	protected List elements = new LinkedList();

	/**
	 * ����� ����� ��������� ���������.
	 */
	protected DoublePoint location = new DoublePoint(0, 0);
	
	/**
	 * �������������� �����.
	 */
	protected Map map;
	
	/** 
	 * ������� ������ �������������� ����. ������������ ���
	 * ������� ������� ����� ������ ��������������. 
	 */
	private boolean physicalNodeSelection = true;
	/** 
	 * ������� ������ ������������� ����. ������������ ��� ���������
	 * ����� �������������� ����� �� ������������� ���������.
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
	 * @param map �������������� �����
	 */
	public Selection(Map map)
	{
		this.map = map;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRemoved()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setRemoved(boolean removed)
	{// cannot be removed
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public void setAlarmState(boolean alarmState)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public boolean getAlarmState()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * �������� ������ ���������� ���������.
	 */
	public void clear()
	{
		this.elements.clear();
		recalcLocation();
		recalcType();
	}

	/**
	 * �������� � ������ ����� ���������� ������� �����.
	 * @param mapElement ������� �����
	 */
	public void add(MapElement mapElement)
	{
		this.elements.add(mapElement);
		recalcLocation();
		recalcType();
	}

	/**
	 * �������� � ������ ��������� ���������� ���������.
	 * @param coll ����� ���������
	 */
	public void addAll(Collection coll)
	{
		this.elements.addAll(coll);
		recalcLocation();
		recalcType();
	}

	/**
	 * ������ �� ������ ������� �����.
	 * @param mapElement ������� �����
	 */
	public void remove(MapElement mapElement)
	{
		this.elements.remove(mapElement);
		recalcLocation();
		recalcType();
	}

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation()
	{
		return this.location;
	}

	/**
	 * ����������� ��� ������� ���������� ���������.
	 */
	protected void recalcLocation()
	{
		MapElement me;
		double x = 0.0D;
		double y = 0.0D;
		for(Iterator it = this.elements.iterator(); it.hasNext();)
		{
			me = (MapElement)it.next();
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
	private void recalcType()
	{
		this.physicalNodeSelection = true;
		this.unboundNodeSelection = true;
		this.unboundLinkSelection = true;
		this.unboundCableSelection = true;
		this.unboundSelection = true;
		this.physicalLinkSelection = true;

		for(Iterator it = this.elements.iterator(); it.hasNext();)
		{
			MapElement me = (MapElement)it.next();
			if(! (me instanceof TopologicalNode))
				this.physicalNodeSelection = false;
			if(! (me instanceof UnboundNode))
				this.unboundNodeSelection = false;
			if(! (me instanceof UnboundLink))
				this.unboundLinkSelection = false;
			if(! (me instanceof CablePath))
				this.unboundCableSelection = false;
			if(	! (me instanceof UnboundNode) &&
				! (me instanceof UnboundLink) &&
				! (me instanceof CablePath))
				this.unboundSelection = false;
			if(! (me instanceof PhysicalLink))
				this.physicalLinkSelection = false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLocation(DoublePoint aLocation)
	{
		throw new UnsupportedOperationException("Cannot set location to selection");
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public boolean isSelected()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public MapElementState getState()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSelected(boolean selected)
	{// cannnot be selected
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public Identifier getId()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName()
	{
		return "selection";
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public void setName(String name)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public Collection getCharacteristics() 
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public void addCharacteristic(Characteristic ch)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public void removeCharacteristic(Characteristic ch)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public Map getMap()
	{
		return this.map;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMap(Map map)
	{
		this.map = map;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText()
	{
		return this.getName();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public void revert(MapElementState state)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * �������� ������ ���������� ���������.
	 * @return ������ ���������� ���������
	 */
	public List getElements()
	{
		return Collections.unmodifiableList(this.elements);
	}


	/**
	 * Get {@link #physicalNodeSelection}.
	 * @return this.physicalNodeSelection
	 */
	public boolean isPhysicalNodeSelection()
	{
		return this.physicalNodeSelection;
	}


	/**
	 * Get {@link #unboundNodeSelection}.
	 * @return this.unboundNodeSelection
	 */
	public boolean isUnboundNodeSelection()
	{
		return this.unboundNodeSelection;
	}


	/**
	 * Get {@link #unboundLinkSelection}.
	 * @return this.unboundLinkSelection
	 */
	public boolean isUnboundLinkSelection()
	{
		return this.unboundLinkSelection;
	}


	/**
	 * Get {@link #unboundSelection}.
	 * @return this.unboundSelection
	 */
	public boolean isUnboundSelection()
	{
		return this.unboundSelection;
	}


	/**
	 * Get {@link #physicalLinkSelection}.
	 * @return this.physicalLinkSelection
	 */
	public boolean isPhysicalLinkSelection()
	{
		return this.physicalLinkSelection;
	}


	/**
	 * Get {@link #unboundCableSelection}.
	 * @return this.unboundCableSelection
	 */
	public boolean isUnboundCableSelection()
	{
		return this.unboundCableSelection;
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public java.util.Map getExportMap()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(java.util.Collection)
	 */
	public void setCharacteristics(Collection characteristics) {
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
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(java.util.Collection)
	 */
	public void setCharacteristics0(Collection characteristics) {
		throw new UnsupportedOperationException();
	}
}
