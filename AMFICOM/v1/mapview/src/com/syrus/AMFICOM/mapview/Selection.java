/**
 * $Id: Selection.java,v 1.2 2005/02/01 15:11:28 krupenn Exp $
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
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * ����� ��������� ���������.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/02/01 15:11:28 $
 * @module mapviewclient_v1
 * @todo copy/paste, properties
 */
public final class Selection
    implements MapElement 
{
	protected List elements = new LinkedList();

	protected DoublePoint location = new DoublePoint(0, 0);
	
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
	{
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

	public void clear()
	{
		elements.clear();
		recalcLocation();
		recalcType();
	}

	public void add(MapElement me)
	{
		elements.add(me);
		recalcLocation();
		recalcType();
	}

	public void addAll(Collection coll)
	{
		elements.addAll(coll);
		recalcLocation();
		recalcType();
	}

	public void remove(MapElement me)
	{
		elements.remove(me);
		recalcLocation();
		recalcType();
	}

	public DoublePoint getLocation()
	{
		return location;
	}

	/**
	 * ����������� ��� ������� ���������� ���������.
	 */
	protected void recalcLocation()
	{
		MapElement me;
		double x = 0.0D;
		double y = 0.0D;
		for(Iterator it = elements.iterator(); it.hasNext();)
		{
			me = (MapElement)it.next();
			DoublePoint pt = me.getLocation();

			x += pt.getX();
			y += pt.getY();
		}
		x /= elements.size();
		y /= elements.size();
		
		location.setLocation(x, y);
	}

	/**
	 * ���������� ����� �������.
	 */
	private void recalcType()
	{
		physicalNodeSelection = true;
		unboundNodeSelection = true;
		unboundLinkSelection = true;
		unboundCableSelection = true;
		unboundSelection = true;
		physicalLinkSelection = true;

		for(Iterator it = elements.iterator(); it.hasNext();)
		{
			MapElement me = (MapElement)it.next();
			if(! (me instanceof TopologicalNode))
				physicalNodeSelection = false;
			if(! (me instanceof UnboundNode))
				unboundNodeSelection = false;
			if(! (me instanceof UnboundLink))
				unboundLinkSelection = false;
			if(! (me instanceof CablePath))
				unboundCableSelection = false;
			if(	! (me instanceof UnboundNode) &&
				! (me instanceof UnboundLink) &&
				! (me instanceof CablePath))
				unboundSelection = false;
			if(! (me instanceof PhysicalLink))
				physicalLinkSelection = false;
		}
	}

	public void setLocation(DoublePoint aLocation)
	{
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

	public void setSelected(boolean selected)
	{
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public Identifier getId()
	{
		throw new UnsupportedOperationException();
	}

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
	public List getCharacteristics() 
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
		return map;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMap(Map map)
	{
		this.map = map;
	}

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText()
	{
		return getName();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public void revert(MapElementState state)
	{
		throw new UnsupportedOperationException();
	}
	
	public List getElements()
	{
		return elements;
	}


	public boolean isPhysicalNodeSelection()
	{
		return physicalNodeSelection;
	}


	public boolean isUnboundNodeSelection()
	{
		return unboundNodeSelection;
	}


	public boolean isUnboundLinkSelection()
	{
		return unboundLinkSelection;
	}


	public boolean isUnboundSelection()
	{
		return unboundSelection;
	}


	public boolean isPhysicalLinkSelection()
	{
		return physicalLinkSelection;
	}


	public boolean isUnboundCableSelection()
	{
		return unboundCableSelection;
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public java.util.Map getExportMap()
	{
		throw new UnsupportedOperationException();
	}
}
